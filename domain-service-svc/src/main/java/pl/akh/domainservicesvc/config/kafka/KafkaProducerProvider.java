package pl.akh.domainservicesvc.config.kafka;

import org.apache.kafka.clients.producer.ProducerConfig;
import org.apache.kafka.common.serialization.StringSerializer;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.kafka.core.DefaultKafkaProducerFactory;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.kafka.core.ProducerFactory;
import org.springframework.kafka.support.serializer.JsonSerializer;
import pl.akh.notificationserviceapi.model.Notification;

import java.util.HashMap;
import java.util.Map;

@Configuration
@ConditionalOnProperty(prefix = "notification", name = "service", havingValue = "async")
public class KafkaProducerProvider {
    @Value(value = "${spring.kafka.bootstrap-servers}")
    private String bootstrapAddress;
    @Bean
    public ProducerFactory<String, Notification> producerFactory() {
        Map<String, Object> conf = new HashMap<>();
        conf.put(ProducerConfig.CLIENT_ID_CONFIG, "domain-service");
        conf.put(ProducerConfig.BOOTSTRAP_SERVERS_CONFIG, bootstrapAddress);
        conf.put(ProducerConfig.KEY_SERIALIZER_CLASS_CONFIG, StringSerializer.class);
        conf.put(ProducerConfig.VALUE_SERIALIZER_CLASS_CONFIG, JsonSerializer.class);
        return new DefaultKafkaProducerFactory<>(conf);
    }

    @Bean
    public KafkaTemplate<String, Notification> getNotificationServiceKafkaTemplate() {
        return new KafkaTemplate<>(producerFactory());
    }

}
