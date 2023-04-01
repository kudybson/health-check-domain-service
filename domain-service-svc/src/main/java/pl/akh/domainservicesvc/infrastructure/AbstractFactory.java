package pl.akh.domainservicesvc.infrastructure;

public interface AbstractFactory<T> {
    T create(String type);
}
