package weatherfit.weatherfit_back.entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.AllArgsConstructor;
import lombok.Builder;
import weatherfit.weatherfit_back.constant.WeatherCondition;
import java.time.LocalDateTime;

@Entity
@Table(name = "weather")
@Getter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Weather {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "weather_range_id")
    private Long id;

    @Column(name = "min_temp", nullable = false)
    private int minTemp;

    @Column(name = "max_temp", nullable = false)
    private int maxTemp;

    @Enumerated(EnumType.STRING)
    @Column(name = "weather_condition", nullable = false)
    private WeatherCondition weatherCondition;

    @Column(name = "latitude", nullable = false)
    private int latitude;

    @Column(name = "longitude", nullable = false)
    private int longitude;

    @Column(name = "weather_date", nullable = false)
    private LocalDateTime weatherDate;

    @Column(name = "weather_time", nullable = false)
    private String weatherTime;

    @Column(name = "current_temp", nullable = false)
    private int currentTemp;

    @Column(name = "current_humidity", nullable = false)
    private int currentHumidity;

    @Column(name = "current_wind_speed", nullable = false)
    private double currentWindSpeed;
}