package web;

import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;
import model.WeatherMeasurement;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import java.util.Random;

@Service
@Slf4j
@RequiredArgsConstructor
public class MeasurementGenerator {

    private final Random random = new Random();
    private final ObjectMapper objectMapper;
    private final String URL = "http://localhost:8080/api/v1/weather";


    @Scheduled(fixedRate = 1000)
    public void postRequest() {
        createRequest();
    }


    private WeatherMeasurement generateMeasurement() {
        int id = random.nextInt(2);
        int temperature = random.nextInt(50);
        double humidity = random.nextDouble(100);
        return new WeatherMeasurement(
                (long) id,
                temperature,
                humidity);
    }

    @SneakyThrows
    public void createRequest() {
        CloseableHttpClient client = HttpClients.createDefault();
        HttpPost httpPost = new HttpPost(URL);
        String json = objectMapper.writeValueAsString(generateMeasurement());
        StringEntity entity = new StringEntity(json);
        httpPost.setEntity(entity);
        httpPost.setHeader("Accept", "application/json");
        httpPost.setHeader("Content-type", "application/json");
        CloseableHttpResponse response = client.execute(httpPost);
        client.close();


    }
}
