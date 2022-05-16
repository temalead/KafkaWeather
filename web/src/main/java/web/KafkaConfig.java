package web;

import com.fasterxml.jackson.databind.ObjectMapper;
import model.WeatherMeasurement;
import org.apache.kafka.clients.producer.ProducerConfig;
import org.apache.kafka.common.serialization.LongSerializer;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.kafka.annotation.EnableKafka;
import org.springframework.kafka.core.DefaultKafkaProducerFactory;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.kafka.support.serializer.JsonSerializer;

import java.util.Map;

@Configuration
@EnableKafka
public class KafkaConfig {

    @Bean
    public ObjectMapper objectMapper() {
        return new ObjectMapper();
    }

    @Bean
    public DefaultKafkaProducerFactory<Long, WeatherMeasurement> kafkaProducerFactory() {
        return new DefaultKafkaProducerFactory<>(producerConfig(), new LongSerializer(), new JsonSerializer<>());
    }


    private Map<String, Object> producerConfig() {
        return Map.of(
                ProducerConfig.BOOTSTRAP_SERVERS_CONFIG, "localhost:9092"
        );
    }

    @Bean
    public KafkaTemplate<Long, WeatherMeasurement> kafkaTemplate(DefaultKafkaProducerFactory<Long, WeatherMeasurement> kafkaProducerFactory) {
        return new KafkaTemplate<>(kafkaProducerFactory);
    }
}

