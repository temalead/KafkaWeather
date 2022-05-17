package streams.service;

import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.apache.kafka.streams.processor.TimestampExtractor;
import service.KafkaTimedMessage;

public class WeatherTimestampExtractor implements TimestampExtractor {
    @Override
    public long extract(ConsumerRecord<Object, Object> record, long partitionTime) {
        final Object value = record.value();
        if(value instanceof KafkaTimedMessage) {
            return ((KafkaTimedMessage) value).getEpochMills();
        } else {
            return System.currentTimeMillis();
        }
    }
}