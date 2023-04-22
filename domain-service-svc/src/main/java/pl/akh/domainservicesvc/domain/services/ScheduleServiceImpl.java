package pl.akh.domainservicesvc.domain.services;

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
import pl.akh.model.rq.ScheduleRQ;
import pl.akh.model.rs.schedules.AppointmentDateRS;
import pl.akh.model.rs.schedules.ScheduleRS;
import pl.akh.model.rs.schedules.SchedulesAppointmentsRS;
import pl.akh.services.ScheduleService;

import java.sql.Timestamp;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;
import java.util.Collection;
import java.util.List;
import java.util.Objects;
import java.util.UUID;
import java.util.stream.Collectors;

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
        schedules = truncateMinutes(schedules);
        if (isAnyOutdated(schedules) || !isTimeValid(schedules) || !isChronologyValid(schedules)) {
            throw new IllegalArgumentException();
        }

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

    private boolean isAnyOutdated(Collection<ScheduleRQ> schedules) {
        LocalDate now = LocalDate.now();
        return schedules.stream()
                .anyMatch(schedule -> schedule.getStartDateTime().toLocalDate().isBefore(now) ||
                        schedule.getEndDateTime().toLocalDate().isBefore(now));
    }

    private Collection<ScheduleRQ> truncateMinutes(Collection<ScheduleRQ> schedules) {
        return schedules.stream()
                .peek(schedule -> schedule.setStartDateTime(schedule.getStartDateTime().truncatedTo(ChronoUnit.MINUTES)))
                .peek(schedule -> schedule.setEndDateTime(schedule.getEndDateTime().truncatedTo(ChronoUnit.MINUTES)))
                .collect(Collectors.toList());
    }

    private boolean isTimeValid(Collection<ScheduleRQ> schedules) {
        return schedules.stream()
                .allMatch(this::isScheduleValidAppointmentTime);
    }

    private boolean isScheduleValidAppointmentTime(ScheduleRQ scheduleRQ) {
        long appointmentTime = AppointmentRepository.APPOINTMENT_TIME.toMinutes();
        if ((scheduleRQ.getStartDateTime().getMinute() % appointmentTime) != 0) return false;
        return (scheduleRQ.getEndDateTime().getMinute() % appointmentTime) == 0;
    }

    private boolean isChronologyValid(Collection<ScheduleRQ> schedules) {
        //validates that start date is not after end date and that they are not equal
        boolean startIsNotBeforeEnd = schedules.stream()
                .anyMatch(schedule -> !schedule.getStartDateTime().isBefore(schedule.getEndDateTime()));
        if (startIsNotBeforeEnd) return false;

        for (ScheduleRQ schedule : schedules) {
            for (ScheduleRQ scheduleToCheck : schedules) {
                if (schedule == scheduleToCheck) continue;
                if (areSchedulesOverlapped(schedule, scheduleToCheck)) return false;
            }
        }
        return true;
    }

    private boolean areSchedulesOverlapped(ScheduleRQ first, ScheduleRQ second) {
        if (isBetweenDates(second.getStartDateTime(), first.getStartDateTime(), first.getEndDateTime())) return true;
        if (isBetweenDates(second.getEndDateTime(), first.getStartDateTime(), first.getEndDateTime())) return true;
        return Objects.equals(first, second);
    }

    private boolean isBetweenDates(LocalDateTime date, LocalDateTime min, LocalDateTime max) {
        return date.isAfter(min) && date.isBefore(max);
    }
}

