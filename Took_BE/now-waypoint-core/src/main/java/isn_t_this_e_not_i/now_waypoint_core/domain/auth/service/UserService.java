package isn_t_this_e_not_i.now_waypoint_core.domain.auth.service;

import isn_t_this_e_not_i.now_waypoint_core.domain.auth.exception.auth.DuplicatePasswordException;
import isn_t_this_e_not_i.now_waypoint_core.domain.auth.exception.auth.NicknameNotFoundException;
import isn_t_this_e_not_i.now_waypoint_core.domain.auth.user.UserFollower;
import isn_t_this_e_not_i.now_waypoint_core.domain.auth.user.UserFollowing;
import isn_t_this_e_not_i.now_waypoint_core.domain.auth.user.dto.UserRequest;
import isn_t_this_e_not_i.now_waypoint_core.domain.auth.user.dto.UserResponse;
import isn_t_this_e_not_i.now_waypoint_core.domain.auth.exception.auth.DuplicateLoginIdException;
import isn_t_this_e_not_i.now_waypoint_core.domain.auth.repository.UserRepository;
import isn_t_this_e_not_i.now_waypoint_core.domain.auth.user.User;
import isn_t_this_e_not_i.now_waypoint_core.domain.auth.user.UserRole;
import isn_t_this_e_not_i.now_waypoint_core.domain.chat.service.ChatMessageService;
import isn_t_this_e_not_i.now_waypoint_core.domain.chat.service.UserChatRoomService;
import isn_t_this_e_not_i.now_waypoint_core.domain.post.dto.response.PostResponse;
import isn_t_this_e_not_i.now_waypoint_core.domain.post.entity.Post;
import isn_t_this_e_not_i.now_waypoint_core.domain.post.service.FileUploadService;
import isn_t_this_e_not_i.now_waypoint_core.domain.post.service.LikeService;
import isn_t_this_e_not_i.now_waypoint_core.domain.post.service.PostService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.UUID;


@Service
@RequiredArgsConstructor
@Slf4j
public class UserService {

    @Value("${default.profile.image.url}")
    private String defaultImageUrl;

    private final PostService postService;
    private final UserRepository userRepository;
    private final BCryptPasswordEncoder bCryptPasswordEncoder;
    private final TokenService tokenService;
    private final FileUploadService fileUploadService;
    private final UserFollowService userFollowService;
    private final ChatMessageService chatMessageService;
    private final UserChatRoomService userChatRoomService;
    private final LikeService likeService;

    //회원 등록
    @Transactional
    public String register(UserRequest.registerRequest registerRequest) {
        List<UserFollower> followers = new ArrayList<>();
        List<UserFollowing> followings = new ArrayList<>();
        String message = "";

        if (userRepository.findByEmail(registerRequest.getEmail()).isPresent()) {
            throw new DuplicateLoginIdException("이미 존재하는 이메일입니다.");
        }

        if (registerRequest.getName() == null) {
            registerRequest.setName(registerRequest.getNickname());
        }

        if (registerRequest.getProfileImageUrl() == null) {
            registerRequest.setProfileImageUrl(defaultImageUrl);
        }
        String loginId = registerRequest.getLoginId();
        String nickname = registerRequest.getNickname();
        Optional<User> findUser = userRepository.findByLoginId(loginId);
        Optional<User> findUserNickname = userRepository.findByNickname(nickname);
        Optional<User> findUserLoginAndNickname = userRepository.findByLoginIdAndNickname(loginId, nickname);

        //중복 아이디만
        if (findUser.isPresent() && findUserNickname.isEmpty()) {
            message = "idNo";
        }
        //중복 닉네임만
        else if(findUserNickname.isPresent() && findUser.isEmpty()) {
            message = "nicknameNo";
        }
        //닉네임과 로그인 아이디 모두 중복
        else if (findUserLoginAndNickname.isPresent()) {
            message = "idNicknameNo";
        } else {
            User user = User.builder()
                    .loginId(registerRequest.getLoginId())
                    .password(bCryptPasswordEncoder.encode(registerRequest.getPassword()))
                    .name(registerRequest.getName())
                    .nickname(registerRequest.getNickname())
                    .email(registerRequest.getEmail())
                    .profileImageUrl(registerRequest.getProfileImageUrl())
                    .description(registerRequest.getDescription())
                    .role(UserRole.USER)
                    .followers(followers)
                    .followings(followings)
                    .createDate(LocalDateTime.now())
                    .loginDate(LocalDateTime.now())
                    .active("true")
                    .locate("11.111111,11.1111")
                    .build();

            userRepository.save(user);
            message = "ok";
        }

        return message;
    }

    //회원 탈퇴
    @Transactional
    public void withdrawal(String loginId) {
        User user = userRepository.findByLoginId(loginId).orElseThrow(() -> new UsernameNotFoundException("일치하는 유저가 없습니다."));
        userFollowService.deleteFollowingByUser(user.getNickname());
        postService.deletePostRedis(user.getNickname());

        likeService.decreaseLikeCount(user);
        userRepository.deleteByLoginId(loginId);
        String accessToken = tokenService.findByLoginId(loginId).get().getAccessToken();
        tokenService.deleteToken(accessToken);
    }

    @Transactional
    public void checkLoginId(String loginId){
        if(userRepository.findByLoginId(loginId).isPresent()){
            throw new DuplicateLoginIdException("이미 존재하는 아이디입니다.");
        }
    }

    //회원 조회
    @Transactional
    public User findUserByLoginId(String loginId) {
        Optional<User> findUser = userRepository.findByLoginId(loginId);

        return findUser.orElse(null);
    }

    @Transactional
    public List<UserResponse.followInfo> getAllUser() {
        List<User> allUser = userRepository.findAll();

        return toAllUserInfo(allUser);
    }

    //마이페이지 회원 조회
    @Transactional
    public UserResponse.userInfo getUserInfo(String loginId) {
        Optional<User> findUser = userRepository.findByLoginId(loginId);
        //포스트 리스트 조회
        List<Post> posts = postService.getPostsByUser(loginId);
        List<PostResponse> response = new ArrayList<>();

        for (Post post : posts) {
            boolean likedByUser = postService.isLikedByUser(post, findUser.get().getLoginId());
            double popularityScore = postService.calculatePopularity(post);
            PostResponse postResponse = new PostResponse(post, likedByUser, popularityScore);
            response.add(postResponse);
        }
        User user = findUser.get();
        return toUserInfo(user, response);
    }

    //다른 회원 페이지 조회
    @Transactional
    public UserResponse.userInfo getOtherUserInfo(String nickname) {
        Optional<User> findUser = userRepository.findByNickname(nickname);
        //포스트 리스트 조회

        List<Post> posts = postService.getPostsByOtherUser(nickname);
        List<PostResponse> response = new ArrayList<>();

        for (Post post : posts) {
            boolean likedByUser = postService.isLikedByUser(post, findUser.get().getLoginId());
            double popularityScore = postService.calculatePopularity(post);
            PostResponse postResponse = new PostResponse(post, likedByUser, popularityScore);
            response.add(postResponse);
        }

        User user = findUser.get();
        return toUserInfo(user, response);
    }

    //아이디 찾기
    @Transactional
    public String getUserId(String email) {
        Optional<User> findUser = userRepository.findByEmail(email);
        if (findUser.isPresent()) {
            User user = findUser.get();
            return user.getLoginId();
        }
        throw new NicknameNotFoundException("일치하는 닉네임이 없습니다.");
    }

    @Transactional
    public UserResponse.updateNickname updateNickname(String loginId, String updateNickname) {
        Optional<User> findUser = userRepository.findByLoginId(loginId);

        if (userRepository.findByNickname(updateNickname).isPresent()) {
            throw new UsernameNotFoundException("이미 존재하는 닉네임입니다.");
        }

        User user = findUser.get();
        String nickname = user.getNickname();
        user.setNickname(updateNickname);
        userFollowService.updateFollowingNickname(nickname, updateNickname);
        userFollowService.updateFollowerNickname(nickname, updateNickname);

        postService.updatePostByNickname(user, updateNickname);

        userChatRoomService.updateUserNicknameInChatRooms(nickname, updateNickname);
        chatMessageService.updateUserNicknameInMessages(nickname, updateNickname, loginId);

        userRepository.save(user);
        UserResponse.updateNickname userResponse = UserResponse.updateNickname.builder().nickname(nickname).build();

        return userResponse;
    }

    @Transactional
    public UserResponse.updateProfileImage updateProfileImage(String loginId, MultipartFile file) {
        User user = userRepository.findByLoginId(loginId).orElseThrow(() -> new UsernameNotFoundException("존재하는 아이디가 없습니다."));
        String profileImageUrl = fileUploadService.fileUpload(file);
        user.setProfileImageUrl(profileImageUrl);

        UserResponse.updateProfileImage userResponse = UserResponse.updateProfileImage.builder().profileImageUrl(profileImageUrl).build();

        userRepository.save(user);
        return userResponse;
    }

    //회원정보 변경
    @Transactional
    public UserResponse updateUserInfo(String loginId, UserRequest userRequest) {
        Optional<User> findUser = userRepository.findByLoginId(loginId);

        User user = findUser.get();
        user.setName(userRequest.getName());
        user.setDescription(userRequest.getDescription());
        user.setUpdateDate(LocalDateTime.now());

        userRepository.save(user);
        return fromUser(user);
    }

    //랜덤 비밀번호 생성
    @Transactional
    public String randomPassword(String loginId) {
        Optional<User> findUser = userRepository.findByLoginId(loginId);

        if (findUser.isEmpty()) {
            throw new UsernameNotFoundException("일치하는 유저가 없습니다.");
        }
        User user = findUser.get();

        String uuid = UUID.randomUUID().toString();

        user.setPassword(bCryptPasswordEncoder.encode(uuid));
        return uuid;
    }

    //비밀번호 변경
    @Transactional
    public void updateUserPassword(String loginId, String password) {
        Optional<User> findUser = userRepository.findByLoginId(loginId);

        if (findUser.isEmpty()) {
            throw new UsernameNotFoundException("일치하는 유저가 없습니다.");
        }

        User user = findUser.get();
        if (bCryptPasswordEncoder.matches(password, user.getPassword())) {
            throw new DuplicatePasswordException("기존 비밀번호와 동일합니다.");
        }
        user.setPassword(bCryptPasswordEncoder.encode(password));
        user.setUpdateDate(LocalDateTime.now());
        userRepository.save(user);
    }

    //회원 위치 정보 업데이트
    @Transactional
    public List<UserResponse.locateUserInfo> getUserByLocate(String loginId) {
        User findUser = userRepository.findByLoginId(loginId).orElseThrow(() -> new UsernameNotFoundException("일치하는 유저가 없습니다."));

        String locate = findUser.getLocate();
        double longitude =Double.parseDouble(locate.split(",")[0]);
        double latitude = Double.parseDouble(locate.split(",")[1]);

        List<User> usersWithinDistance = userRepository.findUsersWithinDistance(latitude, longitude);
        return toLocateUserInfo(usersWithinDistance);
    }

    @Transactional
    public void updateUser(User user){
        userRepository.save(user);
    }

    public UserResponse fromUser(User user) {
        return UserResponse.builder()
                .loginId(user.getLoginId())
                .location(user.getLocate())
                .name(user.getName())
                .nickname(user.getNickname())
                .profileImageUrl(user.getProfileImageUrl())
                .description(user.getDescription())
                .createDate(user.getCreateDate())
                .loginDate(user.getLoginDate())
                .build();
    }

    public UserResponse.userInfo toUserInfo(User user, List<PostResponse> response) {
        return UserResponse.userInfo.builder()
                .loginId(user.getLoginId())
                .name(user.getName())
                .nickname(user.getNickname())
                .profileImageUrl(user.getProfileImageUrl())
                .description(user.getDescription())
                .email(user.getEmail())
                .follower(String.valueOf(user.getFollowers().size()))
                .following(String.valueOf(user.getFollowings().size()))
                .posts(response)
                .build();
    }

    public List<UserResponse.followInfo> toAllUserInfo(List<User> users) {
        List<UserResponse.followInfo> allUserInfo = new ArrayList<>();

        for (User user : users) {
            UserResponse.followInfo userInfo = UserResponse.followInfo.builder()
                    .name(user.getName())
                    .nickname(user.getNickname())
                    .profileImageUrl(user.getProfileImageUrl())
                    .build();

            allUserInfo.add(userInfo);
        }

        return allUserInfo;
    }

    public List<UserResponse.locateUserInfo> toLocateUserInfo(List<User> users) {
        List<UserResponse.locateUserInfo> allUserInfo = new ArrayList<>();

        for (User user : users) {
            UserResponse.locateUserInfo userInfo = UserResponse.locateUserInfo.builder()
                    .name(user.getName())
                    .nickname(user.getNickname())
                    .profileImageUrl(user.getProfileImageUrl())
                    .locate(user.getLocate())
                    .build();

            allUserInfo.add(userInfo);
        }

        return allUserInfo;
    }
}
