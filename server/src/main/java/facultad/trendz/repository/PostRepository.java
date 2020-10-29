package facultad.trendz.repository;

import facultad.trendz.model.Post;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import java.util.List;
import java.util.Optional;

@Repository
public interface PostRepository extends JpaRepository<Post, Long> {
    boolean existsByTitle(String title);
    List<Post> findByTitleIgnoreCaseContaining(String title);
    Optional<Post> findByIdAndDeletedIsFalse(Long id);
}
