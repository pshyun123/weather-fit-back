package weatherfit.weatherfit_back.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import weatherfit.weatherfit_back.entity.User;
import weatherfit.weatherfit_back.entity.Coordinate;
import weatherfit.weatherfit_back.entity.Like;
import weatherfit.weatherfit_back.repository.UserRepository;
import weatherfit.weatherfit_back.repository.CoordinateRepository;
import weatherfit.weatherfit_back.repository.LikeRepository;
import weatherfit.weatherfit_back.dto.UserResDTO;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.crypto.password.PasswordEncoder;
import weatherfit.weatherfit_back.dto.LikeDTO;

import java.util.List;
import java.util.stream.Collectors;


@Service
@RequiredArgsConstructor
@Transactional
@Slf4j
public class UserService {
    private final UserRepository userRepository;
    private final CoordinateRepository coordinateRepository;
    private final LikeRepository likeRepository;
    private final PasswordEncoder passwordEncoder;


        //회원 리스트 불러오기
        public List<UserResDTO> getUserList() {
            List<User> users = userRepository.findAll();
            return users.stream()
                .map(UserResDTO::of)
                .collect(Collectors.toList());
        }

    //회원 비밀번호 수정.
    public void updatePassword(String email, String newPassword) {
        User user = userRepository.findByEmail(email)
            .orElseThrow(() -> new RuntimeException("사용자를 찾을 수 없습니다."));
        
        // 비밀번호 암호화
        String encryptedPassword = passwordEncoder.encode(newPassword);
        user.setPassword(encryptedPassword);

        log.info("비밀번호 수정 결과: 암호화된 비밀번호로 저장됨");
        userRepository.save(user);
    }

    //암호화되어 있는 회원 비밀번호 체크.
    public boolean verifyPassword(String email, String password) {
        User user = userRepository.findByEmail(email)
            .orElseThrow(() -> new RuntimeException("사용자를 찾을 수 없습니다."));

        // 암호화된 비밀번호와 입력된 평문 비밀번호를 matches() 메서드로 비교
        return passwordEncoder.matches(password, user.getPassword());
    }
    

    //회원 프로필 이미지 수정
    public void updateProfileImage(String email, String newProfileImage) {
        User user = userRepository.findByEmail(email)
            .orElseThrow(() -> new RuntimeException("사용자를 찾을 수 없습니다."));
        user.setProfileImage(newProfileImage);
        userRepository.save(user);
    }

    //회원 취향 수정
    public void updatePreferences(String email, String newPreferences) {
        User user = userRepository.findByEmail(email)
            .orElseThrow(() -> new RuntimeException("사용자를 찾을 수 없습니다."));
        user.setPreferences(newPreferences);
        userRepository.save(user);
    }

    //회원 나이대 수정
    public void updateAgeGroup(String email, String newAgeGroup) {
        try {
            log.info("연령대 업데이트 시작: email={}, newAgeGroup={}", email, newAgeGroup);
            
            if (email == null || email.isEmpty()) {
                throw new IllegalArgumentException("이메일이 비어있습니다.");
            }
            
            if (newAgeGroup == null || newAgeGroup.isEmpty()) {
                throw new IllegalArgumentException("연령대가 비어있습니다.");
            }
            
            User user = userRepository.findByEmail(email)
                .orElseThrow(() -> new RuntimeException("사용자를 찾을 수 없습니다: " + email));
            
            // 현재 연령대와 동일한 경우 업데이트 불필요
            if (newAgeGroup.equals(user.getAgeGroup())) {
                log.info("현재 연령대와 동일하여 업데이트 불필요: email={}, ageGroup={}", email, newAgeGroup);
                return;
            }
            
            log.info("연령대 업데이트: email={}, 기존 연령대={}, 새 연령대={}", 
                    email, user.getAgeGroup(), newAgeGroup);
            
            user.setAgeGroup(newAgeGroup);
            userRepository.save(user);
            
            log.info("연령대 업데이트 완료: email={}, newAgeGroup={}", email, newAgeGroup);
        } catch (Exception e) {
            log.error("연령대 업데이트 중 오류 발생: {}", e.getMessage(), e);
            throw e;
        }
    }

    //회원 탈퇴
    public String deleteUser(String email) {
        User user = userRepository.findByEmail(email)
            .orElseThrow(() -> new RuntimeException("사용자를 찾을 수 없습니다."));
        userRepository.delete(user);
    
        return "정상적으로 탈퇴되었습니다.";
    }

    //비밀번호 조회
    public String getPassword(String email) {
        User user = userRepository.findByEmail(email)
            .orElseThrow(() -> new RuntimeException("해당 계정이 존재하지 않습니다."));
        return user.getPassword();
    }

    //좋아요 추가
    public void addLike(Long userId, Long coordinateId) {
        try {
            log.info("좋아요 추가 시작: userId={}, coordinateId={}", userId, coordinateId);
            
            if (userId == null) {
                throw new IllegalArgumentException("사용자 ID가 비어있습니다.");
            }
            
            if (coordinateId == null) {
                throw new IllegalArgumentException("착장 ID가 비어있습니다.");
            }
            
            User user = userRepository.findById(userId)
                .orElseThrow(() -> new RuntimeException("사용자를 찾을 수 없습니다: " + userId));
            Coordinate coordinate = coordinateRepository.findById(coordinateId)
                .orElseThrow(() -> new RuntimeException("해당 착장 정보를 찾을 수 없습니다: " + coordinateId));
            
            // 이미 좋아요가 있는지 확인
            if (likeRepository.existsByUserAndCoordinate(user, coordinate)) {
                log.info("이미 좋아요한 착장입니다: userId={}, coordinateId={}", userId, coordinateId);
                throw new RuntimeException("이미 좋아요한 착장입니다.");
            }

            Like like = Like.builder()
                .user(user)
                .coordinate(coordinate)
                .build();
            
            likeRepository.save(like);
            log.info("좋아요 추가 완료: userId={}, coordinateId={}", userId, coordinateId);
        } catch (Exception e) {
            log.error("좋아요 추가 중 오류 발생: {}", e.getMessage(), e);
            throw e;
        }
    }

    //좋아요 삭제
    public void deleteLike(Long userId, Long coordinateId) {
        try {
            log.info("좋아요 삭제 시작: userId={}, coordinateId={}", userId, coordinateId);
            
            if (userId == null) {
                throw new IllegalArgumentException("사용자 ID가 비어있습니다.");
            }
            
            if (coordinateId == null) {
                throw new IllegalArgumentException("착장 ID가 비어있습니다.");
            }
            
            User user = userRepository.findById(userId)
                .orElseThrow(() -> new RuntimeException("사용자를 찾을 수 없습니다: " + userId));
            Coordinate coordinate = coordinateRepository.findById(coordinateId)
                .orElseThrow(() -> new RuntimeException("해당 착장 정보를 찾을 수 없습니다: " + coordinateId));
            
            Like like = likeRepository.findByUserAndCoordinate(user, coordinate)
                .orElseThrow(() -> new RuntimeException("좋아요를 찾을 수 없습니다."));
            
            likeRepository.delete(like);
            log.info("좋아요 삭제 완료: userId={}, coordinateId={}", userId, coordinateId);
        } catch (Exception e) {
            log.error("좋아요 삭제 중 오류 발생: {}", e.getMessage(), e);
            throw e;
        }
    }

    //여기서부터 좋아요+취향조건.

    //좋아요 한 것들 중 preferences가 미니멀인 것들 조회
    public List<LikeDTO> getMinimalLikeList(Long userId) {
        List<LikeDTO> likeList = likeRepository.findByUserId(userId)
            .stream()
            .filter(like -> like.getCoordinate().getPreference().equals("minimal"))
            .map(LikeDTO::of)
            .collect(Collectors.toList());
        return likeList;
    }

    //좋아요 한 것들 중 preferences가 모던인 것들 조회
    public List<LikeDTO> getModernLikeList(Long userId) {
        List<LikeDTO> likeList = likeRepository.findByUserId(userId)
            .stream()
            .filter(like -> like.getCoordinate().getPreference().equals("modern"))
            .map(LikeDTO::of)
            .collect(Collectors.toList());
        return likeList;
    }

    //좋아요 한 것들 중 preferences가 캐주얼인 것들 조회
    public List<LikeDTO> getCasualLikeList(Long userId) {
        List<LikeDTO> likeList = likeRepository.findByUserId(userId)
            .stream()
            .filter(like -> like.getCoordinate().getPreference().equals("casual"))
            .map(LikeDTO::of)
            .collect(Collectors.toList());
        return likeList;
    }

    //좋아요 한 것들 중 preferences가 스트릿인 것들 조회
    public List<LikeDTO> getStreetLikeList(Long userId) {
        List<LikeDTO> likeList = likeRepository.findByUserId(userId)
            .stream()
            .filter(like -> like.getCoordinate().getPreference().equals("street"))
            .map(LikeDTO::of)
            .collect(Collectors.toList());
        return likeList;
    }

    //좋아요 한 것들 중 preferences가 러블리인 것들 조회
    public List<LikeDTO> getRomanticLikeList(Long userId) {
        List<LikeDTO> likeList = likeRepository.findByUserId(userId)
            .stream()
            .filter(like -> like.getCoordinate().getPreference().equals("lovely"))
            .map(LikeDTO::of)
            .collect(Collectors.toList());
        return likeList;    
    }

    //좋아요 한 것들 중 preferences가 럭셔리인 것들 조회
    public List<LikeDTO> getLuxuryLikeList(Long userId) {
        List<LikeDTO> likeList = likeRepository.findByUserId(userId)
            .stream()
            .filter(like -> like.getCoordinate().getPreference().equals("luxury"))
            .map(LikeDTO::of)
            .collect(Collectors.toList());
        return likeList;
    }

//////////////여기서부터 날씨+좋아요.

    //좋아요 한 것들 중 날씨 조건이 더움인 것들 조회
    public List<LikeDTO> getHotLikeList(Long userId) {
        List<LikeDTO> likeList = likeRepository.findByUserId(userId)
            .stream()
            .filter(like -> like.getCoordinate().getWeatherCondition().equals("hot"))
            .map(LikeDTO::of)
            .collect(Collectors.toList());
        return likeList;
    }

    //좋아요 한 것들 중 날씨 조건이 따뜻한 것들 조회
    public List<LikeDTO> getWarmLikeList(Long userId) {
        List<LikeDTO> likeList = likeRepository.findByUserId(userId)
            .stream()
            .filter(like -> like.getCoordinate().getWeatherCondition().equals("warm"))  
            .map(LikeDTO::of)
            .collect(Collectors.toList());
        return likeList;
    }

    //좋아요 한 것들 중 날씨 조건이 비인 것들 조회
    public List<LikeDTO> getRainLikeList(Long userId) {
        List<LikeDTO> likeList = likeRepository.findByUserId(userId)
            .stream()
            .filter(like -> like.getCoordinate().getWeatherCondition().equals("rain"))
            .map(LikeDTO::of)
            .collect(Collectors.toList());
        return likeList;
    }

    //좋아요 한 것들 중 날씨 조건이 추움인 것들 조회
    public List<LikeDTO> getColdLikeList(Long userId) {
        List<LikeDTO> likeList = likeRepository.findByUserId(userId)
            .stream()
            .filter(like -> like.getCoordinate().getWeatherCondition().equals("cold"))  
            .map(LikeDTO::of)
            .collect(Collectors.toList());
        return likeList;
    }

    //좋아요 한 것들 중 날씨 조건이 매우 추움인 것들 조회   
    public List<LikeDTO> getChillLikeList(Long userId) {
        List<LikeDTO> likeList = likeRepository.findByUserId(userId)
            .stream()
            .filter(like -> like.getCoordinate().getWeatherCondition().equals("chill"))
            .map(LikeDTO::of)
            .collect(Collectors.toList());
        return likeList;
    }

    //좋아요 한 것들 중 날씨 조건이 눈인 것들 조회
    public List<LikeDTO> getSnowLikeList(Long userId) {
        List<LikeDTO> likeList = likeRepository.findByUserId(userId)
            .stream()
            .filter(like -> like.getCoordinate().getWeatherCondition().equals("snow"))
            .map(LikeDTO::of)
            .collect(Collectors.toList());
        return likeList;
    }



    ///////////////// 여기서 날씨 + TPo
    /// 

    //좋아요 한 것들 중 TPO가 데이트인 것들 조회
    public List<LikeDTO> getDateLikeList(Long userId) {
        List<LikeDTO> likeList = likeRepository.findByUserId(userId)
            .stream()
            .filter(like -> like.getCoordinate().getTpo().equals("date"))
            .map(LikeDTO::of)
            .collect(Collectors.toList());
        return likeList;
    }

    //좋아요 한 것들 중 TPO가 출근인 것들 조회
    public List<LikeDTO> getWorkLikeList(Long userId) {
        List<LikeDTO> likeList = likeRepository.findByUserId(userId)
            .stream()
            .filter(like -> like.getCoordinate().getTpo().equals("work"))
            .map(LikeDTO::of)
            .collect(Collectors.toList());
        return likeList;
    }

    //좋아요 한 것들 중 TPO가 여행인 것들 조회
    public List<LikeDTO> getTravelLikeList(Long userId) {
        List<LikeDTO> likeList = likeRepository.findByUserId(userId)
            .stream()
            .filter(like -> like.getCoordinate().getTpo().equals("travel"))
            .map(LikeDTO::of)
            .collect(Collectors.toList());
        return likeList;
    }

    //좋아요 한 것들 중 TPO가 운동인 것들 조회
    public List<LikeDTO> getExerciseLikeList(Long userId) {
        List<LikeDTO> likeList = likeRepository.findByUserId(userId)
            .stream()
            .filter(like -> like.getCoordinate().getTpo().equals("exercise"))
            .map(LikeDTO::of)
            .collect(Collectors.toList());
        return likeList;
    }

    //좋아요 한 것들 중 TPO가 모임인 것들 조회
    public List<LikeDTO> getMeetingLikeList(Long userId) {
        List<LikeDTO> likeList = likeRepository.findByUserId(userId)
            .stream()
            .filter(like -> like.getCoordinate().getTpo().equals("meeting"))
            .map(LikeDTO::of)
            .collect(Collectors.toList());
        return likeList;
    }

    //좋아요 한 것들 중 TPO가 일상인 것들 조회
    public List<LikeDTO> getDailyLikeList(Long userId) {    
        List<LikeDTO> likeList = likeRepository.findByUserId(userId)
            .stream()
            .filter(like -> like.getCoordinate().getTpo().equals("daily"))
            .map(LikeDTO::of)
            .collect(Collectors.toList());
        return likeList;
    }


}
