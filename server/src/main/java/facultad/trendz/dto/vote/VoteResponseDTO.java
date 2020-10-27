package facultad.trendz.dto.vote;

import javax.validation.constraints.NotNull;


public class VoteResponseDTO {


    @NotNull(message = "UserId cannot be empty")
    private Long userId;

    @NotNull(message = "Username cannot be empty")
    private String username;

    @NotNull(message = "postId cannot be empty")
    private Long postId;

    @NotNull(message = "postTitle cannot be empty")
    private String postTitle;

    private boolean isUpvote;


    public VoteResponseDTO( Long userId,String username,Long postId,String postTitle, Boolean isUpvote) {
        this.userId = userId;
        this.username = username;
        this.postId = postId;
        this.postTitle = postTitle;
        this.isUpvote=isUpvote;
    }

    public VoteResponseDTO(){}

    public Long getUserId() {
        return userId;
    }

    public void setUserId(Long userId) {
        this.userId = userId;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public Long getPostId() {
        return postId;
    }

    public void setPostId(Long postId) {
        this.postId = postId;
    }

    public String getPostTitle() {
        return postTitle;
    }

    public void setPostTitle(String postTitle) {
        this.postTitle = postTitle;
    }

    public boolean isUpvote() {
        return isUpvote;
    }

    public void setUpvote(boolean upvote) {
        isUpvote = upvote;
    }
}
