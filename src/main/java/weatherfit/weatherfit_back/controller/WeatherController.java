package weatherfit.weatherfit_back.controller;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import weatherfit.weatherfit_back.service.WeatherService;
import weatherfit.weatherfit_back.dto.WeatherDTO;
import org.springframework.http.ResponseEntity;
import weatherfit.weatherfit_back.dto.WeatherForecastDTO;
import org.springframework.web.bind.annotation.GetMapping;
import java.util.List;
@RestController
@Slf4j
@RequestMapping("/weather")
@RequiredArgsConstructor

public class WeatherController {
    private final WeatherService weatherService;

    @GetMapping("/current")
    public ResponseEntity<WeatherDTO> getCurrentWeather() {
        WeatherDTO weatherDTO = weatherService.getCurrentWeather();
        return ResponseEntity.ok(weatherDTO);
    }

    @GetMapping("/forecast")
    public ResponseEntity<List<WeatherForecastDTO>> getWeatherForecast() {
        List<WeatherForecastDTO> weatherForecastDTO = weatherService.getWeatherForecast();
        return ResponseEntity.ok(weatherForecastDTO);
    }


}
