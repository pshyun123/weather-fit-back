package weatherfit.weatherfit_back.dto;

import lombok.Getter;
import lombok.Setter;
import lombok.Builder;
import weatherfit.weatherfit_back.constant.WeatherCondition;
import weatherfit.weatherfit_back.entity.Weather;

@Getter
@Setter
@Builder

public class WeatherDTO {
    private Long id;
    private int min_Temp;
    private int max_Temp;
    private WeatherCondition weather_Condition;
    private int latitude;
    private int longitude;
    private String weather_Date;
    private String weather_Time;
    private int current_Temp;


    public static WeatherDTO of(Weather weather) {
        return WeatherDTO.builder()
                .id(weather.getId())
                .min_Temp(weather.getMin_Temp())
                .max_Temp(weather.getMax_Temp())
                .weather_Condition(weather.getWeather_Condition())
                .latitude(weather.getLatitude())
                .longitude(weather.getLongitude())
                .weather_Date(weather.getWeather_Date())
                .weather_Time(weather.getWeather_Time())
                .current_Temp(weather.getCurrent_Temp())
                .build();


    }



}
