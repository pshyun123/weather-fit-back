package weatherfit.weatherfit_back.dto;


import lombok.Builder;
import lombok.Getter;
import lombok.Setter;
import weatherfit.weatherfit_back.entity.User;

@Getter
@Setter
@Builder
public class UserResDTO {
    private String email;
    private String name;
    private String ageGroup;
    private String profileImage;

    public static UserResDTO of(User user) {
        return UserResDTO.builder()
                .email(user.getEmail())
                .name(user.getName())
                .ageGroup(user.getAgeGroup())
                .profileImage(user.getProfileImage())
                .build();
    }
}
