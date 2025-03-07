package isn_t_this_e_not_i.now_waypoint_core.domain.post.repository;

import isn_t_this_e_not_i.now_waypoint_core.domain.post.dto.response.PostResponseDTO;
import isn_t_this_e_not_i.now_waypoint_core.domain.post.entity.PostRedis;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface PostRedisRepository extends CrudRepository<PostRedis, String> {
    List<PostRedis> findPostRedisByNickname(String nickname);

    Optional<PostRedis> findById(String id);

    void deletePostRedisByNickname(String nickname);
}
