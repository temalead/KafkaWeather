package web;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.scheduling.annotation.EnableScheduling;

@SpringBootApplication
@EnableScheduling
public class WebRunner {
    public static void main(String[] args) {
        SpringApplication.run(WebRunner.class,args);
    }
}
