package pl.akh.services;

import pl.akh.model.rq.AppointmentRQ;
import pl.akh.model.rs.AppointmentRS;

import java.time.LocalDateTime;
import java.util.Collection;
import java.util.Optional;
import java.util.UUID;

public interface AppointmentService {

    Optional<AppointmentRS> getAppointmentById(Long id);

    AppointmentRS createAppointment(AppointmentRQ appointmentRQ) throws Exception;

    void removeAppointmentById(Long id) throws Exception;

    Collection<AppointmentRS> getAppointmentsByDoctorId(UUID doctorUUID, LocalDateTime start, LocalDateTime end);

    Collection<AppointmentRS> getAppointmentsByPatientId(UUID patientUUID, LocalDateTime start, LocalDateTime end);

    Collection<AppointmentRS> getAppointmentsByDepartmentId(Long id, LocalDateTime start, LocalDateTime end);

    AppointmentRS addCommentToAppointment(Long id, String comment);
}
