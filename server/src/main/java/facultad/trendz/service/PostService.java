package facultad.trendz.service;

import facultad.trendz.config.model.MyUserDetails;
import facultad.trendz.dto.comment.CommentResponseDTO;
import facultad.trendz.dto.post.*;
import facultad.trendz.exception.post.PostExistsException;
import facultad.trendz.exception.post.PostNotFoundException;
import facultad.trendz.model.Comment;
import facultad.trendz.model.Post;
import facultad.trendz.model.Vote;
import facultad.trendz.repository.PostRepository;
import facultad.trendz.repository.TopicRepository;
import facultad.trendz.repository.UserRepository;
import facultad.trendz.repository.VoteRepository;
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
    private final VoteRepository voteRepository;

    @Autowired
    public PostService(PostRepository postRepository, TopicRepository topicRepository, UserRepository userRepository, VoteRepository voteRepository) {
        this.postRepository = postRepository;
        this.topicRepository = topicRepository;
        this.userRepository = userRepository;
        this.voteRepository = voteRepository;
    }

    public SimplePostResponseDTO savePost(PostCreateDTO postCreateDTO, Long userId) {
        final Post post = new Post(postCreateDTO.getTitle(), postCreateDTO.getDescription(),
                postCreateDTO.getLink(), new Date(), topicRepository.getTopicById(postCreateDTO.getTopicId()), userRepository.findById(userId).get());
        postRepository.save(post);
        return new SimplePostResponseDTO(post.getId(),
                post.getTitle(),
                post.getDescription(),
                post.getLink(),
                post.getDate(),
                post.getTopic().getId(),
                userId,
                post.getUser().getUsername(),
                post.getTopic().getTitle(),
                post.isDeleted(),
                voteListToNumberList(this.voteRepository.findByPostIdAndIsUpvote(post.getId(), true)),
                voteListToNumberList(this.voteRepository.findByPostIdAndIsUpvote(post.getId(), false)));
    }

    public void validatePostTitle(String title) {
        if (postRepository.existsByTitle(title))
            throw new PostExistsException("Title " + title + " already in use");
    }

    public PostResponseDTO editPost(PostEditDTO postEdit, Long postId) {
        final Optional<Post> post = postRepository.findByIdAndDeletedIsFalse(postId);
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
                    editedPost.getUser().getUsername(),
                    editedPost.isDeleted(),
                    voteListToNumberList(
                            this.voteRepository.findByPostIdAndIsUpvote(postId, true)),
                    voteListToNumberList(this.voteRepository.findByPostIdAndIsUpvote(postId, false))
            );
        }).orElseThrow(PostNotFoundException::new);
    }

    public PostResponseDTO getPost(Long postId) {
        final Optional<Post> post = postRepository.findByIdAndDeletedIsFalse(postId);
        return post.map(foundPost -> new PostResponseDTO(postId,
                foundPost.getTitle(),
                foundPost.getDescription(),
                foundPost.getLink(),
                foundPost.getDate(),
                foundPost.getTopic().getId(),
                foundPost.getUser().getId(),
                commentListToDTO(foundPost.getComments()),
                foundPost.getUser().getUsername(),
                foundPost.isDeleted(),
                voteListToNumberList(this.voteRepository.findByPostIdAndIsUpvote(postId, true)),
                voteListToNumberList(this.voteRepository.findByPostIdAndIsUpvote(postId, false)))).orElseThrow(PostNotFoundException::new);
    }

    public boolean postAuthorVerification(Long postId, Authentication authentication) {
        final Optional<Post> post = postRepository.findByIdAndDeletedIsFalse(postId);
        if (!post.isPresent()) throw new PostNotFoundException();
        MyUserDetails myUserDetails = (MyUserDetails) authentication.getPrincipal();
        return post.get().getUser().getId().equals(myUserDetails.getId());

    }

    public SimplePostResponseDTO deletePost(Long postId) {
        return postRepository.findByIdAndDeletedIsFalse(
                postId).map(post -> {
            post.setDeleted(true);
            postRepository.save(post);
            return new SimplePostResponseDTO(postId,
                    post.getTitle(),
                    post.getDescription(),
                    post.getLink(),
                    post.getDate(),
                    post.getTopic().getId(),
                    post.getUser().getId(),
                    post.getUser().getUsername(),
                    post.getTopic().getTitle(),
                    post.isDeleted(),
                    voteListToNumberList(this.voteRepository.findByPostIdAndIsUpvote(post.getId(), true)),
                    voteListToNumberList(this.voteRepository.findByPostIdAndIsUpvote(post.getId(), false)));
        }).orElseThrow(PostNotFoundException::new);
    }

    public List<SimplePostResponseDTO> findPostByTitle(String title, int amount) {
        return postRepository.findByTitleIgnoreCaseContainingAndDeletedIsFalse(title)
                .stream()
                .sorted(Comparator.comparing(Post::getDate).reversed())
                .limit(amount)
                .map(post -> new SimplePostResponseDTO(post.getId(),
                                post.getTitle(),
                                post.getDescription(),
                                post.getLink(),
                                post.getDate(),
                                post.getTopic().getId(),
                                post.getUser().getId(),
                                post.getUser().getUsername(),
                                post.getTopic().getTitle(),
                                post.isDeleted(),
                        voteListToNumberList(this.voteRepository.findByPostIdAndIsUpvote(post.getId(), true)),
                        voteListToNumberList(this.voteRepository.findByPostIdAndIsUpvote(post.getId(), false))))
                .collect(Collectors.toList());
    }

    private List<CommentResponseDTO> commentListToDTO(List<Comment> comments) {
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


    private List<Long> voteListToNumberList(List<Vote> votes) {
        List longList = new ArrayList();
        for (int i = 0; i < votes.size(); i++) {
            longList.add(votes.get(i).getUser().getId());


        }
        return longList;
    }

    int getNumberOfUpvotes(Long postId) {
        return voteRepository.findByPostIdAndIsUpvote(postId, true).size();
    }

    int getNumberOfDownvotes(Long postId) {
        return voteRepository.findByPostIdAndIsUpvote(postId, false).size();
    }
}
