package weatherfit.weatherfit_back.service;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import lombok.RequiredArgsConstructor;
import java.util.List;
import weatherfit.weatherfit_back.entity.Coordinate;
import weatherfit.weatherfit_back.repository.CoordinateRepository;
import weatherfit.weatherfit_back.entity.User;
import weatherfit.weatherfit_back.repository.UserRepository;

@Service
@RequiredArgsConstructor
@Transactional

public class CoordinateService {
    private final CoordinateRepository coordinateRepository;
    private final UserRepository userRepository;

    //착장 정보 조회
    public List<Coordinate> getCoordinateList() {
        return coordinateRepository.findAll();
    }

    //착장 좋아요 여부 확인
    public boolean isLikedByUser(Long userId, Long coordinateId) {
        User user = userRepository.findById(userId)
            .orElseThrow(() -> new RuntimeException("사용자를 찾을 수 없습니다."));
        return user.getLikedCoordinates().stream()
            .anyMatch(coordinate -> coordinate.getId().equals(coordinateId));
    }


    

}

