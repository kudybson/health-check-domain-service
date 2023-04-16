package pl.akh.domainservicesvc.domain.mappers;

import jakarta.validation.constraints.NotNull;
import pl.akh.domainservicesvc.domain.model.entities.Administrator;
import pl.akh.domainservicesvc.domain.model.entities.Receptionist;
import pl.akh.model.rs.AdministratorRS;
import pl.akh.model.rs.ReceptionistRS;

import java.util.Objects;

public class MapperFactory {
    public static <E, D> EntityToDtoMapper getEntityToDtoMapper(Class entityClass, Class dtoClass) {
        if (Objects.equals(entityClass, Administrator.class) && Objects.equals(dtoClass, AdministratorRS.class)) {
            return new AdministratorEntityToDtoMapper();
        }
        if (Objects.equals(entityClass, Receptionist.class) && Objects.equals(dtoClass, ReceptionistRS.class)) {
            return new ReceptionistEntityToDtoMapper();
        }
        throw new UnsupportedOperationException();
    }
}
