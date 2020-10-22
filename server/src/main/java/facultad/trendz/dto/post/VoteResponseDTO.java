package facultad.trendz.dto.post;

import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

public class VoteResponseDTO {


    @NotNull(message = "UserId cannot be empty")
    private Long userId;

    @NotNull(message = "Username cannot be empty")
    private String username;

    @NotNull(message = "postId cannot be empty")
    private Long postId;

    @NotNull(message = "postTitle cannot be empty")
    private String postTitle;


    public VoteResponseDTO(@NotNull(message = "UserId cannot be empty") Long userId,
                             @NotNull(message = "Username cannot be empty") String username,
                             @NotNull(message = "postId cannot be empty") Long postId,
                             @NotNull(message = "postTitle cannot be empty") String postTitle) {
        this.userId = userId;
        this.username = username;
        this.postId = postId;
        this.postTitle = postTitle;
    }

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
}
