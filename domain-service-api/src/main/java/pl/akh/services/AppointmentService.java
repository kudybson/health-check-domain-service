package pl.akh.services;

import pl.akh.model.rq.AppointmentRQ;
import pl.akh.model.rs.AppointmentRS;

import java.util.Collection;
import java.util.Optional;
import java.util.UUID;

public interface AppointmentService {

    Optional<AppointmentRS> getAppointmentById(Long id);

    AppointmentRS createAppointment(AppointmentRQ appointmentRQ) throws Exception;

    AppointmentRS removeAppointmentById(Long id);

    Collection<AppointmentRS> getAppointmentsByDoctorId(UUID doctorUUID);

    Collection<AppointmentRS> getAppointmentsByPatientId(UUID patientUUID);

    Collection<AppointmentRS> getAppointmentsByDepartmentId(Long id);

    AppointmentRS addCommentToAppointment(Long appointmentId, String comment);
}
