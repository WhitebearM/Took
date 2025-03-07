package isn_t_this_e_not_i.now_waypoint_core.domain.main.dto;

import lombok.*;

import java.time.ZonedDateTime;

@Getter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class NotifyDTO {
    private Long id;
    private String nickname;
    @Setter
    private String message;
    private String profileImageUrl;
    private ZonedDateTime createDate;
    private Long postId;
    private String mediaUrl;
    private String comment;
}
