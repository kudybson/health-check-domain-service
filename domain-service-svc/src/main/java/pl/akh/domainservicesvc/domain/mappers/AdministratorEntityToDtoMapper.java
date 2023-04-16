package pl.akh.domainservicesvc.domain.mappers;

import pl.akh.domainservicesvc.domain.model.entities.Administrator;
import pl.akh.model.rs.AdministratorRS;

public class AdministratorEntityToDtoMapper implements EntityToDtoMapper<Administrator, AdministratorRS> {
    @Override
    public AdministratorRS mapToDto(Administrator entity) {
        return AdministratorRS.builder()
                .administratorUUID(entity.getId())
                .firstname(entity.getFirstName())
                .lastname(entity.getSecondName())
                .departmentId(entity.getDepartment().getId())
                .build();
    }
}
