package facultad.trendz.repository;

import facultad.trendz.model.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface UserRepository extends JpaRepository<User, Long> {
    User findByEmail(String email);
    boolean existsByUsername(String username);

    boolean existsByEmail(String email);

    Optional<User> findByUsernameAndDeletedIsFalse(String username);

    Optional<User> findByEmailAndDeletedIsFalse(String email);

    Optional<User> findByIdAndDeletedIsFalse(Long id);
}
