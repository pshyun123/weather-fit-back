package weatherfit.weatherfit_back.controller;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;
import weatherfit.weatherfit_back.service.WeatherService;
import weatherfit.weatherfit_back.dto.WeatherDTO;
import org.springframework.http.ResponseEntity;
import weatherfit.weatherfit_back.dto.WeatherForecastDTO;
import weatherfit.weatherfit_back.dto.CoordinateDTO;
import java.util.List;

/**
 * 날씨 관련 API를 처리하는 컨트롤러
 * 날씨 정보 조회 및 날씨 기반 스타일 추천 기능을 제공
 */
@RestController
@Slf4j
@RequestMapping("/weather")
@RequiredArgsConstructor
public class WeatherController {
    private final WeatherService weatherService;

    /**
     * 현재 날씨 정보를 조회하는 API
     * GET /weather/current
     * 
     * @return 현재 날씨 정보 (기온, 날씨 상태, 습도 등)
     */
    @GetMapping("/current")
    public ResponseEntity<WeatherDTO> getCurrentWeather() {
        log.info("현재 날씨 조회 요청");
        WeatherDTO weather = weatherService.getCurrentWeather();
        return ResponseEntity.ok(weather);
    }

    /**
     * 날씨 예보 정보를 조회하는 API
     * GET /weather/forecast
     * 
     * @return 향후 날씨 예보 정보 목록
     */
    @GetMapping("/forecast")
    public ResponseEntity<List<WeatherForecastDTO>> getWeatherForecast() {
        log.info("날씨 예보 조회 요청");
        List<WeatherForecastDTO> forecast = weatherService.getWeatherForecast();
        return ResponseEntity.ok(forecast);
    }

    /**
     * 특정 날씨 조건에 맞는 스타일을 추천하는 API
     * GET /weather/styles/{weatherCondition}
     * 
     * @param weatherCondition 날씨 조건 (맑음, 비, 눈 등)
     * @return 해당 날씨 조건에 맞는 스타일 목록
     */
    @GetMapping("/styles/{weatherCondition}")
    public ResponseEntity<List<CoordinateDTO>> getWeatherBasedStyles(
            @PathVariable String weatherCondition) {
        log.info("날씨 기반 스타일 추천 요청: {}", weatherCondition);
        List<CoordinateDTO> styles = weatherService.getWeatherBasedStyles(weatherCondition);
        return ResponseEntity.ok(styles);
    }

    /**
     * 특정 날씨 조건에서 사용자가 좋아요한 스타일을 조회하는 API
     * GET /weather/styles/{weatherCondition}/likes
     * 
     * @param weatherCondition 날씨 조건 (맑음, 비, 눈 등)
     * @param userId 사용자 ID
     * @return 해당 날씨 조건에서 사용자가 좋아요한 스타일 목록
     */
    @GetMapping("/styles/{weatherCondition}/likes")
    public ResponseEntity<List<CoordinateDTO>> getWeatherBasedLikes(
            @PathVariable String weatherCondition,
            @RequestParam Long userId) {
        log.info("날씨 기반 좋아요 스타일 요청: {}, userId: {}", weatherCondition, userId);
        List<CoordinateDTO> likes = weatherService.getWeatherBasedLikes(weatherCondition, userId);
        return ResponseEntity.ok(likes);
    }

    /**
     * 스타일 좋아요를 토글(추가/삭제)하는 API
     * POST /weather/styles/{weatherCondition}/{styleId}/like
     * 
     * @param weatherCondition 날씨 조건 (맑음, 비, 눈 등)
     * @param styleId 스타일 ID
     * @param userId 사용자 ID
     * @return 좋아요 토글 결과 메시지
     */
    @PostMapping("/styles/{weatherCondition}/{styleId}/like")
    public ResponseEntity<String> toggleWeatherStyleLike(
            @PathVariable String weatherCondition,
            @PathVariable Long styleId,
            @RequestParam Long userId) {
        log.info("날씨 기반 스타일 좋아요 토글 요청: {}, styleId: {}, userId: {}", 
                weatherCondition, styleId, userId);
        String result = weatherService.toggleWeatherStyleLike(weatherCondition, styleId, userId);
        return ResponseEntity.ok(result);
    }
}
