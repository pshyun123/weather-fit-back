package weatherfit.weatherfit_back.entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Builder;
import weatherfit.weatherfit_back.constant.WeatherCondition;

@Entity
@Table(name = "weather")
@Getter
@NoArgsConstructor
public class Weather {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "weather_range_id")
    private Long id;

    @Column(name = "min_temp", nullable = false)
    private int min_Temp;

    @Column(name = "max_temp", nullable = false)
    private int max_Temp;

    @Enumerated(EnumType.STRING)
    @Column(name = "weather_condition", nullable = false)
    private WeatherCondition weather_Condition;


    @Column(name = "latitude", nullable = false)
    private int latitude;

    @Column(name = "longitude", nullable = false)
    private int longitude;

    @Column(name = "weather_date", nullable = false)
    private String weather_Date;


    @Column(name = "weather_time", nullable = false)
    private String weather_Time;


    @Column(name = "current_temp", nullable = false)
    private int current_Temp;


    @Builder
    public Weather(int min_Temp, int max_Temp, WeatherCondition weather_Condition, 
                  int latitude, int longitude, String weather_Date, String weather_Time, 
                  int current_Temp) {
        this.min_Temp = min_Temp;

        this.max_Temp = max_Temp;
        this.weather_Condition = weather_Condition;
        this.latitude = latitude;
        this.longitude = longitude;
        this.weather_Date = weather_Date;
        this.weather_Time = weather_Time;
        this.current_Temp = current_Temp;

    }
}