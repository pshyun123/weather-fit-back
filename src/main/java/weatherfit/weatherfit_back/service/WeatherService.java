package weatherfit.weatherfit_back.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import weatherfit.weatherfit_back.entity.Weather;
import weatherfit.weatherfit_back.repository.WeatherRepository;
import weatherfit.weatherfit_back.dto.WeatherDTO;
import weatherfit.weatherfit_back.entity.WeatherForecast;
import weatherfit.weatherfit_back.repository.WeatherForecastRepository;
import weatherfit.weatherfit_back.dto.WeatherForecastDTO;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Transactional



public class WeatherService {
    private final WeatherRepository weatherRepository;
    private final WeatherForecastRepository weatherForecastRepository;

    public WeatherDTO getCurrentWeather() {
        try {
            Weather weather = weatherRepository.findById(1L)  // 현재 날씨 데이터 ID
                .orElseThrow(() -> new RuntimeException("날씨 정보를 찾을 수 없습니다."));
            // 성공 메시지 출력
            System.out.println("현재 날씨 정보를 성공적으로 조회했습니다.");
            return WeatherDTO.of(weather);
        } catch (Exception e) {
            // 실패 메시지 출력
            System.out.println("현재 날씨 정보 조회에 실패했습니다: " + e.getMessage());
            throw new RuntimeException("현재 날씨 정보를 찾을 수 없습니다.");
        }
    }

    // 날씨 예보 조회 3시간 단위로 조회
    public List<WeatherForecastDTO> getWeatherForecast() {
        try {
            List<WeatherForecast> weatherForecasts = weatherForecastRepository.findAll();
            // 성공 메시지 출력
            System.out.println("날씨 예보를 성공적으로 조회했습니다.");
            return weatherForecasts.stream()
                .map(WeatherForecastDTO::of)
                .collect(Collectors.toList());
        } catch (Exception e) {
            // 실패 메시지 출력
            System.out.println("날씨 예보 조회에 실패했습니다: " + e.getMessage());
            throw new RuntimeException("날씨 예보 정보를 찾을 수 없습니다.");
        }
    }


}
