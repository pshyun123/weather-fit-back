package weatherfit.weatherfit_back.dto;

import lombok.Getter;
import lombok.Setter;


@Getter
@Setter
public class UserReqDTO {
    private String email;
    private String password;
    private String name;
    private String ageGroup;
    private String profileImage;

    public UserReqDTO(String email, String password, String name, String ageGroup, String profileImage) {
        this.email = email;
        this.password = password;
        this.name = name;
        this.ageGroup = ageGroup;
        this.profileImage = profileImage;
    }
}
