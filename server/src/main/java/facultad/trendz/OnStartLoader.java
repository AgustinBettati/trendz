package facultad.trendz;

import facultad.trendz.model.User;
import facultad.trendz.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.stereotype.Component;

@Component
class DataLoader implements ApplicationRunner {

  private final UserRepository userRepository;

  @Autowired
  public DataLoader(UserRepository userRepository) {
    this.userRepository = userRepository;
  }

  public void run(ApplicationArguments args) {
    userRepository.save(new User("agustinbettati@gmail.com", "Agustin", "Bettati"));
    userRepository.save(new User("marcoskhabie@gmail.com", "Marcos", "Khabie"));
    userRepository.save(new User("gonzalodeachaval@gmail.com", "Gonzalo", "De Achaval"));
    userRepository.save(new User("florvimberg@gmail.com", "Florencia", "Vimberg"));
  }
}
