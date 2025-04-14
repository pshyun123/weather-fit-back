package weatherfit.weatherfit_back.dto;

import lombok.Getter;
import lombok.Setter;
import lombok.Builder;
import weatherfit.weatherfit_back.constant.WeatherCondition;
import weatherfit.weatherfit_back.entity.WeatherForecast;

@Getter
@Setter
@Builder
public class WeatherForecastDTO {
    private Long id;
    private String forecastDate;
    private String forecastTime;
    private double forecastTemp;
    private double forecastTempMin;
    private double forecastTempMax;
    private int forecastHumidity;
    private double forecastWindSpeed;
    private WeatherCondition forecastWeatherCondition;
    private String forecastDescription;
    private double forecastLatitude;
    private double forecastLongitude;

    public static WeatherForecastDTO of(WeatherForecast weatherForecast) {
        return WeatherForecastDTO.builder()
                .id(weatherForecast.getId())
                .forecastDate(weatherForecast.getForecastDate())
                .forecastTime(weatherForecast.getForecastTime())
                .forecastTemp(weatherForecast.getForecastTemp())
                .forecastTempMin(weatherForecast.getForecastTempMin())
                .forecastTempMax(weatherForecast.getForecastTempMax())
                .forecastHumidity(weatherForecast.getForecastHumidity())
                .forecastWindSpeed(weatherForecast.getForecastWindSpeed())
                .forecastWeatherCondition(weatherForecast.getForecastWeatherCondition())
                .forecastDescription(weatherForecast.getForecastDescription())
                .forecastLatitude(weatherForecast.getForecastLatitude())
                .forecastLongitude(weatherForecast.getForecastLongitude())
                .build();
    }
}
