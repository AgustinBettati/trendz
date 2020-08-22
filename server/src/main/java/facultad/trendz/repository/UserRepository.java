package facultad.trendz.repository;

import facultad.trendz.model.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface UserRepository extends JpaRepository<User, Long> {
    User findByEmail(String email);

    boolean existsByUsername(String username);

    boolean existsByEmail(String username);
}
