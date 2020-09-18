package facultad.trendz.controller;

import facultad.trendz.dto.PostCreateDTO;
import facultad.trendz.dto.PostResponseDTO;
import facultad.trendz.dto.TopicResponseDTO;
import facultad.trendz.service.PostService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.support.DefaultMessageSourceResolvable;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
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
public class PostController {

    private final PostService postService;

    @Autowired
    public PostController(PostService postService) {
        this.postService = postService;
    }

    @PostMapping("/post")
    public ResponseEntity<Object> createPost(@Valid @RequestBody PostCreateDTO post, BindingResult bindingResult){
        if (bindingResult.hasErrors()) {
            final HttpStatus status = HttpStatus.BAD_REQUEST;
            String error = bindingResult.getAllErrors().stream().map(DefaultMessageSourceResolvable::getDefaultMessage).collect(Collectors.joining(", "));
            return new ResponseEntity<>(error, status);
        }
        postService.validatePostTitle(post.getTitle());
        final PostResponseDTO body = postService.savePost(post);
        final HttpStatus status = HttpStatus.CREATED;
        return new ResponseEntity<>(body, status);
    }
}

