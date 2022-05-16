package model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import service.KafkaTimedMessage;

import java.time.Instant;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class WeatherDataAggregation implements KafkaTimedMessage {
    private Long id;
    private Instant timestamp;
    private Double avgTempCelsius;
    private Double avgHumidity;


    @Override
    public Long getEpochMills() {
        return timestamp.toEpochMilli();
    }
}
