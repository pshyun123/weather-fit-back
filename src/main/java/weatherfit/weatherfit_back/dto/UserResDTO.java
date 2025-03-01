package weatherfit.weatherfit_back.dto;


import lombok.Builder;
import lombok.Getter;
import lombok.Setter;
import weatherfit.weatherfit_back.entity.User;

@Getter
@Setter
@Builder
public class UserResDTO {
    private Long id;
    private String email;
    private String password;
    private String name;
    private String ageGroup;
     private String profileImage;
    private String preferences;

    public static UserResDTO of(User user) {
        return UserResDTO.builder()
                .email(user.getEmail())
                .password(user.getPassword())
                
                .name(user.getName())
                .ageGroup(user.getAgeGroup())
                .profileImage(user.getProfileImage())
                .preferences(user.getPreferences())
                .build();
    }
}
