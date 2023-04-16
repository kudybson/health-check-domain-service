package pl.akh.services;

import pl.akh.model.rq.PrescriptionRQ;
import pl.akh.model.rq.TreatmentRQ;
import pl.akh.model.rs.TreatmentRS;

public interface TreatmentService {
    TreatmentRS addTreatmentToAppointment(TreatmentRQ comment);

    TreatmentRS updateTreatment(long treatmentId, TreatmentRQ comment);

    void removeTreatmentByAppointmentId(long appointmentId);

    void deleteTreatment(long treatmentId);

    void addPrescription(PrescriptionRQ prescriptionRQ);

}
