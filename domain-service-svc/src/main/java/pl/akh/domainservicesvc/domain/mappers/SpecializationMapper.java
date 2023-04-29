package pl.akh.domainservicesvc.domain.mappers;

import pl.akh.domainservicesvc.domain.model.entities.enums.Specialization;

public class SpecializationMapper {
    public static Specialization toEntity(pl.akh.model.common.Specialization specialization) {
        if(specialization == null) return null;
        return Specialization.valueOf(specialization.toString());
    }
}
