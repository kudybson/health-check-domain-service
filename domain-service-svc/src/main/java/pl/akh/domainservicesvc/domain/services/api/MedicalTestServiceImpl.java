package pl.akh.domainservicesvc.domain.services.api;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import pl.akh.domainservicesvc.domain.exceptions.OverwritingTimeException;
import pl.akh.domainservicesvc.domain.mappers.MedicalTestMapper;
import pl.akh.domainservicesvc.domain.model.entities.Department;
import pl.akh.domainservicesvc.domain.model.entities.MedicalTest;
import pl.akh.domainservicesvc.domain.model.entities.MedicalTestSchedule;
import pl.akh.domainservicesvc.domain.model.entities.Patient;
import pl.akh.domainservicesvc.domain.repository.DepartmentRepository;
import pl.akh.domainservicesvc.domain.repository.MedicalTestRepository;
import pl.akh.domainservicesvc.domain.repository.MedicalTestScheduleRepository;
import pl.akh.domainservicesvc.domain.repository.PatientRepository;
import pl.akh.model.common.TestType;
import pl.akh.model.rq.MedicalTestRQ;
import pl.akh.model.rs.MedicalTestRS;
import pl.akh.services.MedicalTestService;

import java.sql.Timestamp;
import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;
import java.util.Collection;
import java.util.Optional;
import java.util.UUID;
import java.util.stream.Collectors;

import static pl.akh.domainservicesvc.domain.repository.MedicalTestRepository.MEDICAL_TEST_DURATION;

@Service
@Slf4j
public class MedicalTestServiceImpl implements MedicalTestService {
    private final MedicalTestScheduleRepository medicalTestScheduleRepository;
    private final MedicalTestRepository medicalTestRepository;
    private final DepartmentRepository departmentRepository;
    private final PatientRepository patientRepository;

    @Autowired
    public MedicalTestServiceImpl(MedicalTestRepository medicalTestRepository, DepartmentRepository departmentRepository, PatientRepository patientRepository,
                                  MedicalTestScheduleRepository medicalTestScheduleRepository) {
        this.medicalTestRepository = medicalTestRepository;
        this.departmentRepository = departmentRepository;
        this.patientRepository = patientRepository;
        this.medicalTestScheduleRepository = medicalTestScheduleRepository;
    }

    @Override
    @Transactional(readOnly = true)
    public Collection<MedicalTestRS> getMedicalTestsByTypeAndDepartmentId(TestType testType, Long departmentId, LocalDateTime start, LocalDateTime end) {
        pl.akh.domainservicesvc.domain.model.entities.TestType testTypeDomain = pl.akh.domainservicesvc.domain.model.entities.TestType.valueOf(testType.name());
        return medicalTestRepository.findAllByDepartmentIdAndType(departmentId, testTypeDomain, Timestamp.valueOf(start), Timestamp.valueOf(end))
                .stream()
                .map(MedicalTestMapper::toDTO)
                .collect(Collectors.toList());
    }

    @Override
    @Transactional(readOnly = true)
    public Collection<MedicalTestRS> getMedicalTestsByDepartmentId(Long departmentId, LocalDateTime start, LocalDateTime end) {
        Timestamp startTime = Timestamp.valueOf(start);
        Timestamp endTime = Timestamp.valueOf(end);
        return medicalTestRepository.findAllByDepartmentId(departmentId, startTime, endTime)
                .stream()
                .map(MedicalTestMapper::toDTO)
                .toList();
    }

    @Override
    @Transactional(readOnly = true)
    public Optional<MedicalTestRS> getMedicalTestById(Long id) {
        return medicalTestRepository.findById(id).map(MedicalTestMapper::toDTO);
    }

    @Override
    @Transactional
    public MedicalTestRS createMedicalTest(MedicalTestRQ medicalTestRQ) throws OverwritingTimeException {
        Department department = departmentRepository.findById(medicalTestRQ.getDepartmentId()).orElseThrow();
        Patient patient = patientRepository.findById(medicalTestRQ.getPatientUUID()).orElseThrow();

        final pl.akh.domainservicesvc.domain.model.entities.TestType testTypeDomain = pl.akh.domainservicesvc.domain.model.entities.TestType.valueOf(medicalTestRQ.getType().name());
        LocalDateTime medicalTestDate = medicalTestRQ.getTestDate().truncatedTo(ChronoUnit.MINUTES);
        final Timestamp medicalTestStartDateTimestamp = Timestamp.valueOf(medicalTestDate);
        final Timestamp medicalTestEndDateTimestamp = Timestamp.valueOf(medicalTestDate.plus(MEDICAL_TEST_DURATION));
        if (medicalTestDate.getMinute() % MEDICAL_TEST_DURATION.toMinutes() != 0) {
            throw new IllegalArgumentException();
        }
        Optional<MedicalTestSchedule> scheduleWhichContainPeriod = medicalTestScheduleRepository.findScheduleWhichContainPeriod(medicalTestRQ.getDepartmentId(),
                testTypeDomain, medicalTestStartDateTimestamp, medicalTestEndDateTimestamp);
        if (scheduleWhichContainPeriod.isEmpty()) {
            throw new IllegalArgumentException();
        }

        Optional<MedicalTest> testAtTheSameTime = medicalTestRepository.findMedicalTestByDepartmentIdAndTypeAndTestDateEquals(
                medicalTestRQ.getDepartmentId(), testTypeDomain, medicalTestStartDateTimestamp);

        if (testAtTheSameTime.isPresent()) {
            throw new OverwritingTimeException();
        }
        MedicalTest medicalTest = new MedicalTest();
        medicalTest.setTestDate(medicalTestStartDateTimestamp);
        medicalTest.setType(testTypeDomain);
        medicalTest.setDepartment(department);
        medicalTest.setPatient(patient);
        MedicalTest saved = medicalTestRepository.save(medicalTest);
        return MedicalTestMapper.toDTO(saved);
    }

    @Override
    @Transactional(readOnly = true)
    public Collection<MedicalTestRS> getAllMedicalByPatientId(UUID id) {
        patientRepository.findById(id).orElseThrow();
        return medicalTestRepository.findMedicalTestsByPatientUUID(id)
                .stream()
                .map(MedicalTestMapper::toDTO)
                .collect(Collectors.toList());
    }

    @Override
    @Transactional
    public void cancelMedicalTest(Long testId) {
        MedicalTest medicalTest = medicalTestRepository.findById(testId).orElseThrow();
        if (medicalTest.getTestDate().toLocalDateTime().isBefore(LocalDateTime.now())) {
            return;
        }
        medicalTestRepository.delete(medicalTest);
    }
}
