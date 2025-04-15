package weatherfit.weatherfit_back.entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Builder;
import weatherfit.weatherfit_back.constant.WeatherCondition;

/**
 * 날씨 예보 정보를 저장하는 엔티티
 * OpenWeatherMap API의 3시간 단위 예보 데이터를 저장
 */
@Entity
@Table(name = "weather_forecast")
@Getter
@NoArgsConstructor
public class WeatherForecast {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "forecast_date", nullable = false)
    private String forecastDate;  // YYYY-MM-DD 형식

    @Column(name = "forecast_time", nullable = false)
    private String forecastTime;  // HH:MM:SS 형식

    @Column(name = "forecast_temp", nullable = false)
    private double forecastTemp;  // 현재 기온

    @Column(name = "forecast_temp_min", nullable = false)
    private double forecastTempMin;  // 최저 기온

    @Column(name = "forecast_temp_max", nullable = false)
    private double forecastTempMax;  // 최고 기온

    @Column(name = "forecast_humidity", nullable = false)
    private int forecastHumidity;  // 습도 (%)

    @Column(name = "forecast_wind_speed", nullable = false)
    private double forecastWindSpeed;  // 풍속 (m/s)

    @Enumerated(EnumType.STRING)
    @Column(name = "forecast_weather_condition", nullable = false)
    private WeatherCondition forecastWeatherCondition;  // 날씨 상태

    @Column(name = "forecast_description", nullable = false)
    private String forecastDescription;  // 날씨 설명

    @Column(name = "forecast_latitude", nullable = false)
    private float forecastLatitude;  // 위도

    @Column(name = "forecast_longitude", nullable = false)
    private float forecastLongitude;  // 경도

    @Column(name = "forecast_location_name", nullable = false)
    private String forecastLocationName;  // 위치 이름

    @Builder
    public WeatherForecast(String forecastDate, String forecastTime, 
                          double forecastTemp, double forecastTempMin, double forecastTempMax,
                          int forecastHumidity, double forecastWindSpeed,
                          WeatherCondition forecastWeatherCondition, String forecastDescription,
                          float forecastLatitude, float forecastLongitude, String forecastLocationName) {
        this.forecastDate = forecastDate;
        this.forecastTime = forecastTime;
        this.forecastTemp = forecastTemp;
        this.forecastTempMin = forecastTempMin;
        this.forecastTempMax = forecastTempMax;
        this.forecastHumidity = forecastHumidity;
        this.forecastWindSpeed = forecastWindSpeed;
        this.forecastWeatherCondition = forecastWeatherCondition;
        this.forecastDescription = forecastDescription;
        this.forecastLatitude = forecastLatitude;
        this.forecastLongitude = forecastLongitude;
        this.forecastLocationName = forecastLocationName;
    }
} 