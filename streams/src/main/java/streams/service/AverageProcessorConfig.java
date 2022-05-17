package streams.service;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import model.WeatherMeasurement;
import model.WeatherMeasurementAggregation;
import org.apache.kafka.common.serialization.Serdes;
import org.apache.kafka.streams.KeyValue;
import org.apache.kafka.streams.kstream.KStream;
import org.apache.kafka.streams.kstream.Materialized;
import org.apache.kafka.streams.kstream.Suppressed;
import org.apache.kafka.streams.kstream.TimeWindows;
import org.apache.kafka.streams.kstream.Windowed;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.kafka.annotation.EnableKafka;
import org.springframework.kafka.annotation.EnableKafkaStreams;
import org.springframework.kafka.support.serializer.JsonSerde;

import java.time.Duration;
import java.util.function.Function;

@Configuration
@Slf4j
public class AverageProcessorConfig {

    @Bean
    public Function<KStream<Long, WeatherMeasurement>, KStream<Long, WeatherMeasurementAggregation>> averageProcessor() {
        return input -> input
                .groupByKey()
                .windowedBy(TimeWindows.ofSizeWithNoGrace(Duration.ofSeconds(5)))
                .aggregate(this::init, this::agg, Materialized.with(Serdes.Long(), new JsonSerde<>(IntermediateAggregationState.class)))
                .suppress(Suppressed.untilWindowCloses(Suppressed.BufferConfig.unbounded()))
                .toStream()
                .map(this::calcAvg);
    }

    private KeyValue<Long, WeatherMeasurementAggregation> calcAvg(Windowed<Long> window, IntermediateAggregationState aggregationState) {
        Double avgHum = aggregationState.getHumCount() / aggregationState.getTempCount();
        Double avgTemp = aggregationState.getTempSum() / aggregationState.getTempCount();

        WeatherMeasurementAggregation statistics = new WeatherMeasurementAggregation(window.key(),
                window.window().endTime(),
                avgTemp,
                avgHum);

        log.info("Got aggregation result: [{}]", statistics);

        return KeyValue.pair(window.key(), statistics);

    }

    private IntermediateAggregationState agg(Long id,
                                             WeatherMeasurement weatherMeasurement,
                                             IntermediateAggregationState aggregation) {
        log.debug("Aggregation incoming messages [{}]", weatherMeasurement);
        Integer tempCount = aggregation.getTempCount();
        Double humCount = aggregation.getHumCount();
        Double humSum = aggregation.getHumSum();
        Double tempSum = aggregation.getTempSum();
        tempCount++;
        tempSum += weatherMeasurement.getTemperatureCelsius().doubleValue();
        humCount++;
        humSum += weatherMeasurement.getHumidity();

        return IntermediateAggregationState.builder()
                .humCount(humCount)
                .humSum(humSum)
                .tempCount(tempCount)
                .tempSum(tempSum).build();
    }

    private IntermediateAggregationState init() {
        return new IntermediateAggregationState(0, 0d, 0d, 0d);
    }

    @Data
    @AllArgsConstructor
    @NoArgsConstructor
    @Builder
    private static class IntermediateAggregationState {
        private Integer tempCount;
        private Double tempSum;
        private Double humCount;
        private Double humSum;
    }
}
