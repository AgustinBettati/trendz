package facultad.trendz;

import facultad.trendz.model.ERole;
import facultad.trendz.model.Role;
import facultad.trendz.model.Topic;
import facultad.trendz.model.User;
import facultad.trendz.repository.RoleRepository;
import facultad.trendz.repository.TopicRepository;
import facultad.trendz.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

import java.util.Date;

@Component
class OnStartLoader implements ApplicationRunner {

    private final UserRepository userRepository;
    private final TopicRepository topicRepository;
    private final RoleRepository roleRepository;
    private final PasswordEncoder passwordEncoder;


    @Autowired
    public OnStartLoader(UserRepository userRepository, RoleRepository roleRepository, PasswordEncoder passwordEncoder, TopicRepository topicRepository) {
        this.userRepository = userRepository;
        this.roleRepository = roleRepository;
        this.passwordEncoder = passwordEncoder;
        this.topicRepository = topicRepository;
    }

    public void run(ApplicationArguments args) {
        roleRepository.save(new Role(ERole.ROLE_ADMIN, 1L));
        roleRepository.save(new Role(ERole.ROLE_USER, 2L));

        Role role = roleRepository.getByEnumRole(ERole.ROLE_ADMIN);
        userRepository.save(new User("agustinbettati@gmail.com", "AgustinBettati", passwordEncoder.encode("password"), role));
        userRepository.save(new User("marcoskhabie@gmail.com", "MarcosKhabie", passwordEncoder.encode("password"), role));
        userRepository.save(new User("gonzalodeachaval@gmail.com", "GonzaloDeAchaval", passwordEncoder.encode("password"), role));
        userRepository.save(new User("florvimberg@gmail.com", "FlorenciaVimberg", passwordEncoder.encode("password"), role));
        userRepository.save(new User("admin@gmail.com", "admin", passwordEncoder.encode("admin"), role));
        userRepository.save(new User("1@gmail.com", "1", passwordEncoder.encode("1"), role));
        userRepository.save(new User("2@gmail.com", "2", passwordEncoder.encode("2"), role));
        userRepository.save(new User("3@gmail.com", "3", passwordEncoder.encode("3"), role));
        topicRepository.save(new Topic("Humor", "Best humor posts of the internet", new Date()));
        topicRepository.save(new Topic("Humor", "Best humor posts of the internet", new Date()));
        topicRepository.save(new Topic("Humor", "Best humor posts of the internet", new Date()));
        topicRepository.save(new Topic("Humor", "Best humor posts of the internet", new Date()));
        topicRepository.save(new Topic("Humor", "Best humor posts of the internet", new Date()));
        topicRepository.save(new Topic("Humor", "Best humor posts of the internet", new Date()));
        topicRepository.save(new Topic("Humor", "Best humor posts of the internet", new Date()));
    }
}
