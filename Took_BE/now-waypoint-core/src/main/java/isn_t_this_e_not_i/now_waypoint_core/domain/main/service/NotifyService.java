package isn_t_this_e_not_i.now_waypoint_core.domain.main.service;

import isn_t_this_e_not_i.now_waypoint_core.domain.auth.repository.UserRepository;
import isn_t_this_e_not_i.now_waypoint_core.domain.auth.user.User;
import isn_t_this_e_not_i.now_waypoint_core.domain.main.dto.NotifyDTO;
import isn_t_this_e_not_i.now_waypoint_core.domain.main.entity.Notify;
import isn_t_this_e_not_i.now_waypoint_core.domain.main.repository.NotifyRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;

@Service
@RequiredArgsConstructor
@Slf4j
public class NotifyService {

    private final NotifyRepository notifyRepository;
    private final UserRepository userRepository;

    @Transactional
    public Notify save(Notify notify) {
        return notifyRepository.save(notify);
    }

    @Transactional
    public List<NotifyDTO> getUserNotify(String loginId){
        User user = userRepository.findByLoginId(loginId).orElseThrow(() -> new IllegalArgumentException("일치하는 유저가 없습니다."));
        List<Notify> userNotifyList = notifyRepository.findByReceiverNickname(user.getNickname());
        return getNotifyDTO(userNotifyList);
    }

    @Transactional
    public void deleteNotify(Long id) {
        notifyRepository.deleteById(id);
    }

    @Transactional
    public void deleteAll(String nickname){
        notifyRepository.deleteAllByReceiverNickname(nickname);
    }

    @Transactional
    public void deleteNotifyByPostId(Long postId){
        notifyRepository.deleteByPostId(postId);
    }

    @Transactional
    public Integer getReadFalseNotifyCountByUser(String loginId) {
        User user = userRepository.findByLoginId(loginId).orElseThrow(() -> new IllegalArgumentException("일치하는 유저가 없습니다."));
        List<Notify> aFalse = notifyRepository.findByReceiverNicknameAndIsRead(user.getNickname(), "false");

        return aFalse.size();
    }

    @Transactional
    public void changeReadStatusByUser(String loginId) {
        User user = userRepository.findByLoginId(loginId).orElseThrow(() -> new IllegalArgumentException("일치하는 유저가 없습니다."));
        List<Notify> aFalse = notifyRepository.findByReceiverNicknameAndIsRead(user.getNickname(), "false");

        for (Notify notify : aFalse) {
            notify.setIsRead("true");
        }
    }

    private List<NotifyDTO> getNotifyDTO(List<Notify> userNotifyList){
        List<NotifyDTO> NotifyDTOList = new ArrayList<>();
        for (Notify notify : userNotifyList) {
            NotifyDTO notifyDTO = NotifyDTO.builder()
                    .id(notify.getId())
                    .nickname(notify.getSenderNickname())
                    .message(notify.getMessage())
                    .profileImageUrl(notify.getProfileImageUrl())
                    .createDate(notify.getCreateDate())
                    .postId(notify.getPostId())
                    .mediaUrl(notify.getMediaUrl())
                    .comment(notify.getComment())
                    .build();
            NotifyDTOList.add(notifyDTO);
        }
        return NotifyDTOList;
    }
}
