package facultad.trendz.dto.comment;

import java.util.Date;

public class CommentResponseDTO {

    private Long id;
    private String username;
    private Long postId;
    private String content;
    private Date creationDate;
    private Date editDate;

    public CommentResponseDTO(Long id, String username, Long postId, String content, Date creationDate, Date editDate) {
        this.id = id;
        this.username = username;
        this.postId = postId;
        this.content = content;
        this.creationDate = creationDate;
        this.editDate = editDate;
    }

    public CommentResponseDTO() {
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
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

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public Date getCreationDate() {
        return creationDate;
    }

    public void setCreationDate(Date creationDate) {
        this.creationDate = creationDate;
    }

    public Date getEditDate() {
        return editDate;
    }

    public void setEditDate(Date editDate) {
        this.editDate = editDate;
    }
}
