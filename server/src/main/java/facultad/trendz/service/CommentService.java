package facultad.trendz.service;

import facultad.trendz.config.model.MyUserDetails;
import facultad.trendz.dto.MessageResponseDTO;
import facultad.trendz.dto.comment.CommentCreateDTO;
import facultad.trendz.dto.comment.CommentResponseDTO;
import facultad.trendz.exception.comment.CommentNotFoundException;
import facultad.trendz.exception.post.PostNotFoundException;
import facultad.trendz.exception.user.UserNotFoundException;
import facultad.trendz.model.Comment;
import facultad.trendz.model.User;
import facultad.trendz.repository.CommentRepository;
import facultad.trendz.repository.PostRepository;
import facultad.trendz.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.Optional;

@Service
public class CommentService {

    private final CommentRepository commentRepository;
    private final UserRepository userRepository;
    private final PostRepository postRepository;

    @Autowired
    public CommentService(CommentRepository commentRepository, UserRepository userRepository, PostRepository postRepository) {
        this.commentRepository = commentRepository;
        this.userRepository = userRepository;
        this.postRepository = postRepository;
    }

    public CommentResponseDTO saveComment(CommentCreateDTO commentDTO, Long postId, Long userId) {
        User user = userRepository.findById(userId).orElseThrow(UserNotFoundException::new);
        Comment comment = new Comment(
                commentDTO.getContent(),
                new Date(),
                user,
                postRepository.findById(postId).orElseThrow(PostNotFoundException::new)
                );
        commentRepository.save(comment);

        return new CommentResponseDTO(comment.getId(), user.getUsername(), postId, comment.getContent(), comment.getDate(), comment.getEditDate(), comment.isDeleted(), comment.getUser().getId());
    }

    public boolean commentAuthorVerification(Long commentId, Authentication authentication) {
        final Optional<Comment> comment = commentRepository.findById(commentId);
        return comment.map(comment1 -> {
            MyUserDetails myUserDetails = (MyUserDetails) authentication.getPrincipal();
            return comment1.getUser().getId().equals(myUserDetails.getId());
        }).orElseThrow(CommentNotFoundException::new);
    }

    public CommentResponseDTO editComment(Long commentId, CommentCreateDTO commentEdit) {
        final Optional<Comment> comment = commentRepository.findById(commentId);
        return comment.map(foundComment -> {
            foundComment.setContent(commentEdit.getContent());
            foundComment.setEditDate(new Date());
            commentRepository.save(foundComment);
            return new CommentResponseDTO(foundComment.getId(),
                    foundComment.getUser().getUsername(),
                    foundComment.getPost().getId(),
                    foundComment.getContent(),
                    foundComment.getDate(),
                    foundComment.getEditDate(),
                    foundComment.isDeleted(),
                    foundComment.getUser().getId());
        }).orElseThrow(CommentNotFoundException::new);
    }

    public MessageResponseDTO deleteComment(Long commentId) {
        Optional<Comment> comment = commentRepository.findById(commentId);
        return comment.map(foundComment -> {
            foundComment.setDeleted(true);
            commentRepository.save(foundComment);
            return new MessageResponseDTO("Comment deleted successfully");
        }).orElseThrow(CommentNotFoundException::new);
    }
}
