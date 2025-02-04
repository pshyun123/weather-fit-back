package weatherfit.weatherfit_back.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import weatherfit.weatherfit_back.dto.UserReqDTO;
import weatherfit.weatherfit_back.dto.UserResDTO;
import weatherfit.weatherfit_back.entity.User;
import weatherfit.weatherfit_back.repository.UserRepository;
import weatherfit.weatherfit_back.constant.Authority;

@Service
@RequiredArgsConstructor
@Transactional
public class AuthService {
    private final UserRepository userRepository;

    // user 이메일 중복 확인
    public Boolean checkUnique(int type, String info) {
        switch (type) {
            case 0:
                return userRepository.existsByEmail(info);
            case 1:
                return !userRepository.existsByEmail(info);
            default:
                return true;
        }
    }

    // user 회원 가입
    public UserResDTO join(UserReqDTO userReqDTO) {
        if (userRepository.existsByEmail(userReqDTO.getEmail())) {
            throw new RuntimeException("이미 가입되어 있는 유저입니다.");
        }

        if (userReqDTO.getProfileImage() == null || userReqDTO.getProfileImage().trim().isEmpty()) {
            throw new RuntimeException("프로필 이미지는 필수 입력값입니다.");
        }

        User user = User.builder()
            .email(userReqDTO.getEmail())
            .password(userReqDTO.getPassword())
            .name(userReqDTO.getName())
            .ageGroup(userReqDTO.getAgeGroup())
            .profileImage(userReqDTO.getProfileImage())
            .authority(Authority.ROLE_USER)
            .isDeleted(false)
            .build();

        return UserResDTO.of(userRepository.save(user));
    }


}
