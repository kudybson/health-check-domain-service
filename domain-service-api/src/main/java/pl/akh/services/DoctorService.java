package pl.akh.services;

import pl.akh.model.rq.schedule.ScheduleRQ;
import pl.akh.model.rs.DoctorRS;
import pl.akh.model.common.Specialization;
import pl.akh.model.rs.RateRS;
import pl.akh.model.rs.ScheduleRS;

import java.util.Collection;
import java.util.UUID;

public interface DoctorService {
    Collection<DoctorRS> getAllDoctors();

    Collection<DoctorRS> getAllDoctors(long pageNumber, long pageSize);

    DoctorRS getDoctorById(UUID doctorUUID);

    Collection<DoctorRS> getDoctorsByDepartment(long departmentId);

    Collection<DoctorRS> getDoctorsByName(String firstName, String lastName);

    Collection<DoctorRS> getDoctorsBySpecialization(Specialization specialization);

    Collection<ScheduleRS> getSchedulesByDoctorId(UUID doctorUUID);

    Collection<ScheduleRS> insertSchedules(UUID doctorUUID, Collection<ScheduleRQ> schedules);

    Collection<RateRS> getDoctorRates(UUID doctorUUID);
}
