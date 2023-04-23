package pl.akh.domainservicesvc.domain.mappers;

import pl.akh.domainservicesvc.domain.model.entities.Patient;
import pl.akh.model.rs.PatientRS;

public class PatientMapper {

    public static PatientRS mapToDto(Patient entity){
        if (entity == null) return null;
        return PatientRS.builder()
                .addressRS(AddressMapper.mapToDto(entity.getAddress()))
                .firstName(entity.getFirstName())
                .lastName(entity.getLastName())
                .gender(entity.getGender().map())
                .patientUUID(entity.getId())
                .build();
    }
}
