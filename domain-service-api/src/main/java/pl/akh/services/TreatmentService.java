package pl.akh.services;

import pl.akh.model.rq.PrescriptionRQ;
import pl.akh.model.rq.ReferralRQ;
import pl.akh.model.rq.TreatmentRQ;
import pl.akh.model.rq.UpdateTreatmentRQ;
import pl.akh.model.rs.TreatmentRS;

public interface TreatmentService {
    TreatmentRS addTreatmentToAppointment(TreatmentRQ treatmentRQ) throws Exception;

    TreatmentRS updateTreatment(long treatmentId, UpdateTreatmentRQ treatmentRQ) throws Exception;

    void removeTreatmentByAppointmentId(long appointmentId);

    void deleteTreatment(long treatmentId);

    void addPrescription(PrescriptionRQ prescriptionRQ) throws Exception;

    void addReferral(ReferralRQ referralRQ) throws Exception;
}
