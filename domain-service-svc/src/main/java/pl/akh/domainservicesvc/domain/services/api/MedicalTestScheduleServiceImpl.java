package pl.akh.domainservicesvc.domain.services.api;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import pl.akh.domainservicesvc.domain.mappers.MedicalTestMapper;
import pl.akh.domainservicesvc.domain.mappers.MedicalTestScheduleMapper;
import pl.akh.domainservicesvc.domain.model.entities.Department;
import pl.akh.domainservicesvc.domain.model.entities.MedicalTestSchedule;
import pl.akh.domainservicesvc.domain.repository.DepartmentRepository;
import pl.akh.domainservicesvc.domain.repository.MedicalTestRepository;
import pl.akh.domainservicesvc.domain.repository.MedicalTestScheduleRepository;
import pl.akh.domainservicesvc.domain.utils.ScheduleUtils;
import pl.akh.model.common.TestType;
import pl.akh.model.rq.MedicalTestScheduleRQ;
import pl.akh.model.rq.ScheduleRQ;
import pl.akh.model.rs.schedules.MedicalTestSchedulesRS;
import pl.akh.model.rs.schedules.ScheduleRS;
import pl.akh.services.MedicalTestScheduleService;

import java.sql.Time;
import java.sql.Timestamp;
import java.time.LocalDateTime;
import java.util.Collection;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;

import static pl.akh.domainservicesvc.domain.repository.MedicalTestRepository.MEDICAL_TEST_DURATION;

@Service
@Slf4j
public class MedicalTestScheduleServiceImpl implements MedicalTestScheduleService {

    private final MedicalTestScheduleRepository medicalTestScheduleRepository;
    private final DepartmentRepository departmentRepository;

    private final MedicalTestRepository medicalTestRepository;

    @Autowired
    public MedicalTestScheduleServiceImpl(MedicalTestScheduleRepository medicalTestScheduleRepository, DepartmentRepository departmentRepository, MedicalTestRepository medicalTestRepository) {
        this.medicalTestScheduleRepository = medicalTestScheduleRepository;
        this.departmentRepository = departmentRepository;
        this.medicalTestRepository = medicalTestRepository;
    }

    @Override
    @Transactional
    public Collection<ScheduleRS> insertMedicalTestSchedules(MedicalTestScheduleRQ medicalTestScheduleRQ) {
        log.debug("Starting inserting schedules to department:{}, type:{}", medicalTestScheduleRQ.getDepartmentId(), medicalTestScheduleRQ.getTestType());
        Collection<ScheduleRQ> schedules = ScheduleUtils.truncateMinutes(medicalTestScheduleRQ.getSchedules());
        Collection<ScheduleRQ> dbSchedules = getSchedulesFromDbWithPossibleCollision(medicalTestScheduleRQ);
        dbSchedules.addAll(schedules);
        ScheduleUtils.validate(dbSchedules);

        Department department = departmentRepository.findById(medicalTestScheduleRQ.getDepartmentId()).orElseThrow();

        List<MedicalTestSchedule> schedulesEntities = schedules.stream()
                .map(schedule -> createSchedule(department,
                        pl.akh.domainservicesvc.domain.model.entities.TestType.valueOf(medicalTestScheduleRQ.getTestType().toString()),
                        schedule))
                .toList();

        log.debug("Schedules saved successfully.");
        return medicalTestScheduleRepository.saveAll(schedulesEntities)
                .stream()
                .map(MedicalTestScheduleMapper::toScheduleRS)
                .collect(Collectors.toList());
    }

    @Override
    @Transactional(readOnly = true)
    public MedicalTestSchedulesRS getMedicalTestSchedules(Long departmentId, TestType testType, LocalDateTime startDate, LocalDateTime endDate) {
        pl.akh.domainservicesvc.domain.model.entities.TestType testTypeDomain = pl.akh.domainservicesvc.domain.model.entities.TestType.valueOf(testType.name());

        Collection<MedicalTestSchedule> schedules = medicalTestScheduleRepository.findByDepartmentAndTypeBetweenDates(departmentId,
                testTypeDomain, Timestamp.valueOf(startDate), Timestamp.valueOf(endDate));

        List<ScheduleRS> assignedTests = medicalTestRepository.findScheduleDatesBetween(departmentId, testTypeDomain, Timestamp.valueOf(startDate), Timestamp.valueOf(endDate))
                .stream()
                .map(medicalTestStartDate -> ScheduleRS.builder()
                        .startDateTime(medicalTestStartDate.toLocalDateTime())
                        .endDateTime(medicalTestStartDate.toLocalDateTime().plus(MEDICAL_TEST_DURATION))
                        .build())
                .sorted(Comparator.comparing(ScheduleRS::getStartDateTime))
                .toList();

        return MedicalTestSchedulesRS.builder()
                .type(testType)
                .departmentId(departmentId)
                .schedules(schedules.stream()
                        .map(MedicalTestScheduleMapper::toScheduleRS)
                        .sorted(Comparator.comparing(ScheduleRS::getStartDateTime))
                        .toList())
                .assignedSchedules(assignedTests)
                .build();
    }

    private MedicalTestSchedule createSchedule(Department department, pl.akh.domainservicesvc.domain.model.entities.TestType type,
                                               ScheduleRQ schedule) {
        MedicalTestSchedule medicalTestSchedule = new MedicalTestSchedule();
        medicalTestSchedule.setDepartment(department);
        medicalTestSchedule.setType(type);
        medicalTestSchedule.setStartDateTime(Timestamp.valueOf(schedule.getStartDateTime()));
        medicalTestSchedule.setEndDateTime(Timestamp.valueOf(schedule.getEndDateTime()));
        return medicalTestSchedule;
    }

    private Collection<ScheduleRQ> getSchedulesFromDbWithPossibleCollision(MedicalTestScheduleRQ medicalTestScheduleRQ) {
        Timestamp min = Timestamp.valueOf(ScheduleUtils.getEarliestStartDateFromSchedules(medicalTestScheduleRQ.getSchedules()));
        Timestamp max = Timestamp.valueOf(ScheduleUtils.getLatestEndDateFromSchedules(medicalTestScheduleRQ.getSchedules()));

        return medicalTestScheduleRepository.findByDepartmentAndTypeBetweenDates(medicalTestScheduleRQ.getDepartmentId(),
                        pl.akh.domainservicesvc.domain.model.entities.TestType.valueOf(medicalTestScheduleRQ.getTestType().name()), min, max)
                .stream()
                .map(schedule -> ScheduleRQ.builder()
                        .startDateTime(schedule.getStartDateTime().toLocalDateTime())
                        .endDateTime(schedule.getEndDateTime().toLocalDateTime())
                        .build())
                .collect(Collectors.toList());

    }
}
