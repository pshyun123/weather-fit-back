package weatherfit.weatherfit_back.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Map;
import java.util.Random;


import weatherfit.weatherfit_back.service.AuthService;
import weatherfit.weatherfit_back.dto.UserResDTO;
import weatherfit.weatherfit_back.dto.UserReqDTO;
import weatherfit.weatherfit_back.service.EmailService;
@RestController
@RequestMapping("/auth")
@RequiredArgsConstructor
public class AuthController {
    private final AuthService authService;
    private final EmailService emailService;

   //인증용 이메일 인증 코드 전송
   @PostMapping("/email/verify")
   public ResponseEntity<String> sendVerificationEmail(@RequestBody Map<String, String> request) {
       String email = request.get("email");
       if (email == null || email.isEmpty()) {
           return ResponseEntity.badRequest().body("이메일 주소를 입력해주세요.");
       }

       //인증코드 생성 6자리
       String verificationCode = String.format("%06d", new Random().nextInt(1000000));

       // 네이버 인증 로직 추가
       if (email.endsWith("@naver.com")) {
           // 네이버 이메일 인증 코드 전송 로직
           try {
               emailService.sendEmail(email, "WeatherFit 회원가입 인증 코드", "인증 코드: " + verificationCode);
               return ResponseEntity.ok(verificationCode);
           } catch (Exception e) {
               return ResponseEntity.internalServerError().body("이메일 전송에 실패했습니다.");
           }
       }

       //gmail 이메일 인증 코드 전송
       try {
           emailService.sendEmail(email, "WeatherFit 회원가입 인증 코드", "인증 코드: " + verificationCode);
           return ResponseEntity.ok(verificationCode);
       } catch (Exception e) {
           return ResponseEntity.internalServerError().body("이메일 전송에 실패했습니다.");
       }
    }


    //user 이메일 중복체크
    @PostMapping("/isunique")
    public ResponseEntity<Boolean> isUnique(@RequestBody Map<String, String> dataMap) {
        int type = Integer.parseInt(dataMap.get("type"));
        return ResponseEntity.ok(authService.checkUnique(type,dataMap.get("data")));
    }

    // user 회원가입
    @PostMapping("/join")
    public ResponseEntity<UserResDTO> join(@RequestBody UserReqDTO userReqDTO) {
        return ResponseEntity.ok(authService.join(userReqDTO));
    }


}
