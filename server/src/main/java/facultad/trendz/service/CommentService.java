package facultad.trendz.service;

import facultad.trendz.dto.comment.CommentCreateDTO;
import facultad.trendz.dto.comment.CommentResponseDTO;
import facultad.trendz.exception.post.PostNotFoundException;
import facultad.trendz.exception.user.UserNotFoundException;
import facultad.trendz.model.Comment;
import facultad.trendz.model.User;
import facultad.trendz.repository.CommentRepository;
import facultad.trendz.repository.PostRepository;
import facultad.trendz.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Date;

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

        return new CommentResponseDTO(comment.getId(), user.getUsername(), postId, comment.getContent(), comment.getDate(), comment.getEditDate());
    }
}
