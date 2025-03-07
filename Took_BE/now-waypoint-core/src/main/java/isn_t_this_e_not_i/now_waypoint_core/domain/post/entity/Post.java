package isn_t_this_e_not_i.now_waypoint_core.domain.post.entity;

import isn_t_this_e_not_i.now_waypoint_core.domain.auth.user.User;
import isn_t_this_e_not_i.now_waypoint_core.domain.comment.entity.Comment;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import lombok.*;

import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

@Entity
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Post {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "post_id")
    private Long id;

    @NotNull
    private String content;

    @ManyToMany(fetch = FetchType.LAZY)
    @JoinTable(
            name = "post_hashtags",
            joinColumns = @JoinColumn(name = "post_id"),
            inverseJoinColumns = @JoinColumn(name = "hashtag_id")
    )
    private Set<Hashtag> hashtags = new HashSet<>();

    private String locationTag;

    @Enumerated(EnumType.STRING)
    private PostCategory category;

    @ElementCollection
    @CollectionTable(name = "post_media_urls", joinColumns = @JoinColumn(name = "post_id"))
    @Column(name = "media_url")
    private List<String> mediaUrls = new ArrayList<>();

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id")
    private User user;

    private ZonedDateTime createdAt;
    private ZonedDateTime updatedAt;

    private int likeCount;
    private int viewCount;

    @OneToMany(mappedBy = "post", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<Comment> comments = new ArrayList<>();

    @PrePersist
    public void prePersist() {
        this.createdAt = ZonedDateTime.now(ZoneId.of("Asia/Seoul"));
        this.updatedAt = ZonedDateTime.now(ZoneId.of("Asia/Seoul"));
        this.likeCount = 0;
        this.viewCount = 0;
    }

    @PreUpdate
    public void preUpdate() {
        this.updatedAt = ZonedDateTime.now(ZoneId.of("Asia/Seoul"));
    }

    public void incrementLikeCount() {
        this.likeCount++;
    }

    public void decrementLikeCount() {
        this.likeCount--;
    }

    public void incrementViewCount() {
        this.viewCount++;
    }

    public int getCommentCount() {
        return comments.size();
    }
}