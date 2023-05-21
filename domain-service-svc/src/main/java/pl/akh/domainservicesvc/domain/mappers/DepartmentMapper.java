package pl.akh.domainservicesvc.domain.mappers;

import pl.akh.domainservicesvc.domain.model.entities.Department;
import pl.akh.model.rs.DepartmentRS;

public class DepartmentMapper {
    public static DepartmentRS mapToDto(Department entity) {
        if (entity == null) return null;
        return DepartmentRS.builder()
                .id(entity.getId())
                .name(entity.getName())
                .address(AddressMapper.mapToDto(entity.getAddress()))
                .build();
    }
}
