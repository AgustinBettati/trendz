package facultad.trendz.repository;


import facultad.trendz.model.Vote;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.Optional;

public interface VoteRepository extends JpaRepository<Vote, Long> {



 @Query("from Vote v where v.post.id=:postId and v.user.id=:userId and v.isUpvote=:isUpvote")
 Optional<Vote> findByPostIdAndUserIdAndIsUpvote(@Param("userId")Long userId,@Param("postId") Long postId,@Param("isUpvote") boolean isUpvote);
}

