package streams.service;

import lombok.extern.slf4j.Slf4j;
import model.WeatherDataAggregation;
import org.apache.kafka.streams.kstream.KStream;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.stereotype.Service;

import java.util.function.Function;



@Configuration
@Slf4j
public class WeatherAlarmProcessor {

    private static final Double TEMPERATURE_ALARM_THRESHOLD = 33.0;
    private static final Double HUMIDITY_ALARM_THRESHOLD = 50.0;

    @Bean
    public Function<KStream<Long, WeatherDataAggregation>, KStream<Long, WeatherDataAggregation>> alarmProcessor() {
        return input ->
                input.filter(this::thresholdReached);

    }

    private boolean thresholdReached(Long id, WeatherDataAggregation aggregation) {
        Double measuredAvgTempCelsius = aggregation.getAvgTempCelsius();
        Double measuredAvgHumidity = aggregation.getAvgHumidity();

        boolean isTempThresholdReached = measuredAvgTempCelsius > TEMPERATURE_ALARM_THRESHOLD;
        boolean isHumThresholdReached = measuredAvgHumidity > HUMIDITY_ALARM_THRESHOLD;

        if (isHumThresholdReached) {
            log.warn("Device #{}: Humidity [{}] is way above than normal [{}]", id, measuredAvgHumidity, HUMIDITY_ALARM_THRESHOLD);
        }
        if (isTempThresholdReached) {
            log.warn("Device #{}: Temperature [{}] is way above than normal [{}]", id, measuredAvgTempCelsius, TEMPERATURE_ALARM_THRESHOLD);
        }

        return isTempThresholdReached || isHumThresholdReached;
    }

}
