package pl.akh.domainservicesvc.externalservices;

public interface AbstractFactory<T> {
    T create(String type);
}
