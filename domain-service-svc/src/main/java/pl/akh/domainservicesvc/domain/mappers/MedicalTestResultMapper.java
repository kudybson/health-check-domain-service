package pl.akh.domainservicesvc.domain.mappers;

import pl.akh.domainservicesvc.domain.model.entities.MedicalTestResult;
import pl.akh.model.rs.MedicalTestResultRS;

public class MedicalTestResultMapper {
    public static MedicalTestResultRS toDTO(MedicalTestResult entity) {
        if (entity == null) return null;
        throw new RuntimeException("plz implement ");
    }
}
