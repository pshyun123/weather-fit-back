package weatherfit.weatherfit_back.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Map;
import weatherfit.weatherfit_back.service.AuthService;
import weatherfit.weatherfit_back.dto.UserResDTO;
import weatherfit.weatherfit_back.dto.UserReqDTO;

@RestController
@RequestMapping("/auth")
@RequiredArgsConstructor
public class AuthController {
    private final AuthService authService;

    // 중복체크
    @PostMapping("/isunique")
    public ResponseEntity<Boolean> isUnique(@RequestBody Map<String, String> dataMap) {
        int type = Integer.parseInt(dataMap.get("type"));
        return ResponseEntity.ok(authService.checkUnique(type,dataMap.get("data")));
    }

    // Member 회원가입
    @PostMapping("/join")
    public ResponseEntity<UserResDTO> join(@RequestBody UserReqDTO userReqDTO) {
        return ResponseEntity.ok(authService.join(userReqDTO));
    }


}
