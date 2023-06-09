package pl.akh.domainservicesvc.domain.mappers;

import pl.akh.domainservicesvc.domain.model.entities.Receptionist;
import pl.akh.model.rs.ReceptionistRS;

public class ReceptionistMapper {

    public static ReceptionistRS mapToDto(Receptionist entity) {
        if (entity == null) return null;
        return ReceptionistRS.builder()
                .receptionistUUID(entity.getId())
                .departmentId(entity.getDepartment().getId())
                .firstname(entity.getFirstName())
                .lastname(entity.getLastName())
                .build();
    }
}
