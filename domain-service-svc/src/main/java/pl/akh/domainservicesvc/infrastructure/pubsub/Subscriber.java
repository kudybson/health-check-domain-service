package pl.akh.domainservicesvc.infrastructure.pubsub;

public interface Subscriber<T> {
    T onMessage() throws Exception;
}
