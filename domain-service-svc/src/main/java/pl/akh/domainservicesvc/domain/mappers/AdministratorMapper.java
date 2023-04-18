package pl.akh.domainservicesvc.domain.mappers;

import pl.akh.domainservicesvc.domain.model.entities.Administrator;
import pl.akh.model.rs.AdministratorRS;

public class AdministratorMapper {
    public static AdministratorRS mapToDto(Administrator entity) {
        return AdministratorRS.builder()
                .administratorUUID(entity.getId())
                .firstname(entity.getFirstName())
                .lastname(entity.getLastName())
                .departmentId(entity.getDepartment().getId())
                .build();
    }
}
