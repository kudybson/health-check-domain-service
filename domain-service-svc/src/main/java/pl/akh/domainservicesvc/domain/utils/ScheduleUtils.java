package pl.akh.domainservicesvc.domain.utils;

import pl.akh.domainservicesvc.domain.repository.AppointmentRepository;
import pl.akh.model.rq.ScheduleRQ;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;
import java.util.Collection;
import java.util.Objects;
import java.util.stream.Collectors;

public class ScheduleUtils {

    public static Collection<ScheduleRQ> truncateMinutes(Collection<ScheduleRQ> schedules) {
        return schedules.stream()
                .peek(schedule -> schedule.setStartDateTime(schedule.getStartDateTime().truncatedTo(ChronoUnit.MINUTES)))
                .peek(schedule -> schedule.setEndDateTime(schedule.getEndDateTime().truncatedTo(ChronoUnit.MINUTES)))
                .collect(Collectors.toList());
    }

    public static LocalDateTime getEarliestStartDateFromSchedules(Collection<ScheduleRQ> schedules) {
        return schedules.stream()
                .map(ScheduleRQ::getStartDateTime)
                .min(LocalDateTime::compareTo)
                .orElseThrow();
    }


    public static LocalDateTime getLatestEndDateFromSchedules(Collection<ScheduleRQ> schedules) {
        return schedules.stream()
                .map(ScheduleRQ::getEndDateTime)
                .max(LocalDateTime::compareTo)
                .orElseThrow();
    }

    public static void validate(Collection<ScheduleRQ> schedules) {
        if (isAnyOutdated(schedules) || !isTimeValid(schedules) || !isChronologyValid(schedules)) {
            throw new IllegalArgumentException();
        }
    }

    private static boolean isAnyOutdated(Collection<ScheduleRQ> schedules) {
        LocalDate now = LocalDate.now().minusDays(1);
        return schedules.stream()
                .anyMatch(schedule -> schedule.getStartDateTime().toLocalDate().isBefore(now) ||
                        schedule.getEndDateTime().toLocalDate().isBefore(now));
    }

    private static boolean isTimeValid(Collection<ScheduleRQ> schedules) {
        return schedules.stream()
                .allMatch(ScheduleUtils::isScheduleValidAppointmentTime);
    }

    private static boolean isScheduleValidAppointmentTime(ScheduleRQ scheduleRQ) {
        if (!Objects.equals(scheduleRQ.getEndDateTime().toLocalDate(), scheduleRQ.getStartDateTime().toLocalDate()))
            return false;
        long appointmentTime = AppointmentRepository.APPOINTMENT_TIME.toMinutes();
        if ((scheduleRQ.getStartDateTime().getMinute() % appointmentTime) != 0) return false;
        return (scheduleRQ.getEndDateTime().getMinute() % appointmentTime) == 0;
    }

    private static boolean isChronologyValid(Collection<ScheduleRQ> schedules) {
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

    private static boolean areSchedulesOverlapped(ScheduleRQ first, ScheduleRQ second) {
        if (isBetweenDates(second.getStartDateTime(), first.getStartDateTime(), first.getEndDateTime())) return true;
        if (isBetweenDates(second.getEndDateTime(), first.getStartDateTime(), first.getEndDateTime())) return true;
        return Objects.equals(first, second);
    }

    private static boolean isBetweenDates(LocalDateTime date, LocalDateTime min, LocalDateTime max) {
        return date.isAfter(min) && date.isBefore(max);
    }
}
