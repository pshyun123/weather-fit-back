package weatherfit.weatherfit_back.controller;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.CrossOrigin;
import java.util.List;
import java.util.Map;
import java.util.Base64;
import java.io.File;
import java.io.FileOutputStream;

import java.util.HashMap;
import org.springframework.http.HttpStatus;

import weatherfit.weatherfit_back.service.UserService;
import weatherfit.weatherfit_back.dto.UserReqDTO;
import weatherfit.weatherfit_back.dto.UserResDTO;

@RestController
@Slf4j
@RequestMapping("/member")
@RequiredArgsConstructor
public class UserController {

    private final UserService userService;


    //회원 리스트 불러오기
    @GetMapping("/list")
    @CrossOrigin(origins = "http://localhost:3000", allowCredentials = "true")
    public ResponseEntity<List<UserResDTO>> getUserList() {
        List<UserResDTO> userList = userService.getUserList();
        return ResponseEntity.ok(userList);
    }

    //회원 비밀번호 수정
    @PostMapping("/update/password")
    @CrossOrigin(origins = "http://localhost:3000", allowCredentials = "true")
    public ResponseEntity<Boolean> updateUser(@RequestBody UserReqDTO userReqDTO) {
        try {
            log.info("비밀번호 업데이트 요청: email={}", userReqDTO.getEmail());
            log.info("비밀번호 필드: password={}, newPassword={}", 
                    userReqDTO.getPassword() != null ? "입력됨" : "null", 
                    userReqDTO.getNewPassword() != null ? "입력됨" : "null");
            
            // newPassword 필드가 있는 경우 (우선순위)
            if (userReqDTO.getNewPassword() != null && !userReqDTO.getNewPassword().isEmpty()) {
                userService.updatePassword(userReqDTO.getEmail(), userReqDTO.getNewPassword());
                log.info("비밀번호 업데이트 성공 (newPassword 필드 사용)");
                return ResponseEntity.ok(true);
            } 
            // password 필드만 있는 경우
            else if (userReqDTO.getPassword() != null && !userReqDTO.getPassword().isEmpty()) {
                userService.updatePassword(userReqDTO.getEmail(), userReqDTO.getPassword());
                log.info("비밀번호 업데이트 성공 (password 필드 사용)");
                return ResponseEntity.ok(true);
            }
            // 두 필드 모두 비어있는 경우
            else {
                log.warn("비밀번호가 비어있습니다. password와 newPassword 필드 모두 비어있습니다.");
                return ResponseEntity.badRequest().body(false);
            }
        } catch (Exception e) {
            log.error("비밀번호 업데이트 실패: {}", e.getMessage(), e);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(false);
        }
    }

    //회원 비밀번호 체크
    @PostMapping("/verify/password")
    @CrossOrigin(origins = "http://localhost:3000", allowCredentials = "true")
    public ResponseEntity<Boolean> verifyPassword(@RequestBody UserReqDTO userReqDTO) {
        boolean isPasswordCorrect = userService.verifyPassword(userReqDTO.getEmail(), userReqDTO.getPassword());

        return ResponseEntity.ok(isPasswordCorrect);
    }


    //회원 프로필 이미지 수정
    @PostMapping("/update/profileImage")
    @CrossOrigin(origins = "http://localhost:3000", allowCredentials = "true")
    public ResponseEntity<Map<String, Object>> updateProfileImage(@RequestBody Map<String, Object> request) {
        try {
            String email = (String) request.get("email");
            String imageBase64 = (String) request.get("profileImage");
            String fileName = (String) request.get("fileName");
            
            // Base64 데이터에서 실제 이미지 데이터 추출 (data:image/jpeg;base64, 부분 제거)
            String base64Image = imageBase64.split(",")[1];
            byte[] imageBytes = Base64.getDecoder().decode(base64Image);
            
            // 파일 저장
            String uniqueFileName = System.currentTimeMillis() + "_" + fileName;
            String uploadPath = "uploads";
            File uploadDir = new File(uploadPath);
            if (!uploadDir.exists()) {
                uploadDir.mkdirs();
            }
            
            File outputFile = new File(uploadDir, uniqueFileName);
            try (FileOutputStream outputStream = new FileOutputStream(outputFile)) {
                outputStream.write(imageBytes);
            }
            
            // 프로필 이미지 경로 업데이트
            String profileImagePath = "/uploads/" + uniqueFileName;
            userService.updateProfileImage(email, profileImagePath);
            
            // 응답 데이터 생성
            Map<String, Object> response = new HashMap<>();
            response.put("success", true);
            response.put("profileImage", profileImagePath);
            
            return ResponseEntity.ok(response);
        } catch (Exception e) {
            Map<String, Object> errorResponse = new HashMap<>();
            errorResponse.put("success", false);
            errorResponse.put("message", e.getMessage());
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(errorResponse);
        }
    }

    //회원 취향 수정
    @PostMapping("/update/preferences")
    @CrossOrigin(origins = "http://localhost:3000", allowCredentials = "true")
    public ResponseEntity<Boolean> updatePreferences(@RequestBody UserReqDTO userReqDTO) {
        userService.updatePreferences(userReqDTO.getEmail(), userReqDTO.getPreferencesAsString());
        return ResponseEntity.ok(true);
    }

    //회원 나이대 수정
    @PostMapping("/update/ageGroup")
    @CrossOrigin(origins = "http://localhost:3000", allowCredentials = "true")
    public ResponseEntity<?> updateAgeGroup(@RequestBody Map<String, Object> requestMap) {
        try {
            log.info("연령대 업데이트 요청 수신: {}", requestMap);
            
            String email = (String) requestMap.get("email");
            String ageGroup = (String) requestMap.get("ageGroup");
            
            if (email == null || email.isEmpty()) {
                log.error("이메일이 비어있습니다.");
                return ResponseEntity.badRequest().body(Map.of(
                    "success", false, 
                    "message", "이메일이 비어있습니다."
                ));
            }
            
            if (ageGroup == null || ageGroup.isEmpty()) {
                log.error("연령대가 비어있습니다.");
                return ResponseEntity.badRequest().body(Map.of(
                    "success", false, 
                    "message", "연령대가 비어있습니다."
                ));
            }
            
            userService.updateAgeGroup(email, ageGroup);
            log.info("연령대 업데이트 성공: email={}, ageGroup={}", email, ageGroup);
            
            return ResponseEntity.ok(Map.of(
                "success", true, 
                "message", "연령대가 성공적으로 업데이트되었습니다."
            ));
        } catch (Exception e) {
            log.error("연령대 업데이트 실패: {}", e.getMessage(), e);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                .body(Map.of(
                    "success", false, 
                    "message", "연령대 업데이트 중 오류가 발생했습니다: " + e.getMessage()
                ));
        }
    }
    
    

    //회원 탈퇴
    @DeleteMapping("/delete")
    @CrossOrigin(origins = "http://localhost:3000", allowCredentials = "true")
    public ResponseEntity<String> deleteUser(@RequestParam String email) {
        String result = userService.deleteUser(email);
        return ResponseEntity.ok(result);
    }

    //비밀번호 조회
    @GetMapping("/password")
    @CrossOrigin(origins = "http://localhost:3000", allowCredentials = "true")
    public ResponseEntity<String> getPassword(@RequestParam String email) {
        String password = userService.getPassword(email);
        return ResponseEntity.ok(password);
    }

    //좋아요 추가
    @PostMapping("/like/add")
    @CrossOrigin(origins = "http://localhost:3000", allowCredentials = "true")
    public ResponseEntity<String> addLike(@RequestBody Map<String, Long> request) {
        Long userId = request.get("userId");
        Long coordinateId = request.get("coordinateId");
        
        if (userId == null || coordinateId == null) {
            return ResponseEntity.badRequest().body("사용자 ID와 착장 ID는 필수입니다.");
        }
        
        try {
            userService.addLike(userId, coordinateId);
            return ResponseEntity.ok("좋아요 추가 완료");
        } catch (Exception e) {
            log.error("좋아요 추가 실패: {}", e.getMessage(), e);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                .body("좋아요 추가 중 오류가 발생했습니다: " + e.getMessage());
        }
    }

    //좋아요 삭제
    @DeleteMapping("/like/delete")
    @CrossOrigin(origins = "http://localhost:3000", allowCredentials = "true")
    public ResponseEntity<String> deleteLike(@RequestBody Map<String, Long> request) {
        Long userId = request.get("userId");
        Long coordinateId = request.get("coordinateId");
        
        if (userId == null || coordinateId == null) {
            return ResponseEntity.badRequest().body("사용자 ID와 착장 ID는 필수입니다.");
        }
        
        try {
            userService.deleteLike(userId, coordinateId);
            return ResponseEntity.ok("좋아요 삭제 완료");
        } catch (Exception e) {
            log.error("좋아요 삭제 실패: {}", e.getMessage(), e);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                .body("좋아요 삭제 중 오류가 발생했습니다: " + e.getMessage());
        }
    }


}
