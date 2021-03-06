package facultad.trendz.service;

import facultad.trendz.config.model.MyUserDetails;
import facultad.trendz.dto.MessageResponseDTO;
import facultad.trendz.dto.comment.CommentCreateDTO;
import facultad.trendz.dto.comment.CommentResponseDTO;
import facultad.trendz.dto.vote.VoteResponseDTO;
import facultad.trendz.exception.comment.CommentNotFoundException;
import facultad.trendz.exception.post.PostNotFoundException;
import facultad.trendz.exception.user.UserNotFoundException;
import facultad.trendz.exception.vote.VoteNotFoundException;
import facultad.trendz.model.Comment;
import facultad.trendz.model.Post;
import facultad.trendz.model.User;
import facultad.trendz.model.Vote;
import facultad.trendz.repository.CommentRepository;
import facultad.trendz.repository.PostRepository;
import facultad.trendz.repository.UserRepository;
import facultad.trendz.repository.VoteRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Service;

import javax.persistence.PreRemove;
import java.util.Date;
import java.util.Optional;

@Service
public class VoteService {

    private final VoteRepository voteRepository;
    private final UserRepository userRepository;
    private final PostRepository postRepository;

    @Autowired
    public VoteService(VoteRepository voteRepository, UserRepository userRepository, PostRepository postRepository) {
        this.voteRepository = voteRepository;
        this.userRepository = userRepository;
        this.postRepository = postRepository;
    }

    public  VoteResponseDTO saveVote(Long postId, Long userId, Boolean isUpvote) {
        User user = userRepository.findById(userId).orElseThrow(UserNotFoundException::new);
        Post post=postRepository.findById(postId).orElseThrow(PostNotFoundException::new);
        Optional<Vote> oppositeVote=voteRepository.findByPostIdAndUserIdAndIsUpvote(userId,postId,!(isUpvote));
        if (oppositeVote.isPresent()){
            oppositeVote.get().setUpvote(isUpvote);
            voteRepository.save(oppositeVote.get());
        }
        if(!voteRepository.findByPostIdAndUserIdAndIsUpvote(userId,postId,isUpvote).isPresent()) {
            Vote vote = new Vote(post, user, isUpvote);
            voteRepository.save(vote);
        }

        return new VoteResponseDTO(user.getId());
    }

    int getNumberOfUpvotes(Long postId){
       return voteRepository.findByPostIdAndIsUpvote(postId,true).size();
    }

    int getNumberOfDownvotes(Long postId){
        return voteRepository.findByPostIdAndIsUpvote(postId,false).size();
    }


    @PreRemove
    public MessageResponseDTO deleteVote(Long postId,Long userId) {
        Optional<Vote> vote = voteRepository.findByPostIdAndUserId(postId,userId);
        if(vote.isPresent()){
             Post post=vote.get().getPost();
             post.getVotes().remove(vote.get());
             User user= vote.get().getUser();
             user.getVotes().remove(vote.get());
       voteRepository.delete(vote.get());
            return new MessageResponseDTO("Vote deleted successfully");
        }
        throw new VoteNotFoundException();
    }

}

