package facultad.trendz.service;

import facultad.trendz.dto.post.SimplePostResponseDTO;
import facultad.trendz.dto.user.ProfileEditDTO;
import facultad.trendz.dto.user.UserCreateDTO;
import facultad.trendz.dto.user.UserResponseDTO;
import facultad.trendz.exception.user.EmailExistsException;
import facultad.trendz.exception.user.IncorrectPasswordException;
import facultad.trendz.exception.user.UsernameExistsException;
import facultad.trendz.model.ERole;
import facultad.trendz.model.Post;
import facultad.trendz.model.Role;
import facultad.trendz.model.User;
import facultad.trendz.exception.user.UserNotFoundException;
import facultad.trendz.repository.RoleRepository;
import facultad.trendz.repository.UserRepository;
import facultad.trendz.repository.VoteRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.Comparator;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class UserService {
    private final UserRepository userRepository;
    private final RoleRepository roleRepository;
    private final PasswordEncoder passwordEncoder;
    private final VoteRepository voteRepository;

    @Autowired
    public UserService(UserRepository userRepository, RoleRepository roleRepository, PasswordEncoder passwordEncoder, VoteRepository voteRepository) {
        this.userRepository = userRepository;
        this.roleRepository = roleRepository;
        this.passwordEncoder = passwordEncoder;
        this.voteRepository = voteRepository;
    }

    public User getUserByEmail(String email) {
        return userRepository.findByEmailAndDeletedIsFalse(email).orElseThrow(UserNotFoundException::new);
    }

    public List<User> getAll() {
        return userRepository.findAll();
    }

    public UserResponseDTO saveUser(UserCreateDTO userCreateDTO) {
        ERole eRole = (userCreateDTO.getRole().equals("admin")) ? ERole.ROLE_ADMIN : ERole.ROLE_USER;
        Role role = roleRepository.getByEnumRole(eRole);

        String encryptedPassword = passwordEncoder.encode(userCreateDTO.getPassword());

        final User user = new User(userCreateDTO.getEmail(), userCreateDTO.getUsername(), encryptedPassword, role);

        userRepository.save(user);
        return new UserResponseDTO(user.getId(), user.getEmail(), user.getUsername(), user.getRole(),user.isDeleted());
    }

    public UserResponseDTO deleteUser(Long id){
        return userRepository.findById(id).map(user -> {
                    user.setDeleted(true);
                    userRepository.save(user);
                    return new UserResponseDTO(user.getId(), user.getEmail(), user.getUsername(), user.getRole(), user.isDeleted());
                }
        ).orElseThrow(UserNotFoundException::new);
    }

    public void validateUsername(String username) {
        if (userRepository.existsByUsername(username))
            throw new UsernameExistsException("Username " + username + " already taken");
    }

    public void validateEmail(String email) {
        if (userRepository.existsByEmail(email))
            throw new EmailExistsException("Email " + email + " already taken");
    }

    public UserResponseDTO getUserById(Long userId) {
        final Optional<User> user = userRepository.findByIdAndDeletedIsFalse(userId);

        if (!user.isPresent()) throw new UserNotFoundException();

        return new UserResponseDTO(user.get().getId(), user.get().getEmail(), user.get().getUsername(), user.get().getRole(), user.get().isDeleted());
    }

    public void editUser(ProfileEditDTO profileEditDTO, Long userId) {
        final Optional<User> user = userRepository.findByIdAndDeletedIsFalse(userId);
        if (!user.isPresent()) throw new UserNotFoundException();

        String oldPassword = profileEditDTO.getOldPassword();
        String newPassword = profileEditDTO.getNewPassword();

        if (oldPassword != null){
            if (passwordEncoder.matches(oldPassword, user.get().getPassword())) {
                if (newPassword != null)
                    user.get().setPassword(passwordEncoder.encode(newPassword));
            } else throw new IncorrectPasswordException();
        }

        String newUsername = profileEditDTO.getUsername();

        if (newUsername != null){
            if(!userRepository.existsByUsername(newUsername)) {
                user.get().setUsername(newUsername);
            } else throw new UsernameExistsException(String.format("Username %s already taken",newUsername));
        }

        userRepository.save(user.get());
    }

    public List<SimplePostResponseDTO> getLastPosts(Long id, int limit) {
        return userRepository.findById(id).map(user ->
            user.getPosts().stream()
                    .filter(post -> !post.isDeleted())
                    .sorted(Comparator.comparing(Post::getDate).reversed())
                    .limit(limit)
                    .map(post -> new SimplePostResponseDTO(post.getId(),
                            post.getTitle(),
                            post.getDescription(),
                            post.getLink(),
                            post.getDate(),
                            post.getTopic().getId(),
                            post.getUser().getId(),
                            post.getUser().getUsername(),
                            post.getTopic().getTitle(),
                            post.isDeleted(),
                            voteRepository.findByPostIdAndIsUpvote(post.getId(),true).stream().map(vote -> vote.getUser().getId()).collect(Collectors.toList()),
                            voteRepository.findByPostIdAndIsUpvote(post.getId(),false).stream().map(vote -> vote.getUser().getId()).collect(Collectors.toList())))
                    .collect(Collectors.toList())
        ).orElseThrow(UserNotFoundException::new);
    }
}
