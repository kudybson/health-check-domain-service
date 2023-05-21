package pl.akh.domainservicesvc.domain.mappers;

import pl.akh.domainservicesvc.domain.model.entities.Prescription;
import pl.akh.model.rs.PrescriptionRS;

public class PrescriptionMapper {

    public static PrescriptionRS mapToDto(Prescription prescription) {
        if (prescription == null) return null;
        return PrescriptionRS.builder()
                .id(prescription.getId())
                .treatmentRS(TreatmentMapper.mapToDto(prescription.getTreatment()))
                .expirationDate(prescription.getExpirationDate())
                .code(prescription.getCode())
                .description(prescription.getDescription())
                .build();
    }
}
