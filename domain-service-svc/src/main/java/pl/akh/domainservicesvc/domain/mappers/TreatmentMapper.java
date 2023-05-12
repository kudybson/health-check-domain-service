package pl.akh.domainservicesvc.domain.mappers;

import pl.akh.domainservicesvc.domain.model.entities.Treatment;
import pl.akh.model.rs.TreatmentRS;

public class TreatmentMapper {

    public static TreatmentRS mapToDto(Treatment treatment) {
        if (treatment == null) return null;
        return TreatmentRS.builder()
                .id(treatment.getId())
                .appointmentId(treatment.getAppointment().getId())
                .referralId(treatment.getReferral() != null ? treatment.getReferral().getId() : null)
                .diagnosis(treatment.getDiagnosis())
                .prescriptionId(treatment.getPrescription() != null ? treatment.getPrescription().getId() : null)
                .recommendation(treatment.getRecommendation())
                .build();
    }
}
