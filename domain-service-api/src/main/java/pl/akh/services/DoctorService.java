package pl.akh.services;

import jdk.jfr.Description;
import pl.akh.model.common.Specialization;
import pl.akh.model.rq.ScheduleRQ;
import pl.akh.model.rs.DoctorRS;
import pl.akh.model.rs.RatingRS;
import pl.akh.model.rs.schedules.ScheduleRS;
import pl.akh.model.rs.schedules.SchedulesAppointmentsRS;

import java.time.LocalDate;
import java.util.Collection;
import java.util.UUID;

public interface DoctorService {
    Collection<DoctorRS> getAllDoctors();

    Collection<DoctorRS> getAllDoctors(long pageNumber, long pageSize);

    DoctorRS getDoctorById(UUID doctorUUID);

    Collection<DoctorRS> getDoctorsByDepartment(long departmentId);

    Collection<DoctorRS> getDoctorsByName(String firstName, String lastName);

    Collection<DoctorRS> getDoctorsBySpecialization(Specialization specialization);

    @Description(value = "no need to implement yet")
    Collection<ScheduleRS> getSchedulesByDoctorIdBetweenDates(UUID doctorUUID, LocalDate startDate, LocalDate endDate);

    SchedulesAppointmentsRS getSchedulesWithAppointmentByDoctorId(UUID doctorUUID, LocalDate startDate, LocalDate endDate);

    Collection<ScheduleRS> insertSchedules(UUID doctorUUID, Collection<ScheduleRQ> schedules);

    Collection<RatingRS> getDoctorRates(UUID doctorUUID);
}
