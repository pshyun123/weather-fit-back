package weatherfit.weatherfit_back.dto;

import lombok.Getter;
import lombok.Setter;
import lombok.Builder;
import weatherfit.weatherfit_back.constant.WeatherCondition;
import weatherfit.weatherfit_back.entity.Weather;
import java.time.LocalDateTime;

@Getter
@Setter
@Builder
public class WeatherDTO {
    private Long id;
    private int minTemp;
    private int maxTemp;
    private WeatherCondition weatherCondition;
    private float latitude;
    private float longitude;
    private LocalDateTime weatherDate;
    private String weatherTime;
    private int currentTemp;
    private int currentHumidity;
    private float currentWindSpeed;
    private String locationName;

    public static WeatherDTO of(Weather weather) {
        return WeatherDTO.builder()
                .id(weather.getId())
                .minTemp(weather.getMinTemp())
                .maxTemp(weather.getMaxTemp())
                .weatherCondition(weather.getWeatherCondition())
                .latitude(weather.getLatitude())
                .longitude(weather.getLongitude())
                .weatherDate(weather.getWeatherDate())
                .weatherTime(weather.getWeatherTime())
                .currentTemp(weather.getCurrentTemp())
                .currentHumidity(weather.getCurrentHumidity())
                .currentWindSpeed(weather.getCurrentWindSpeed())
                .locationName(weather.getLocationName())
                .build();
    }
}
