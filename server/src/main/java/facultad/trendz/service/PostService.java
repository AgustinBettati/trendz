package facultad.trendz.service;

import facultad.trendz.config.model.MyUserDetails;
import facultad.trendz.dto.post.PostCreateDTO;
import facultad.trendz.dto.post.PostEditDTO;
import facultad.trendz.dto.post.PostGetDTO;
import facultad.trendz.dto.post.PostResponseDTO;
import facultad.trendz.exception.post.PostExistsException;
import facultad.trendz.exception.post.PostNotFoundException;
import facultad.trendz.model.Post;
import facultad.trendz.repository.PostRepository;
import facultad.trendz.repository.TopicRepository;
import facultad.trendz.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.Optional;

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

    public PostResponseDTO savePost(PostCreateDTO postCreateDTO, Long userId) {
        final Post post = new Post(postCreateDTO.getTitle(), postCreateDTO.getDescription(),
                postCreateDTO.getLink(), new Date(), topicRepository.getTopicById(postCreateDTO.getTopicId()),userRepository.findById(userId).get());
        postRepository.save(post);
        return new PostResponseDTO(post.getId(), post.getTitle(), post.getDescription(), post.getLink(), post.getDate(), post.getTopic().getId(),userId);
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
                    editedPost.getUser().getId());
        }).orElseThrow(PostNotFoundException::new);
    }

    public PostGetDTO getPost(Long postId){
        final Optional<Post> post= postRepository.findById(postId);
        if (!post.isPresent()) throw new PostNotFoundException();

        return new PostGetDTO(post.get().getTopic().getId(),
                post.get().getTitle(),
                post.get().getDescription(),
                post.get().getLink(),
                post.get().getDate(),
                post.get().getUser().getId(),
                post.get().getUser().getUsername()
        );
    }

    public Long getPostAuthor(Long postId){
        final Optional<Post> post= postRepository.findById(postId);
        return post.map(post1 ->
                post1.getUser().getId()
        ).orElseThrow(PostNotFoundException::new);
    }

    public boolean postAuthorVerification(Long postId, Authentication authentication){
        final Optional<Post> post= postRepository.findById(postId);
        if (post==null){ throw  new PostNotFoundException();}
        MyUserDetails myUserDetails=(MyUserDetails)authentication.getPrincipal();
        if(post.get().getUser().getId()==myUserDetails.getId()) return true;
        return false;

    }


    public void deletePost(Long postId) {
        postRepository.delete(postRepository.findById(postId).orElseThrow(PostNotFoundException::new));
    }
}