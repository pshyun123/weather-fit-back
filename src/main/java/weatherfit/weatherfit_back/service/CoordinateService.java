package weatherfit.weatherfit_back.service;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import lombok.RequiredArgsConstructor;
import java.util.List;
import weatherfit.weatherfit_back.entity.Coordinate;
import weatherfit.weatherfit_back.repository.CoordinateRepository;
import weatherfit.weatherfit_back.entity.User;
import weatherfit.weatherfit_back.repository.LikeRepository;
import weatherfit.weatherfit_back.repository.UserRepository;
import java.util.stream.Collectors;
import weatherfit.weatherfit_back.dto.CoordinateDTO;

@Service
@RequiredArgsConstructor
@Transactional

public class CoordinateService {
    private final CoordinateRepository coordinateRepository;
    private final UserRepository userRepository;
    private final LikeRepository likeRepository;

    //착장 정보 조회
    public List<Coordinate> getCoordinateList() {
        return coordinateRepository.findAll();
    }

    //착장 정보 중 preference 컬럼 조회
    public List<String> getPreferenceList() {
        List<Coordinate> coordinates = coordinateRepository.findAll();
        List<String> preferences = coordinates.stream()
            .map(coordinate -> {
                String preference = coordinate.getPreference();
                System.out.println("원본 preference 값: " + preference);
                if (preference != null) {
                    // UTF-8로 인코딩된 바이트 배열 출력
                    byte[] bytes = preference.getBytes(java.nio.charset.StandardCharsets.UTF_8);
                    System.out.println("UTF-8 바이트 배열: " + java.util.Arrays.toString(bytes));
                }
                return preference;
            })
            .collect(Collectors.toList());
        return preferences;
    }

    //착장 정보 중 preference 컬럼 중 미니멀의 coordinateImg 조회.    
    public List<CoordinateDTO> getMinimalPreferenceList() {
        return coordinateRepository.findAll().stream()
            .filter(coordinate -> "minimal".equals(coordinate.getPreference()))
            .map(CoordinateDTO::of)
            .collect(Collectors.toList());
    }
    

    //착장 정보 중 preference 컬럼 중 모던의 coordinateImg 조회.      
    public List<CoordinateDTO> getModernPreferenceList() {
        return coordinateRepository.findAll().stream()
            .filter(coordinate -> "modern".equals(coordinate.getPreference()))
            .map(CoordinateDTO::of)
            .collect(Collectors.toList());
    }

    //착장 정보 중 preference 컬럼 중 캐주얼의 coordinateImg 조회.       
    public List<CoordinateDTO> getCasualPreferenceList() {
        return coordinateRepository.findAll().stream()
            .filter(coordinate -> "casual".equals(coordinate.getPreference()))
            .map(CoordinateDTO::of)
            .collect(Collectors.toList());
    }
    

    //착장 정보 중 preference 컬럼 중 스트릿의 coordinateImg 조회.       
    public List<CoordinateDTO> getStreetPreferenceList() {
        return coordinateRepository.findAll().stream()
            .filter(coordinate -> "street".equals(coordinate.getPreference()))
            .map(CoordinateDTO::of)
            .collect(Collectors.toList());
    }

    //착장 정보 중 preference 컬럼 중 러블리의 coordinateImg 조회.       
    public List<CoordinateDTO> getLivelyPreferenceList() {
        return coordinateRepository.findAll().stream()
            .filter(coordinate -> "lovely".equals(coordinate.getPreference()))
            .map(CoordinateDTO::of)
            .collect(Collectors.toList());
    }

    //착장 정보 중 preference 컬럼 중 럭셔리의 coordinateImg 조회.       
    public List<CoordinateDTO> getLuxuryPreferenceList() {
        return coordinateRepository.findAll().stream()
            .filter(coordinate -> "luxury".equals(coordinate.getPreference()))
            .map(CoordinateDTO::of)
            .collect(Collectors.toList());
    }

    //착장 좋아요 여부 확인
    public boolean isLikedByUser(Long userId, Long coordinateId) {
        User user = userRepository.findById(userId)
            .orElseThrow(() -> new RuntimeException("사용자를 찾을 수 없습니다."));
        Coordinate coordinate = coordinateRepository.findById(coordinateId)
            .orElseThrow(() -> new RuntimeException("착장을 찾을 수 없습니다."));
            
        return likeRepository.existsByUserAndCoordinate(user, coordinate);
    }


    

}

