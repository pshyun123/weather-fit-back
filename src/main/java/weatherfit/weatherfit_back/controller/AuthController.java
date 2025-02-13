package weatherfit.weatherfit_back.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestPart;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.multipart.MultipartFile;

import java.util.Map;
import java.util.Random;


import weatherfit.weatherfit_back.service.AuthService;
import weatherfit.weatherfit_back.dto.UserResDTO;
import weatherfit.weatherfit_back.dto.UserReqDTO;
import weatherfit.weatherfit_back.service.EmailService;
@RestController
@RequestMapping("/auth")
@RequiredArgsConstructor
@CrossOrigin(origins = "http://localhost:3000", allowCredentials = "true")
public class AuthController {
    private final AuthService authService;
    private final EmailService emailService;

   //인증용 이메일 인증 코드 전송
   @PostMapping("/email/verify")
   @CrossOrigin(origins = "http://localhost:3000", allowCredentials = "true")
   public ResponseEntity<String> sendVerificationEmail(@RequestBody Map<String, String> request) {
       String email = request.get("email");
       if (email == null || email.isEmpty()) {
           return ResponseEntity.badRequest().body("이메일 주소를 입력해주세요.");
       }

       //인증코드 생성 6자리
       String verificationCode = String.format("%06d", new Random().nextInt(1000000));

       // 네이버 인증 로직 추가
       if (email.endsWith("@naver.com")) {
           try {
               emailService.sendEmailByNaver(email, "WeatherFit 회원가입 인증 코드", "인증 코드: " + verificationCode);
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

      //인증코드와 입력한 인증코드 일치하는지 확인
      @PostMapping("/email/check")
      @CrossOrigin(origins = "http://localhost:3000", allowCredentials = "true")
      public ResponseEntity<Boolean> verifyCode(@RequestBody Map<String, String> request) {
          String email = request.get("email");
          String code = request.get("code");
          
          if (email == null || code == null) {
              return ResponseEntity.badRequest().body(false);
          }
          
          return ResponseEntity.ok(emailService.verifyCode(email, code));
      }


    //user 이메일 중복체크
    @PostMapping("/isunique")
    @CrossOrigin(origins = "http://localhost:3000", allowCredentials = "true")
    public ResponseEntity<Boolean> isUnique(@RequestBody Map<String, String> dataMap) {
        int type = Integer.parseInt(dataMap.get("type"));
        return ResponseEntity.ok(authService.checkUnique(type,dataMap.get("data")));
    }

    // user 회원가입
    @PostMapping("/join")
    @CrossOrigin(origins = "http://localhost:3000", allowCredentials = "true")
    public ResponseEntity<UserResDTO> join(
        @RequestPart("profileImage") MultipartFile profileImage,
        @RequestPart("userData") UserReqDTO userReqDTO
    ) {
        try {
            // 이미지 파일 처리 로직
            String fileName = profileImage.getOriginalFilename();
            String fileUrl = "uploads/" + fileName; // 실제 저장 경로로 수정 필요
            
            // 이미지 저장 로직 추가 필요
            // profileImage.transferTo(new File(fileUrl));
            
            // UserReqDTO에 이미지 URL 설정
            userReqDTO.setProfileImage(fileUrl);
            
            return ResponseEntity.ok(authService.join(userReqDTO));
        } catch (Exception e) {
            throw new RuntimeException("파일 업로드 실패: " + e.getMessage());
        }
    }

    // user 로그인
    @PostMapping("/login")
    @CrossOrigin(origins = "http://localhost:3000", allowCredentials = "true")
    public ResponseEntity<UserResDTO> login(@RequestBody UserReqDTO userReqDTO) {
        return ResponseEntity.ok(authService.login(userReqDTO));
    }

    // user 로그아웃
    @PostMapping("/logout")
    @CrossOrigin(origins = "http://localhost:3000", allowCredentials = "true")
    public ResponseEntity<Void> logout() {
        authService.logout();
        return ResponseEntity.ok().build();
    }

}
