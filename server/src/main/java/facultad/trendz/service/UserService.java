package facultad.trendz.service;

import facultad.trendz.dto.UserCreateDTO;
import facultad.trendz.model.User;
import facultad.trendz.exception.UserNotFoundException;
import facultad.trendz.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class UserService {
    private final UserRepository userRepository;

    @Autowired
    public UserService(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    public User getUserByEmail(String email) {
        final User user = userRepository.findByEmail(email);

        if (user == null) throw new UserNotFoundException();

        return user;
    }

  public List<User> getAll() {
    return userRepository.findAll();
  }

    public User saveUser(UserCreateDTO userCreateDTO) {
        final User user = new User(userCreateDTO.getEmail(), userCreateDTO.getName(), userCreateDTO.getSurname());
        return userRepository.save(user);
    }
}
