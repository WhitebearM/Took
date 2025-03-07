package isn_t_this_e_not_i.now_waypoint_core.domain.auth.controller;

import isn_t_this_e_not_i.now_waypoint_core.domain.auth.mail.service.EmailAuthService;
import isn_t_this_e_not_i.now_waypoint_core.domain.auth.user.dto.ApiResponse;
import isn_t_this_e_not_i.now_waypoint_core.domain.auth.user.dto.UserRequest;
import isn_t_this_e_not_i.now_waypoint_core.domain.auth.user.dto.UserResponse;
import isn_t_this_e_not_i.now_waypoint_core.domain.auth.service.UserService;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/api/user")
@RequiredArgsConstructor
public class UserController {

    private final UserService userService;
    private final EmailAuthService emailAuthService;

    @Value("${spring.security.oauth2.client.registration.kakao.redirect-uri}")
    private String KAKAO_REDIRECT_URI;

    @Value("${spring.security.oauth2.client.registration.kakao.client-id}")
    private String KAKAO_CLIENT_ID;

    @Value("${spring.security.oauth2.client.registration.google.client-id}")
    private String GOOGLE_CLIENT_ID;

    @Value("${spring.security.oauth2.client.registration.google.redirect-uri}")
    private String GOOGLE_REDIRECT_URI;


    @PostMapping("/register")
    public ResponseEntity<ApiResponse<String>> resist(@RequestBody @Valid UserRequest.registerRequest registerRequest) {
        String message = userService.register(registerRequest);
        ApiResponse<String> apiResponse = ApiResponse.<String>builder()
                .status(HttpStatus.OK.value())
                .data(message)
                .build();

        return ResponseEntity.ok(apiResponse);
    }

    @GetMapping("/login/kakao")
    public String loginWithKakao() {
        String state = UUID.randomUUID().toString();
        String kakaoLoginUrl = String.format(
                "https://accounts.kakao.com/login?continue=https://kauth.kakao.com/oauth/authorize?scope=profile_nickname%%20profile_image%%20account_email&response_type=code&state=%s&redirect_uri=%s&through_account=true&client_id=%s",
                state, KAKAO_REDIRECT_URI, KAKAO_CLIENT_ID
        );
        return "redirect:" + kakaoLoginUrl;
    }

    @GetMapping("/login/google")
    public String loginWithGoogle() {
        String state = UUID.randomUUID().toString();
        String googleLoginUrl = String.format(
                "https://accounts.google.com/o/oauth2/auth?" +
                        "client_id=%s&redirect_uri=%s" +
                        "&response_type=code&scope=https://www.googleapis.com/auth/userinfo.email https://www.googleapis.com/auth/userinfo.profile&state=%s",
                GOOGLE_CLIENT_ID, GOOGLE_REDIRECT_URI, state
        );
        return "redirect:" + googleLoginUrl;
    }

    @PostMapping("/withdrawal")
    public ResponseEntity<String> withdraw(Authentication auth){
        userService.withdrawal(auth.getName());
        return ResponseEntity.ok("회원탈퇴되었습니다.");
    }

    @PostMapping("/userId")
    public ResponseEntity<UserResponse.findUserInfo> userId(@RequestBody @Valid UserRequest.findUserInfo findUserInfo) {
        emailAuthService.confirmAuthNumber(findUserInfo.getAuthNumber(),findUserInfo.getEmail());
        UserResponse.findUserInfo userId = UserResponse.findUserInfo.builder().id(userService.getUserId(findUserInfo.getEmail())).build();
        return ResponseEntity.ok().body(userId);
    }

    @GetMapping("/all")
    public ResponseEntity<List<UserResponse.followInfo>> getFollowers(Authentication auth){
        return ResponseEntity.ok().body(userService.getAllUser());
    }

    @GetMapping("/locate")
    public ResponseEntity<List<UserResponse.locateUserInfo>> getUserByLocate(Authentication auth){
        return ResponseEntity.ok().body(userService.getUserByLocate(auth.getName()));
    }

    @PutMapping("/password/find")
    public ResponseEntity<UserResponse.findUserInfo> findPassword(@RequestBody @Valid UserRequest.findUserInfo findUserInfo){
        emailAuthService.confirmAuthNumber(findUserInfo.getAuthNumber(), findUserInfo.getEmail());
        UserResponse.findUserInfo userPassword = UserResponse.findUserInfo.builder().password(userService.randomPassword(findUserInfo.getLoginId())).build();
        return ResponseEntity.ok().body(userPassword);
    }

    @PutMapping("/password/change")
    public ResponseEntity<String> changePassword(Authentication auth, @RequestBody @Valid UserRequest.updatePasswordRequest updatePasswordRequest) {
        userService.updateUserPassword(auth.getName(), updatePasswordRequest.getPassword());
        return ResponseEntity.ok("비밀번호가 변경되었습니다.");
    }

    @PutMapping("/nickname/change")
    public ResponseEntity<UserResponse.updateNickname> updateNickname(Authentication auth, @RequestBody @Valid UserRequest userRequest) {
        return ResponseEntity.ok().body(userService.updateNickname(auth.getName(),userRequest.getNickname()));
    }

    @PutMapping("/profileImage/change")
    public ResponseEntity<UserResponse.updateProfileImage> updateProfileImage(Authentication auth, @RequestPart("file")MultipartFile file) {
        return ResponseEntity.ok().body(userService.updateProfileImage(auth.getName(), file));
    }

    @PutMapping
    public ResponseEntity<UserResponse> updateInfo(Authentication auth, @RequestBody @Valid UserRequest userRequest) {
        return ResponseEntity.ok().body(userService.updateUserInfo(auth.getName(), userRequest));
    }

    @GetMapping
    public ResponseEntity<UserResponse.userInfo> userPage(Authentication auth,@RequestParam(value ="nickname", required = false)String nickname){
        if (nickname == null) {
            return ResponseEntity.ok().body(userService.getUserInfo(auth.getName()));
        }else{
            return ResponseEntity.ok().body(userService.getOtherUserInfo(nickname));
        }
    }

    @PostMapping("/checkLoginId")
    public ResponseEntity<String> checkLoginId(@RequestBody UserRequest.updatePasswordRequest updatePasswordRequest){
        userService.checkLoginId(updatePasswordRequest.getLoginId());
        return ResponseEntity.ok().body("가능한 아이디입니다.");
    }

    @GetMapping("/auth")
    public ResponseEntity<String> checkAuth(HttpServletResponse response){
        String authHeader = response.getHeader("Authorization");
        String token = authHeader.substring(7);

        if (token != null) {
            return ResponseEntity.ok().body(token);
        } else {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
        }
    }

    @GetMapping("/test")
    public String testP() {
        return "test Page";
    }

    @ExceptionHandler(UsernameNotFoundException.class)
    public ResponseEntity<String> handleUsernameNotFoundException(UsernameNotFoundException e) {
        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(e.getMessage());
    }
}
