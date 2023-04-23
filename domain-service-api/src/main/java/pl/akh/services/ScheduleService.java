package pl.akh.services;

import jdk.jfr.Description;
import pl.akh.model.rq.ScheduleRQ;
import pl.akh.model.rs.schedules.ScheduleRS;
import pl.akh.model.rs.schedules.SchedulesAppointmentsRS;

import java.time.LocalDateTime;
import java.util.Collection;
import java.util.UUID;

public interface ScheduleService {
    @Description(value = "no need to implement yet")
    Collection<ScheduleRS> getSchedulesByDoctorIdBetweenDates(UUID doctorUUID, LocalDateTime startDate, LocalDateTime endDate) throws Exception;

    SchedulesAppointmentsRS getSchedulesWithAppointmentByDoctorId(UUID doctorUUID, LocalDateTime startDate, LocalDateTime endDate) throws Exception;

    Collection<ScheduleRS> insertSchedules(UUID doctorUUID, Collection<ScheduleRQ> schedules);
}
