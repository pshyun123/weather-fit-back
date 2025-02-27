package weatherfit.weatherfit_back.dto;

import lombok.Getter;
import lombok.Setter;
import lombok.NoArgsConstructor;
import lombok.AllArgsConstructor;
import lombok.Builder;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import java.util.List;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class UserReqDTO {
    private String email;
    private String password;
    private String name;
    private String ageGroup;
    private String profileImage;
    private List<String> preferences;

    // preferences를 JSON 문자열로 변환하는 메서드 추가
    public String getPreferencesAsString() {
        try {
            ObjectMapper mapper = new ObjectMapper();
            return mapper.writeValueAsString(preferences);
        } catch (JsonProcessingException e) {
            return "[]";  // 기본값 반환
        }
    }

    // profileImage를 JSON 문자열로 변환하는 메서드 추가
    public String getProfileImageAsString() {
        try {
            ObjectMapper mapper = new ObjectMapper();
            return mapper.writeValueAsString(profileImage);
        } catch (JsonProcessingException e) {
            return "[]";  // 기본값 반환
        }
    }
}
