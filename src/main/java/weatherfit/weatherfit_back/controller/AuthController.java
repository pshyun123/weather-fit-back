package weatherfit.weatherfit_back.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Map;

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
    public ResponseEntity<UserResDto> join(@RequestBody UserReqDto userReqDto) {
        return ResponseEntity.ok(authService.join(userReqDto));
    }


}
