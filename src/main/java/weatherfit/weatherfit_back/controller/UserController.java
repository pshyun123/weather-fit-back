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
import weatherfit.weatherfit_back.dto.LikeDTO;

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
    @io.swagger.v3.oas.annotations.Operation(
        summary = "좋아요 추가",
        description = "사용자가 코디네이트에 좋아요를 추가합니다.",
        requestBody = @io.swagger.v3.oas.annotations.parameters.RequestBody(
            content = @io.swagger.v3.oas.annotations.media.Content(
                mediaType = "application/json",
                schema = @io.swagger.v3.oas.annotations.media.Schema(
                    example = "{\"userId\": 1, \"coordinateId\": 212}"
                )
            )
        )
    )
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

     //여기서부터 좋아요+취향조건.

    //좋아요 한 것들 중 preferences가 미니멀인 것들 조회
    @GetMapping("/like/minimal")
    @CrossOrigin(origins = "http://localhost:3000", allowCredentials = "true")
    public ResponseEntity<List<LikeDTO>> getMinimalLikeList(@RequestParam Long userId) {
        try {
            List<LikeDTO> likeList = userService.getMinimalLikeList(userId);
            return ResponseEntity.ok(likeList);
        } catch (Exception e) {
            log.error("좋아요 한 것들 중 preferences가 미니멀인 것들 조회 실패: {}", e.getMessage(), e);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                .body(List.of());
        }
    }

    // 좋아요 한 것들 중 preferences가 모던인 것들 조회
    @GetMapping("/like/modern")
    @CrossOrigin(origins = "http://localhost:3000", allowCredentials = "true")
    public ResponseEntity<List<LikeDTO>> getModernLikeList(@RequestParam Long userId) {
        try {
            List<LikeDTO> likeList = userService.getModernLikeList(userId);
            return ResponseEntity.ok(likeList);
        } catch (Exception e) {
            log.error("좋아요 한 것들 중 preferences가 모던인 것들 조회 실패: {}", e.getMessage(), e);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                .body(List.of());
        }
    }

    // 좋아요 한 것들 중 preferences가 캐주얼인 것들 조회
    @GetMapping("/like/casual")
    @CrossOrigin(origins = "http://localhost:3000", allowCredentials = "true")
    public ResponseEntity<List<LikeDTO>> getCasualLikeList(@RequestParam Long userId) {
        try {
            List<LikeDTO> likeList = userService.getCasualLikeList(userId);
            return ResponseEntity.ok(likeList);
        } catch (Exception e) {
            log.error("좋아요 한 것들 중 preferences가 캐주얼인 것들 조회 실패: {}", e.getMessage(), e);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                .body(List.of());
        }
    }

    // 좋아요 한 것들 중 preferences가 스트릿인 것들 조회
    @GetMapping("/like/street")
    @CrossOrigin(origins = "http://localhost:3000", allowCredentials = "true")
    public ResponseEntity<List<LikeDTO>> getStreetLikeList(@RequestParam Long userId) {
        try {
            List<LikeDTO> likeList = userService.getStreetLikeList(userId);
            return ResponseEntity.ok(likeList);
        } catch (Exception e) {
            log.error("좋아요 한 것들 중 preferences가 스트릿인 것들 조회 실패: {}", e.getMessage(), e);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                .body(List.of());
        }
    }

    // 좋아요 한 것들 중 preferences가 러블리인 것들 조회
    @GetMapping("/like/romantic")
    @CrossOrigin(origins = "http://localhost:3000", allowCredentials = "true")
    public ResponseEntity<List<LikeDTO>> getRomanticLikeList(@RequestParam Long userId) {
        try {
            List<LikeDTO> likeList = userService.getRomanticLikeList(userId);
            return ResponseEntity.ok(likeList);
        } catch (Exception e) {
            log.error("좋아요 한 것들 중 preferences가 러블리인 것들 조회 실패: {}", e.getMessage(), e);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                .body(List.of());
        }
    }

    // 좋아요 한 것들 중 preferences가 럭셔리인 것들 조회
    @GetMapping("/like/luxury")
    @CrossOrigin(origins = "http://localhost:3000", allowCredentials = "true")
    public ResponseEntity<List<LikeDTO>> getLuxuryLikeList(@RequestParam Long userId) {
        try {
            List<LikeDTO> likeList = userService.getLuxuryLikeList(userId);
            return ResponseEntity.ok(likeList);
        } catch (Exception e) {
            log.error("좋아요 한 것들 중 preferences가 럭셔리인 것들 조회 실패: {}", e.getMessage(), e);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                .body(List.of());
        }
    }

  ////////////////////// 여기서부터 날씨+ 좋아요
  

  //좋아요 한 것들 중 날씨 조건이 더움인 것들 조회
  @GetMapping("/like/hot")
  @CrossOrigin(origins = "http://localhost:3000", allowCredentials = "true")
  public ResponseEntity<List<LikeDTO>> getHotLikeList(@RequestParam Long userId) {
    try {
      List<LikeDTO> likeList = userService.getHotLikeList(userId);
      return ResponseEntity.ok(likeList);
    } catch (Exception e) {
      log.error("좋아요 한 것들 중 날씨 조건이 더움인 것들 조회 실패: {}", e.getMessage(), e);
      return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
          .body(List.of());
    }
  }
  
  //좋아요 한 것들 중 날씨 조건이 따뜻한 것들 조회
  @GetMapping("/like/warm")
  @CrossOrigin(origins = "http://localhost:3000", allowCredentials = "true")
  public ResponseEntity<List<LikeDTO>> getWarmLikeList(@RequestParam Long userId) {
    try {
      List<LikeDTO> likeList = userService.getWarmLikeList(userId);
      return ResponseEntity.ok(likeList);
    } catch (Exception e) {
      log.error("좋아요 한 것들 중 날씨 조건이 따뜻한 것들 조회 실패: {}", e.getMessage(), e);
      return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
          .body(List.of());
    }
  }

  //좋아요 한 것들 중 날씨 조건이 비인 것들 조회
  @GetMapping("/like/rain")
  @CrossOrigin(origins = "http://localhost:3000", allowCredentials = "true")
  public ResponseEntity<List<LikeDTO>> getRainLikeList(@RequestParam Long userId) {
    try {
      List<LikeDTO> likeList = userService.getRainLikeList(userId); 
      return ResponseEntity.ok(likeList);
    } catch (Exception e) {
      log.error("좋아요 한 것들 중 날씨 조건이 비인 것들 조회 실패: {}", e.getMessage(), e);
      return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
          .body(List.of());
    }
  }

  //좋아요 한 것들 중 날씨 조건이 추움인 것들 조회
  @GetMapping("/like/cold")
  @CrossOrigin(origins = "http://localhost:3000", allowCredentials = "true")
  public ResponseEntity<List<LikeDTO>> getColdLikeList(@RequestParam Long userId) {
    try {
      List<LikeDTO> likeList = userService.getColdLikeList(userId);
      return ResponseEntity.ok(likeList);
    } catch (Exception e) {
      log.error("좋아요 한 것들 중 날씨 조건이 추움인 것들 조회 실패: {}", e.getMessage(), e);
      return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
          .body(List.of());
    }
  }

  //좋아요 한 것들 중 날씨 조건이 매우 추움인 것들 조회 
  @GetMapping("/like/chill")
  @CrossOrigin(origins = "http://localhost:3000", allowCredentials = "true")
  public ResponseEntity<List<LikeDTO>> getChillLikeList(@RequestParam Long userId) {
    try {
      List<LikeDTO> likeList = userService.getChillLikeList(userId);
      return ResponseEntity.ok(likeList);
    } catch (Exception e) {
      log.error("좋아요 한 것들 중 날씨 조건이 매우 추움인 것들 조회 실패: {}", e.getMessage(), e);
      return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
          .body(List.of());
    }
  }

  //좋아요 한 것들 중 날씨 조건이 눈인 것들 조회
  @GetMapping("/like/snow")
  @CrossOrigin(origins = "http://localhost:3000", allowCredentials = "true")
  public ResponseEntity<List<LikeDTO>> getSnowLikeList(@RequestParam Long userId) {
    try {
      List<LikeDTO> likeList = userService.getSnowLikeList(userId);
      return ResponseEntity.ok(likeList);
    } catch (Exception e) {
      log.error("좋아요 한 것들 중 날씨 조건이 눈인 것들 조회 실패: {}", e.getMessage(), e);
      return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
          .body(List.of());
    }
  }


  ///////////////// 여기서 날씨 + TPo
  
  // 좋아요 한 것들 중 TPO가 데이트인 것들 조회
  @GetMapping("/like/date")
  @CrossOrigin(origins = "http://localhost:3000", allowCredentials = "true")
  public ResponseEntity<List<LikeDTO>> getDateLikeList(@RequestParam Long userId) {
    try {
      List<LikeDTO> likeList = userService.getDateLikeList(userId);
      return ResponseEntity.ok(likeList);
    } catch (Exception e) {
      log.error("좋아요 한 것들 중 TPO가 데이트인 것들 조회 실패: {}", e.getMessage(), e);
      return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
          .body(List.of());
    }
  }

   // 좋아요 한 것들 중 TPO가 출근인 것들 조회
   @GetMapping("/like/work")
   @CrossOrigin(origins = "http://localhost:3000", allowCredentials = "true")
   public ResponseEntity<List<LikeDTO>> getWorkLikeList(@RequestParam Long userId) {
    try {
      List<LikeDTO> likeList = userService.getWorkLikeList(userId);
      return ResponseEntity.ok(likeList);
    } catch (Exception e) {
      log.error("좋아요 한 것들 중 TPO가 출근인 것들 조회 실패: {}", e.getMessage(), e);
      return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
          .body(List.of());
    }
   }

   //좋아요 한 것들 중 TPO가 여행인 것들 조회
   @GetMapping("/like/travel")
   @CrossOrigin(origins = "http://localhost:3000", allowCredentials = "true")
   public ResponseEntity<List<LikeDTO>> getTravelLikeList(@RequestParam Long userId) {
    try {
      List<LikeDTO> likeList = userService.getTravelLikeList(userId);
      return ResponseEntity.ok(likeList);
    } catch (Exception e) {
      log.error("좋아요 한 것들 중 TPO가 여행인 것들 조회 실패: {}", e.getMessage(), e);
      return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
          .body(List.of());
    }
   }

   //좋아요 한 것들 중 TPO가 운동인 것들 조회
   @GetMapping("/like/exercise")
   @CrossOrigin(origins = "http://localhost:3000", allowCredentials = "true")
   public ResponseEntity<List<LikeDTO>> getExerciseLikeList(@RequestParam Long userId) {
    try {
      List<LikeDTO> likeList = userService.getExerciseLikeList(userId);
      return ResponseEntity.ok(likeList);
    } catch (Exception e) {
      log.error("좋아요 한 것들 중 TPO가 운동인 것들 조회 실패: {}", e.getMessage(), e);
      return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
          .body(List.of());
    }
   }

   //좋아요 한 것들 중 TPO가 모임인 것들 조회
   @GetMapping("/like/meeting")
   @CrossOrigin(origins = "http://localhost:3000", allowCredentials = "true")
   public ResponseEntity<List<LikeDTO>> getMeetingLikeList(@RequestParam Long userId) {
    try {
      List<LikeDTO> likeList = userService.getMeetingLikeList(userId);
      return ResponseEntity.ok(likeList);
    } catch (Exception e) {
      log.error("좋아요 한 것들 중 TPO가 모임인 것들 조회 실패: {}", e.getMessage(), e);
      return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
          .body(List.of());
    }
   }

   // 좋아요 한 것들 중 TPO가 일상인 것들 조회
   @GetMapping("/like/daily")
   @CrossOrigin(origins = "http://localhost:3000", allowCredentials = "true")
   public ResponseEntity<List<LikeDTO>> getDailyLikeList(@RequestParam Long userId) {
    try {
      List<LikeDTO> likeList = userService.getDailyLikeList(userId);
      return ResponseEntity.ok(likeList);
    } catch (Exception e) {
      log.error("좋아요 한 것들 중 TPO가 일상인 것들 조회 실패: {}", e.getMessage(), e);
      return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
          .body(List.of());
    }
   }
   


}
