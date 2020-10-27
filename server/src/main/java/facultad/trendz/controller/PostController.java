package facultad.trendz.controller;

import facultad.trendz.config.model.MyUserDetails;
import facultad.trendz.dto.MessageResponseDTO;
import facultad.trendz.dto.post.*;
import facultad.trendz.dto.vote.VoteResponseDTO;
import facultad.trendz.service.PostService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;

@CrossOrigin(origins = "*")
@Controller
public class PostController implements ControllerUtils {

    private final PostService postService;

    @Autowired
    public PostController(PostService postService) {
        this.postService = postService;
    }

    @PostMapping("/post")
    public ResponseEntity<Object> createPost(@Valid @RequestBody PostCreateDTO post, BindingResult bindingResult, Authentication authentication) {

        if (bindingResult.hasErrors()) return getInvalidDTOResponse(bindingResult);

        MyUserDetails userDetails = (MyUserDetails)authentication.getPrincipal();

        postService.validatePostTitle(post.getTitle());
        final SimplePostResponseDTO body = postService.savePost(post,userDetails.getId());
        final HttpStatus status = HttpStatus.CREATED;
        return new ResponseEntity<>(body, status);
    }

    @PreAuthorize("hasRole('ROLE_ADMIN') or @postService.postAuthorVerification(#postId,#authentication)")
    @PutMapping("/post/{postId}")
    public ResponseEntity<PostResponseDTO> editPost(@Valid @RequestBody PostEditDTO postEdit, @PathVariable Long postId,Authentication authentication) {
        final PostResponseDTO body = postService.editPost(postEdit, postId);
        final HttpStatus status = HttpStatus.OK;
        return new ResponseEntity<>(body,status);
    }

    @GetMapping("/post/{postId}")
    public ResponseEntity<PostResponseDTO> getPost(@PathVariable Long postId) {
        final PostResponseDTO body = postService.getPost(postId);
        final HttpStatus status = HttpStatus.OK;
        return new ResponseEntity<>(body,status);
    }

    @PreAuthorize("hasRole('ROLE_ADMIN') or @postService.postAuthorVerification(#postId,#authentication)")
    @DeleteMapping("/post/{postId}")
    public ResponseEntity<MessageResponseDTO> deletePost(Authentication authentication, @PathVariable Long postId){
        postService.deletePost(postId);
        final MessageResponseDTO body = new MessageResponseDTO("Successfully deleted Post");
        final HttpStatus status = HttpStatus.OK;
        return new ResponseEntity<>(body,status);
    }



}

