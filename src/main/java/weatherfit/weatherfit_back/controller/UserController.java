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
import java.util.List;

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
    public ResponseEntity<List<UserResDTO>> getUserList() {
        List<UserResDTO> userList = userService.getUserList();
        return ResponseEntity.ok(userList);
    }

    //회원 정보 수정
    @PostMapping("/update")
    public ResponseEntity<Boolean> updateUser(@RequestBody UserReqDTO userReqDTO) {
        userService.updateUser(userReqDTO);
        return ResponseEntity.ok(true);
    }

    //회원 탈퇴
    @DeleteMapping("/delete")
    public ResponseEntity<String> deleteUser(@RequestParam String email) {
        String result = userService.deleteUser(email);
        return ResponseEntity.ok(result);
    }

    //비밀번호 조회
    @GetMapping("/password")
    public ResponseEntity<String> getPassword(@RequestParam String email) {
        String password = userService.getPassword(email);
        return ResponseEntity.ok(password);
    }

    //좋아요 추가
    @PostMapping("/like/add")
    public ResponseEntity<String> addLike(@RequestBody Long userId, @RequestBody Long coordinateId) {
        userService.addLike(userId, coordinateId);
        return ResponseEntity.ok("좋아요 추가 완료");
    }

    //좋아요 삭제
    @DeleteMapping("/like/delete")
    public ResponseEntity<String> deleteLike(@RequestBody Long userId, @RequestBody Long coordinateId) {
        userService.deleteLike(userId, coordinateId);
        return ResponseEntity.ok("좋아요 삭제 완료");
    }


}
