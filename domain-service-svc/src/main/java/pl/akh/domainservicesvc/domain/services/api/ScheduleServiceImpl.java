package pl.akh.domainservicesvc.domain.services.api;

import jakarta.transaction.Transactional;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import pl.akh.domainservicesvc.domain.exceptions.DoctorNotFoundException;
import pl.akh.domainservicesvc.domain.mappers.AppointmentMapper;
import pl.akh.domainservicesvc.domain.mappers.ScheduleMapper;
import pl.akh.domainservicesvc.domain.model.entities.Doctor;
import pl.akh.domainservicesvc.domain.model.entities.Schedule;
import pl.akh.domainservicesvc.domain.repository.AppointmentRepository;
import pl.akh.domainservicesvc.domain.repository.DoctorRepository;
import pl.akh.domainservicesvc.domain.repository.ScheduleRepository;
import pl.akh.domainservicesvc.domain.utils.ScheduleUtils;
import pl.akh.model.rq.ScheduleRQ;
import pl.akh.model.rs.schedules.AppointmentDateRS;
import pl.akh.model.rs.schedules.ScheduleRS;
import pl.akh.model.rs.schedules.SchedulesAppointmentsRS;
import pl.akh.services.ScheduleService;

import java.sql.Timestamp;
import java.time.LocalDateTime;
import java.util.*;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import static java.sql.Timestamp.valueOf;

@Service
@Transactional
@Slf4j
public class ScheduleServiceImpl implements ScheduleService {
    private final DoctorRepository doctorRepository;

    private final ScheduleRepository scheduleRepository;
    private final AppointmentRepository appointmentRepository;

    @Autowired
    public ScheduleServiceImpl(ScheduleRepository scheduleRepository, AppointmentRepository appointmentRepository,
                               DoctorRepository doctorRepository) {
        this.scheduleRepository = scheduleRepository;
        this.appointmentRepository = appointmentRepository;
        this.doctorRepository = doctorRepository;
    }

    @Override
    public Collection<ScheduleRS> getSchedulesByDoctorIdBetweenDates(UUID doctorUUID, LocalDateTime startDate, LocalDateTime endDate) throws DoctorNotFoundException {
        if (!doctorRepository.existsById(doctorUUID)) throw new DoctorNotFoundException();
        return scheduleRepository.getSchedulesByDoctorIdAndStartDateTimeAfterAndEndDateTimeBefore(doctorUUID, valueOf(startDate), valueOf(endDate))
                .stream()
                .map(ScheduleMapper::mapToDto)
                .collect(Collectors.toList());
    }

    @Override
    public SchedulesAppointmentsRS getSchedulesWithAppointmentByDoctorId(UUID doctorUUID, LocalDateTime startDate, LocalDateTime endDate) throws Exception {
        Collection<ScheduleRS> schedulesByDoctorIdBetweenDates = getSchedulesByDoctorIdBetweenDates(doctorUUID, startDate, endDate);
        Collection<AppointmentDateRS> appointmentsByDoctorIdBetweenDates = appointmentRepository.getAppointmentsByDoctorIdAndAppointmentDateIsBetween(doctorUUID, valueOf(startDate), valueOf(endDate))
                .stream()
                .map(AppointmentMapper::mapToDto)
                .toList();
        return SchedulesAppointmentsRS.builder()
                .schedules(schedulesByDoctorIdBetweenDates)
                .appointments(appointmentsByDoctorIdBetweenDates)
                .build();
    }

    @Override
    public Collection<ScheduleRS> insertSchedules(UUID doctorUUID, Collection<ScheduleRQ> schedules) {
        schedules = ScheduleUtils.truncateMinutes(schedules);
        Timestamp min = Timestamp.valueOf(ScheduleUtils.getEarliestStartDateFromSchedules(schedules));
        Timestamp max = Timestamp.valueOf(ScheduleUtils.getLatestEndDateFromSchedules(schedules));

        Stream<ScheduleRQ> savedSchedules = scheduleRepository.getSchedulesByDoctorIdAndStartDateTimeAfterAndEndDateTimeBefore(doctorUUID, min, max)
                .stream()
                .map(schedule -> ScheduleRQ.builder()
                        .startDateTime(schedule.getStartDateTime().toLocalDateTime())
                        .endDateTime(schedule.getEndDateTime().toLocalDateTime())
                        .build());

        ScheduleUtils.validate(Stream.concat(savedSchedules, schedules.stream()).toList());

        Doctor doctor = doctorRepository.findById(doctorUUID).orElseThrow();
        List<Schedule> scheduleEntities = schedules.stream()
                .map(schedule -> createSchedule(schedule, doctor))
                .toList();

        return scheduleRepository.saveAll(scheduleEntities)
                .stream()
                .map(ScheduleMapper::mapToDto)
                .collect(Collectors.toList());
    }

    private Schedule createSchedule(ScheduleRQ scheduleRQ, Doctor doctor) {
        Schedule schedule = new Schedule();
        schedule.setStartDateTime(Timestamp.valueOf(scheduleRQ.getStartDateTime()));
        schedule.setEndDateTime(Timestamp.valueOf(scheduleRQ.getEndDateTime()));
        schedule.setDoctor(doctor);
        return schedule;
    }
}

