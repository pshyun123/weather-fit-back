package weatherfit.weatherfit_back.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import weatherfit.weatherfit_back.entity.Weather;
import weatherfit.weatherfit_back.repository.WeatherRepository;
import weatherfit.weatherfit_back.dto.WeatherDTO;
import weatherfit.weatherfit_back.entity.WeatherForecast;
import weatherfit.weatherfit_back.repository.WeatherForecastRepository;
import weatherfit.weatherfit_back.dto.WeatherForecastDTO;
import weatherfit.weatherfit_back.dto.CoordinateDTO;
import weatherfit.weatherfit_back.repository.CoordinateRepository;
import weatherfit.weatherfit_back.repository.LikeRepository;
import weatherfit.weatherfit_back.entity.Like;
import weatherfit.weatherfit_back.repository.UserRepository;
import weatherfit.weatherfit_back.entity.Coordinate;
import weatherfit.weatherfit_back.entity.User;

import java.util.List;
import java.util.stream.Collectors;
import java.util.Optional;
import java.util.Collections;

@Service
@RequiredArgsConstructor
@Transactional
@Slf4j
public class WeatherService {
    private final WeatherRepository weatherRepository;
    private final WeatherForecastRepository weatherForecastRepository;
    private final CoordinateRepository coordinateRepository;
    private final LikeRepository likeRepository;
    private final UserRepository userRepository;

    public WeatherDTO getCurrentWeather() {
        try {
            // 가장 최근의 날씨 데이터 조회
            Weather weather = weatherRepository.findLatestWeather()
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

    // 날씨 예보 조회 1시간 단위로 조회
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

    public List<CoordinateDTO> getWeatherBasedStyles(String weatherCondition) {
        log.info("날씨 기반 스타일 추천 서비스 호출: {}", weatherCondition);
        return coordinateRepository.findByWeatherCondition(weatherCondition)
                .stream()
                .map(CoordinateDTO::from)
                .collect(Collectors.toList());
    }

    // *현재 weather_condition과 일치하는 스타일 목록 중 현재 로그인한 사용자의 preferences값이 존재하는 값들만을 랜덤하게 조회하는 API
    
    public List<CoordinateDTO> getCurrentWeatherBasedStyles(String email) {
        WeatherDTO currentWeather = getCurrentWeather();
        String currentWeatherCondition = currentWeather.getWeatherCondition().name();
        log.info("현재 날씨 기반 스타일 추천 서비스 호출: {}", email);
        log.info("현재 날씨 정보: {}", currentWeather);

        // 현재 날씨 조건과 정확히 일치하는 스타일 조회
        List<Coordinate> styles = coordinateRepository.findByWeatherCondition(currentWeatherCondition);
        log.info("현재 날씨 조건({})과 일치하는 스타일 수: {}", currentWeatherCondition, styles.size());

        if (styles.isEmpty()) {
            log.info("현재 날씨 조건({})과 일치하는 스타일이 없습니다.", currentWeatherCondition);
        }

        // 스타일 리스트를 랜덤하게 섞기
        Collections.shuffle(styles);
        log.info("스타일 리스트를 랜덤하게 섞었습니다.");

        return styles.stream()
                .map(CoordinateDTO::from)
                .collect(Collectors.toList());
    }


    public List<CoordinateDTO> getWeatherBasedLikes(String weatherCondition, String email) {
        log.info("날씨 기반 좋아요 스타일 서비스 호출: {}, email: {}", weatherCondition, email);
        return coordinateRepository.findByWeatherConditionAndLikesUserId(weatherCondition, email)
                .stream()
                .map(CoordinateDTO::from)
                .collect(Collectors.toList());
    }

    public String toggleWeatherStyleLike(String weatherCondition, Long styleId, Long userId) {
        log.info("날씨 기반 스타일 좋아요 토글 서비스 호출: {}, styleId: {}, userId: {}", 
                weatherCondition, styleId, userId);
        
        Optional<Like> existingLikeOpt = likeRepository.findByUserIdAndCoordinateId(userId, styleId);
        
        if (existingLikeOpt.isPresent()) {
            likeRepository.delete(existingLikeOpt.get());
            return "좋아요가 취소되었습니다.";
        } else {
            Like newLike = new Like();
            newLike.setUser(userRepository.findById(userId).orElseThrow());
            newLike.setCoordinate(coordinateRepository.findById(styleId).orElseThrow());
            likeRepository.save(newLike);
            return "좋아요가 추가되었습니다.";
        }
    }
}
