package web;

import lombok.RequiredArgsConstructor;
import model.WeatherMeasurement;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class MeasurementSender {
    private final KafkaTemplate<Long, WeatherMeasurement> kafkaTemplate;

    public void send(WeatherMeasurement measurement){
        kafkaTemplate.send("weather-data",measurement.getId(),measurement);
    }
}
