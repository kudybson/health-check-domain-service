package pl.akh.domainservicesvc.domain.mappers;

import pl.akh.domainservicesvc.domain.model.entities.Treatment;
import pl.akh.model.rs.TreatmentRS;

public class TreatmentMapper {

    public static TreatmentRS mapToDto(Treatment treatment) {
        if (treatment == null) return null;
        return TreatmentRS.builder()
                .id(treatment.getId())
                .appointmentId(treatment.getAppointment().getId())
                .referralId(treatment.getReferral().getId())
                .diagnosis(treatment.getDiagnosis())
                .prescriptionId(treatment.getPrescription().getId())
                .recommendation(treatment.getRecommendation())
                .build();
    }
}
