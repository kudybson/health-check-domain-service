package pl.akh.domainservicesvc.infrastructure.pubsub;

public interface Publisher<T> {
    void publish(T message) throws Exception;
}
