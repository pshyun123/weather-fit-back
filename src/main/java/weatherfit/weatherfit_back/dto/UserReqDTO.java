package weatherfit.weatherfit_back.dto;

import lombok.Getter;
import lombok.Setter;
import lombok.NoArgsConstructor;
import lombok.AllArgsConstructor;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class UserReqDTO {
    private String email;
    private String password;
    private String name;
    private String ageGroup;
    private String profileImage;
}
