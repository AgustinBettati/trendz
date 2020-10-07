package facultad.trendz.controller;

import facultad.trendz.config.model.MyUserDetails;
import facultad.trendz.dto.MessageResponseDTO;
import facultad.trendz.dto.comment.CommentCreateDTO;
import facultad.trendz.dto.comment.CommentResponseDTO;
import facultad.trendz.service.CommentService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.support.DefaultMessageSourceResolvable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.stream.Collectors;

@CrossOrigin(origins = "*")
@Controller
public class CommentController {

    private final CommentService commentService;

    @Autowired
    public CommentController(CommentService commentService) {
        this.commentService = commentService;
    }

    @PostMapping("/post/{postId}/comment")
    public ResponseEntity<Object> createComment(@Valid @RequestBody CommentCreateDTO comment, BindingResult bindingResult, @PathVariable Long postId, Authentication authentication) {
        if (bindingResult.hasErrors()) return getInvalidDTOResponse(bindingResult);

        MyUserDetails userDetails = (MyUserDetails) authentication.getPrincipal();

        final CommentResponseDTO body = commentService.saveComment(comment, postId, userDetails.getId());
        final HttpStatus status = HttpStatus.CREATED;
        return new ResponseEntity<>(body, status);
    }

    @PreAuthorize("hasRole('ROLE_ADMIN') or @commentService.commentAuthorVerification(#commentId,#authentication)")
    @PutMapping("/comment/{commentId}")
    public ResponseEntity<Object> editComment(@Valid @RequestBody CommentCreateDTO comment, BindingResult bindingResult, @PathVariable Long commentId, Authentication authentication) {
        if (bindingResult.hasErrors()) return getInvalidDTOResponse(bindingResult);

        final CommentResponseDTO body = commentService.editComment(commentId, comment);
        final HttpStatus status = HttpStatus.OK;
        return new ResponseEntity<>(body, status);
    }

    private ResponseEntity<Object> getInvalidDTOResponse(BindingResult bindingResult) {
        final HttpStatus status = HttpStatus.BAD_REQUEST;
        String error = bindingResult.getAllErrors().stream().map(DefaultMessageSourceResolvable::getDefaultMessage).collect(Collectors.joining(", "));
        MessageResponseDTO body = new MessageResponseDTO(error);
        return new ResponseEntity<>(body, status);
    }

}
