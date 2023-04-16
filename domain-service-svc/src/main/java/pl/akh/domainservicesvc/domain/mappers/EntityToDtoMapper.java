package pl.akh.domainservicesvc.domain.mappers;

@FunctionalInterface
public interface EntityToDtoMapper<E, D> {
    D mapToDto(E entity);
}
