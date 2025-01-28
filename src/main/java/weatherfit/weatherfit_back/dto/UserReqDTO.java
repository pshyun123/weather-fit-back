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

}
