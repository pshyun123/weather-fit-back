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
    private double temp;
    private double tempMin;
    private double tempMax;
    private int humidity;
    private WeatherCondition weatherCondition;
    private String description;
    private double latitude;
    private double longitude;


    public static WeatherForecastDTO of(WeatherForecast weatherForecast) {
        return WeatherForecastDTO.builder()
                .id(weatherForecast.getId())
                .forecastDate(weatherForecast.getForecastDate())
                .forecastTime(weatherForecast.getForecastTime())
                .temp(weatherForecast.getTemp())
                .tempMin(weatherForecast.getTempMin())
                .tempMax(weatherForecast.getTempMax())
                .humidity(weatherForecast.getHumidity())
                .weatherCondition(weatherForecast.getWeatherCondition())
                .description(weatherForecast.getDescription())
                .latitude(weatherForecast.getLatitude())    
                .longitude(weatherForecast.getLongitude())
                .build();
    }
 
}
