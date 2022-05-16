package web;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import model.WeatherMeasurement;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@Slf4j
@RequestMapping("/api/v1/weather")
public class WeatherController {
    private final MeasurementSender sender;

    @PostMapping
    public void collectMeasurement(@RequestBody WeatherMeasurement measurement){
        log.info("Got [{}]",measurement);
        sender.send(measurement);
    }
}
