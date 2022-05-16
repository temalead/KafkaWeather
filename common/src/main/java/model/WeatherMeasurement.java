package model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class WeatherMeasurement {
    private Long id;
    private Integer temperatureCelsius;
    private Double humidity;


}
