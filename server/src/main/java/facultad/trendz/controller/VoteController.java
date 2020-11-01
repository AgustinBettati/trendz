package facultad.trendz.controller;

import facultad.trendz.config.model.MyUserDetails;
import facultad.trendz.dto.MessageResponseDTO;
import facultad.trendz.dto.comment.CommentCreateDTO;
import facultad.trendz.dto.comment.CommentResponseDTO;
import facultad.trendz.dto.vote.VoteResponseDTO;
import facultad.trendz.service.CommentService;
import facultad.trendz.service.VoteService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;

@CrossOrigin(origins = "*")
@Controller
public class VoteController implements ControllerUtils{

    private final VoteService voteService;

    @Autowired
    public VoteController(VoteService voteService) {
        this.voteService = voteService;
    }

    @PostMapping("/post/{postId}/upvote")
    public ResponseEntity<Object> upvote(  @PathVariable Long postId, Authentication authentication) {


        MyUserDetails userDetails = (MyUserDetails) authentication.getPrincipal();

        final VoteResponseDTO body = this.voteService.saveVote(postId,userDetails.getId(),true);
        final HttpStatus status = HttpStatus.CREATED;
        return new ResponseEntity<>(body, status);
    }

    @PostMapping("/post/{postId}/downvote")
    public ResponseEntity<Object> downvote( @PathVariable Long postId, Authentication authentication) {

        MyUserDetails userDetails = (MyUserDetails) authentication.getPrincipal();

        final VoteResponseDTO body = this.voteService.saveVote(postId,userDetails.getId(),false);
        final HttpStatus status = HttpStatus.CREATED;
        return new ResponseEntity<>(body, status);
    }



}

