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


import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Transactional
public class UserService {
    private final UserRepository userRepository;
    private final CoordinateRepository coordinateRepository;
    private final LikeRepository likeRepository;


    //회원 비밀번호 수정.
    public void updatePassword(String email, String newPassword) {
        User user = userRepository.findByEmail(email)
            .orElseThrow(() -> new RuntimeException("사용자를 찾을 수 없습니다."));
        user.setPassword(newPassword);
        userRepository.save(user);
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
        User user = userRepository.findByEmail(email)
            .orElseThrow(() -> new RuntimeException("사용자를 찾을 수 없습니다."));
        user.setAgeGroup(newAgeGroup);
        userRepository.save(user);
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
        User user = userRepository.findById(userId)
            .orElseThrow(() -> new RuntimeException("사용자를 찾을 수 없습니다."));
        Coordinate coordinate = coordinateRepository.findById(coordinateId)
            .orElseThrow(() -> new RuntimeException("해당 착장 정보를 찾을 수 없습니다."));
        
        // 이미 좋아요가 있는지 확인
        if (likeRepository.existsByUserAndCoordinate(user, coordinate)) {
            throw new RuntimeException("이미 좋아요한 착장입니다.");
        }

        Like like = Like.builder()
            .user(user)
            .coordinate(coordinate)
            .build();
        
        likeRepository.save(like);
    }

    //좋아요 삭제
    public void deleteLike(Long userId, Long coordinateId) {
        User user = userRepository.findById(userId)
            .orElseThrow(() -> new RuntimeException("사용자를 찾을 수 없습니다."));
        Coordinate coordinate = coordinateRepository.findById(coordinateId)
            .orElseThrow(() -> new RuntimeException("해당 착장 정보를 찾을 수 없습니다."));
        
        Like like = likeRepository.findByUserAndCoordinate(user, coordinate)
            .orElseThrow(() -> new RuntimeException("좋아요를 찾을 수 없습니다."));
        
        likeRepository.delete(like);
    }

    //회원 리스트 불러오기
    public List<UserResDTO> getUserList() {
        List<User> users = userRepository.findAll();
        return users.stream()
            .map(UserResDTO::of)
            .collect(Collectors.toList());
    }
}
