package pl.akh.domainservicesvc.infrastructure;

import org.springframework.data.util.Pair;

public interface AbstractFactory<T> {
    T create(String type, Pair<String, String>... metaParams);
}
