package facultad.trendz.service;

import facultad.trendz.config.model.MyUserDetails;
import facultad.trendz.dto.comment.CommentResponseDTO;
import facultad.trendz.dto.post.PostCreateDTO;
import facultad.trendz.dto.post.PostEditDTO;
import facultad.trendz.dto.post.PostResponseDTO;
import facultad.trendz.dto.post.SimplePostResponseDTO;
import facultad.trendz.exception.post.PostExistsException;
import facultad.trendz.exception.post.PostNotFoundException;
import facultad.trendz.model.Comment;
import facultad.trendz.model.Post;
import facultad.trendz.repository.PostRepository;
import facultad.trendz.repository.TopicRepository;
import facultad.trendz.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Service;

import java.util.*;
import java.util.stream.Collectors;

@Service
public class PostService {

    private final PostRepository postRepository;
    private final TopicRepository topicRepository;
    private final UserRepository userRepository;

    @Autowired
    public PostService(PostRepository postRepository, TopicRepository topicRepository, UserRepository userRepository) {
        this.postRepository = postRepository;
        this.topicRepository = topicRepository;
        this.userRepository=userRepository;
    }

    public SimplePostResponseDTO savePost(PostCreateDTO postCreateDTO, Long userId) {
        final Post post = new Post(postCreateDTO.getTitle(), postCreateDTO.getDescription(),
                postCreateDTO.getLink(), new Date(), topicRepository.getTopicById(postCreateDTO.getTopicId()),userRepository.findById(userId).get());
        postRepository.save(post);
        return new SimplePostResponseDTO(post.getId(),
                post.getTitle(),
                post.getDescription(),
                post.getLink(),
                post.getDate(),
                post.getTopic().getId(),
                userId,
                post.getUser().getUsername());
    }

    public void validatePostTitle(String title) {
        if (postRepository.existsByTitle(title))
            throw new PostExistsException("Title " + title + " already in use");
    }

    public PostResponseDTO editPost(PostEditDTO postEdit, Long postId) {
        final Optional<Post> post = postRepository.findById(postId);
        return post.map(post1 -> {

            String newTitle = postEdit.getTitle();
            String newDescription = postEdit.getDescription();
            String newLink = postEdit.getLink();

            if (!newTitle.equals(post1.getTitle())) {
                if (postRepository.existsByTitle(newTitle)) {
                    throw new PostExistsException(String.format("Title %s already in use", newTitle));
                } else post1.setTitle(newTitle);
            }

            if (newDescription != null) post1.setDescription(newDescription);

            if (newLink != null) post1.setLink(newLink);

            Post editedPost = postRepository.save(post1);

            return new PostResponseDTO(editedPost.getId(),
                    editedPost.getTitle(),
                    editedPost.getDescription(),
                    editedPost.getLink(),
                    editedPost.getDate(),
                    editedPost.getTopic().getId(),
                    editedPost.getUser().getId(),
                    commentListToDTO(editedPost.getComments()),
                    editedPost.getUser().getUsername());
        }).orElseThrow(PostNotFoundException::new);
    }

    public PostResponseDTO getPost(Long postId){
        final Optional<Post> post= postRepository.findById(postId);
        return post.map(foundPost -> new PostResponseDTO(postId,
                foundPost.getTitle(),
                foundPost.getDescription(),
                foundPost.getLink(),
                foundPost.getDate(),
                foundPost.getTopic().getId(),
                foundPost.getUser().getId(),
                commentListToDTO(foundPost.getComments()),
                foundPost.getUser().getUsername())).orElseThrow(PostNotFoundException::new);
    }

    public boolean postAuthorVerification(Long postId, Authentication authentication){
        final Optional<Post> post = postRepository.findById(postId);
        if (!post.isPresent()) throw new PostNotFoundException();
        MyUserDetails myUserDetails = (MyUserDetails) authentication.getPrincipal();
        return post.get().getUser().getId().equals(myUserDetails.getId());

    }

    public void deletePost(Long postId) {
        postRepository.delete(postRepository.findById(postId).orElseThrow(PostNotFoundException::new));
    }

    public List<SimplePostResponseDTO> findPostByTitle(String title){
        return postRepository.findByTitleIgnoreCaseContaining(title)
                .stream()
                .sorted(Comparator.comparing(Post::getDate).reversed())
                .map(post -> new SimplePostResponseDTO(post.getId(),
                        post.getTitle(),
                        post.getDescription(),
                        post.getLink(),
                        post.getDate(),
                        post.getTopic().getId(),
                        post.getUser().getId(),
                        post.getUser().getUsername()))
                .collect(Collectors.toList());
    }

    private List<CommentResponseDTO> commentListToDTO(List<Comment> comments){
        return comments.stream()
                .filter(comment -> !comment.isDeleted())
                .sorted(Comparator.comparing(Comment::getDate).reversed())
                .map(comment -> new CommentResponseDTO(comment.getId(),
                        comment.getUser().getUsername(),
                        comment.getPost().getId(),
                        comment.getContent(),
                        comment.getDate(),
                        comment.getEditDate(),
                        comment.isDeleted(),
                        comment.getUser().getId()))
                .collect(Collectors.toList());
    }
}
