package facultad.trendz.dto.vote;

import javax.validation.constraints.NotNull;


public class VoteResponseDTO {


    @NotNull(message = "UserId cannot be empty")
    private Long userId;

    @NotNull(message = "postId cannot be empty")
    private Long postId;




    public VoteResponseDTO( Long userId,Long postId) {
        this.userId = userId;
        this.postId = postId;

    }

    public VoteResponseDTO(){}

    public Long getUserId() {
        return userId;
    }

    public void setUserId(Long userId) {
        this.userId = userId;
    }


    public Long getPostId() {
        return postId;
    }

    public void setPostId(Long postId) {
        this.postId = postId;
    }

}
