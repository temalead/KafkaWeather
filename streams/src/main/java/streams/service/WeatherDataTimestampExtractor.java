package streams.service;

import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.apache.kafka.streams.processor.TimestampExtractor;
import service.KafkaTimedMessage;

public class WeatherDataTimestampExtractor implements TimestampExtractor {

    @Override
    public long extract(ConsumerRecord<Object, Object> record, long partitionTime) {
        return ((KafkaTimedMessage) record.value()).getEpochMills();
    }
}
