package pl.akh.domainservicesvc.infrastructure.pubsub;

@FunctionalInterface
public interface Publisher<T> {
    void publish(T message) throws Exception;
}
