package pl.akh.services;

import pl.akh.model.rq.appointment.CreateAppointmentRQ;
import pl.akh.model.rq.appointment.CreateTreatmentRQ;
import pl.akh.model.rs.AppointmentRS;

import java.util.Collection;
import java.util.UUID;

public interface AppointmentService {
    Collection<AppointmentRS> getAllAppointments();

    AppointmentRS getAppointmentById(long id);

    AppointmentRS createAppointment(CreateAppointmentRQ createAppointmentRQ);

    AppointmentRS removeAppointment(long id);

    Collection<AppointmentRS> getAppointmentsByDoctorId(UUID doctorUUID);

    Collection<AppointmentRS> getAppointmentsByPatientId(UUID patientUUID);

    Collection<AppointmentRS> getAppointmentsByDepartmentId(long id);

    AppointmentRS addCommentToAppointment(long appointmentId, String comment);
}
