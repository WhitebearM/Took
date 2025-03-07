package isn_t_this_e_not_i.now_waypoint_core.domain.post.dto.response;

import isn_t_this_e_not_i.now_waypoint_core.domain.post.entity.Post;
import isn_t_this_e_not_i.now_waypoint_core.domain.post.entity.PostCategory;
import lombok.*;

import java.time.ZonedDateTime;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class PostResponse {

    private Long id;
    private String content;
    private Set<String> hashtags;
    private String locationTag;
    private PostCategory category;
    private List<String> mediaUrls;
    private String nickname;
    private String profileImageUrl;
    private ZonedDateTime createdAt;
    private int likeCount;
    private boolean likedByUser;
    private int viewCount;
    private double popularityScore;

    public PostResponse(Post post, boolean likedByUser, double popularityScore) {
        this.id = post.getId();
        this.content = post.getContent();
        this.hashtags = post.getHashtags().stream().map(hashtag -> hashtag.getName()).collect(Collectors.toSet());
        this.locationTag = post.getLocationTag();
        this.category = post.getCategory();
        this.mediaUrls = post.getMediaUrls();
        this.nickname = post.getUser().getNickname();
        this.profileImageUrl = post.getUser().getProfileImageUrl();
        this.createdAt = post.getCreatedAt();
        this.likeCount = post.getLikeCount();
        this.likedByUser = likedByUser;
        this.viewCount = post.getViewCount();
        this.popularityScore = popularityScore;
    }
}
