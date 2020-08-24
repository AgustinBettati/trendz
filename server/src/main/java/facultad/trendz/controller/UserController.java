package facultad.trendz.controller;

import facultad.trendz.dto.UserCreateDTO;
import facultad.trendz.dto.UserResponseDTO;
import facultad.trendz.exception.UsernameExistsException;
import facultad.trendz.model.User;
import facultad.trendz.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.List;
import java.util.stream.Collectors;

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

    @GetMapping(value = "/user")
    public ResponseEntity<List<User>> getAll() {
      final List<User> result = userService.getAll();
      final HttpStatus status = HttpStatus.OK;

      return new ResponseEntity<>(result, status);
    }

    @PostMapping(value = "/user")
    public ResponseEntity<?> createUser(@Valid @RequestBody UserCreateDTO user,BindingResult bindingResult) throws UsernameExistsException {

        if (bindingResult.hasErrors()){
            final HttpStatus status = HttpStatus.BAD_REQUEST;
            String error = bindingResult.getAllErrors().stream().map(e -> e.getDefaultMessage()).collect(Collectors.joining(", "));
            return new ResponseEntity<>(error,status);
        }
        userService.validateEmail(user.getEmail());
        userService.validateUsername(user.getUsername());
        final UserResponseDTO body = userService.saveUser(user);
        final HttpStatus status = HttpStatus.CREATED;

        return new ResponseEntity<>(body, status);
    }
}
