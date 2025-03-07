package isn_t_this_e_not_i.now_waypoint_core.domain.main.controller;

import isn_t_this_e_not_i.now_waypoint_core.domain.main.dto.NotifyDTO;
import isn_t_this_e_not_i.now_waypoint_core.domain.main.service.NotifyService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.security.Principal;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/notify")
@RequiredArgsConstructor
public class NotifyController {

    private final NotifyService notifyService;

    @GetMapping
    public ResponseEntity<List<NotifyDTO>> getNotify(Principal principal){
        return ResponseEntity.ok().body(notifyService.getUserNotify(principal.getName()));
    }

    @GetMapping("/read")
    public ResponseEntity<Integer> getReadFalseNotifyCount(Principal principal){
        return ResponseEntity.ok().body(notifyService.getReadFalseNotifyCountByUser(principal.getName()));
    }

    @GetMapping("/changeRead")
    public ResponseEntity<String> changeReadStatus(Principal principal) {
        notifyService.changeReadStatusByUser(principal.getName());
        return ResponseEntity.ok("읽음 상태가 변경되었습니다.");
    }

    @DeleteMapping
    public void deleteNotify(@RequestBody Map<String, Long> request){
        Long id = request.get("id");
        notifyService.deleteNotify(id);
    }

    @DeleteMapping("/all")
    public void deleteAllNotification(@RequestBody Map<String, String> request){
        String nickname = request.get("nickname");
        notifyService.deleteAll(nickname);
    }
}
