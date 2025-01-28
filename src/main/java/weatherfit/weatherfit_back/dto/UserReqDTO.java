package weatherfit.weatherfit_back.dto;

import lombok.Getter;
import lombok.Setter;
import weatherfit.weatherfit_back.entity.User;

@Getter
@Setter
public class UserReqDTO {
    private String email;
    private String password;
    private String name;
    private String ageGroup;
    private String profileImage;

}
