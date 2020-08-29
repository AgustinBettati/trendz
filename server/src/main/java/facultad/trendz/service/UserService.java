package facultad.trendz.service;

import facultad.trendz.dto.UserCreateDTO;
import facultad.trendz.dto.UserResponseDTO;
import facultad.trendz.exception.EmailExistsException;
import facultad.trendz.exception.UsernameExistsException;
import facultad.trendz.model.ERole;
import facultad.trendz.model.Role;
import facultad.trendz.model.User;
import facultad.trendz.exception.UserNotFoundException;
import facultad.trendz.repository.RoleRepository;
import facultad.trendz.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class UserService {
    private final UserRepository userRepository;
    private final RoleRepository roleRepository;
    private final PasswordEncoder passwordEncoder;

    @Autowired
    public UserService(UserRepository userRepository, RoleRepository roleRepository, PasswordEncoder passwordEncoder) {
        this.userRepository = userRepository;
        this.roleRepository = roleRepository;
        this.passwordEncoder = passwordEncoder;
    }

    public User getUserByEmail(String email) {
        final User user = userRepository.findByEmail(email);

        if (user == null) throw new UserNotFoundException();

        return user;
    }

    public List<User> getAll() {
        return userRepository.findAll();
    }

    public UserResponseDTO saveUser(UserCreateDTO userCreateDTO) {
        ERole eRole = (userCreateDTO.getRole().equals("admin")) ? ERole.ROLE_ADMIN : ERole.ROLE_USER;
        Role role = roleRepository.getByRole(eRole);

        String encryptedPassword = passwordEncoder.encode(userCreateDTO.getPassword());

        final User user = new User(userCreateDTO.getEmail(), userCreateDTO.getUsername(), encryptedPassword, role);

        userRepository.save(user);
        return new UserResponseDTO(user.getId(), user.getEmail(), user.getUsername(), user.getRole());
    }

    public void validateUsername(String username) throws UsernameExistsException {
        if (userRepository.existsByUsername(username))
            throw new UsernameExistsException("Username " + username + " already taken");
    }

    public void validateEmail(String email) throws EmailExistsException {
        if (userRepository.existsByEmail(email))
            throw new EmailExistsException("Email " + email + " already taken");
    }

    public UserResponseDTO getUserById(Long userId) {
        final Optional<User> user = userRepository.findById(userId);

        if (!user.isPresent()) throw new UserNotFoundException();

        return new UserResponseDTO(user.get().getId(), user.get().getEmail(), user.get().getUsername(), user.get().getRole());
    }
}
