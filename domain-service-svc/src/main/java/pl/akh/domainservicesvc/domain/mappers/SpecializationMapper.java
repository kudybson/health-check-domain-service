package pl.akh.domainservicesvc.domain.mappers;

import pl.akh.domainservicesvc.domain.model.entities.Specialization;

public class SpecializationMapper {
    public static Specialization toEntity(pl.akh.model.common.Specialization specialization) {
        return Specialization.valueOf(specialization.toString());
    }
}
