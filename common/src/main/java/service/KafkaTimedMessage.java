package service;

@FunctionalInterface
public interface KafkaTimedMessage {
    Long getEpochMills();
}
