package pl.akh.services;

import pl.akh.model.rq.AppointmentRQ;
import pl.akh.model.rs.AppointmentRS;

import java.util.Collection;
import java.util.UUID;

public interface AppointmentService {
    Collection<AppointmentRS> getAllAppointments();
    AppointmentRS getAppointmentById(long id);
    AppointmentRS createAppointment(AppointmentRQ appointmentRQ);
    AppointmentRS removeAppointmentById(long id);
    Collection<AppointmentRS> getAppointmentsByDoctorId(UUID doctorUUID);
    Collection<AppointmentRS> getAppointmentsByPatientId(UUID patientUUID);
    Collection<AppointmentRS> getAppointmentsByDepartmentId(long id);
    AppointmentRS addCommentToAppointment(long appointmentId, String comment);
}
