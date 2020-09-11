package facultad.trendz.controller;

import facultad.trendz.dto.TopicCreateDTO;
import facultad.trendz.dto.TopicResponseDTO;
import facultad.trendz.service.TopicService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.support.DefaultMessageSourceResolvable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

import javax.validation.Valid;
import java.util.stream.Collectors;

@CrossOrigin(origins = "*")
@Controller
public class TopicController {

    private final TopicService topicService;

    @Autowired
    public TopicController(TopicService topicService) {
        this.topicService = topicService;
    }

    @PreAuthorize("hasRole('ROLE_ADMIN')")
    @PostMapping("/topic")
    public ResponseEntity<Object> createTopic(@Valid @RequestBody TopicCreateDTO topic, BindingResult bindingResult){
        if (bindingResult.hasErrors()) {
            final HttpStatus status = HttpStatus.BAD_REQUEST;
            String error = bindingResult.getAllErrors().stream().map(DefaultMessageSourceResolvable::getDefaultMessage).collect(Collectors.joining(", "));
            return new ResponseEntity<>(error, status);
        }
        final TopicResponseDTO body = topicService.saveTopic(topic);
        final HttpStatus status = HttpStatus.CREATED;
        return new ResponseEntity<>(body, status);
    }
}
