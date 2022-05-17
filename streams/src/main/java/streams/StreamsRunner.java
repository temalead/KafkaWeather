package streams;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.kafka.annotation.EnableKafkaStreams;

@SpringBootApplication
@EnableKafkaStreams
public class StreamsRunner {
    public static void main(String[] args) {
        SpringApplication.run(StreamsRunner.class,args);
    }
}
