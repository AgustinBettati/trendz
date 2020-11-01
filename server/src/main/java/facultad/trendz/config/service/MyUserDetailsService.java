package facultad.trendz.config.service;

import facultad.trendz.config.model.MyUserDetails;
import facultad.trendz.model.User;
import facultad.trendz.repository.UserRepository;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

@Service
public class MyUserDetailsService implements UserDetailsService {

    private UserRepository userRepository;

    public MyUserDetailsService(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    @Override
    public UserDetails loadUserByUsername(String username){
        return MyUserDetails.build(userRepository.findByUsernameAndDeletedIsFalse(username)
                .orElseThrow(() -> new UsernameNotFoundException("User Not Found with username: " + username)));
    }

    public String getUsernameByEmail(String email) {
        return userRepository.findByEmailAndDeletedIsFalse(email).map(User::getUsername)
                .orElseThrow(() -> new UsernameNotFoundException("User Not Found with email: " + email));
    }
}
