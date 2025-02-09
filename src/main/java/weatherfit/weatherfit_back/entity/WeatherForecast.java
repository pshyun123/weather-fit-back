package weatherfit.weatherfit_back.entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Builder;
import weatherfit.weatherfit_back.constant.WeatherCondition;

@Entity
@Table(name = "weather_forecast")
@Getter
@NoArgsConstructor
public class WeatherForecast {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "forecast_date", nullable = false)
    private String forecastDate;

    @Column(name = "forecast_time", nullable = false)
    private String forecastTime;

    @Column(name = "temp", nullable = false)
    private double temp;

    @Column(name = "temp_min", nullable = false)
    private double tempMin;

    @Column(name = "temp_max", nullable = false)
    private double tempMax;

    @Column(name = "humidity", nullable = false)
    private int humidity;

    @Enumerated(EnumType.STRING)
    @Column(name = "weather_condition", nullable = false)
    private WeatherCondition weatherCondition;

    @Column(name = "description", nullable = false)
    private String description;

    @Column(name = "latitude", nullable = false)
    private double latitude;

    @Column(name = "longitude", nullable = false)
    private double longitude;

    @Builder
    public WeatherForecast(String forecastDate, String forecastTime, 
                          double temp, double tempMin, double tempMax,
                          int humidity, WeatherCondition weatherCondition,
                          String description, double latitude, double longitude) {
        this.forecastDate = forecastDate;
        this.forecastTime = forecastTime;
        this.temp = temp;
        this.tempMin = tempMin;
        this.tempMax = tempMax;
        this.humidity = humidity;
        this.weatherCondition = weatherCondition;
        this.description = description;
        this.latitude = latitude;
        this.longitude = longitude;
    }
} 