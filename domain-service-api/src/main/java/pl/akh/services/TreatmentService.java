package pl.akh.services;

import pl.akh.model.rq.appointment.CreateTreatmentRQ;
import pl.akh.model.rs.AppointmentRS;
import pl.akh.model.rs.TreatmentRS;

public interface TreatmentService {
    TreatmentRS addTreatmentToAppointment(long appointmentId, CreateTreatmentRQ comment);
    TreatmentRS updateTreatment(long treatmentId, CreateTreatmentRQ comment);
    void removeTreatmentFromAppointment(long appointmentId);
    void deleteTreatment(long treatmentId);

}
