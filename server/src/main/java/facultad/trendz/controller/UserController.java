package facultad.trendz.controller;

import facultad.trendz.dto.UserCreateDTO;
import facultad.trendz.model.User;
import facultad.trendz.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

@Controller
public class UserController {
    private final UserService userService;

    @Autowired
    public UserController(UserService userService) {
        this.userService = userService;
    }

    @GetMapping(value = "/user/{email}")
    public ResponseEntity<User> getUser(@PathVariable("email") String email) {
        final User body = userService.getUserByEmail(email);
        final HttpStatus status = HttpStatus.OK;

        return new ResponseEntity<>(body, status);
    }

    @PostMapping(value = "/user")
    public ResponseEntity<User> createUser(@RequestBody UserCreateDTO user) {
        final User body = userService.saveUser(user);
        final HttpStatus status = HttpStatus.CREATED;

        return new ResponseEntity<>(body, status);
    }
}
