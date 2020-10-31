package facultad.trendz.dto.post;

import facultad.trendz.dto.comment.CommentResponseDTO;

import java.util.Date;
import java.util.List;

public class PostResponseDTO {

    private Long id;
    private String title;
    private String description;
    private String link;
    private Date date;
    private Long topicId;
    private Long userId;
    private String username;
    private List<CommentResponseDTO> comments;
    private int upvotes;
    private int downvotes;




    public PostResponseDTO(Long id, String title, String description, String link, Date date,Long topicId,Long userId, List<CommentResponseDTO> comments, String username,int upvotes,int downvotes){
        this.id = id;
        this.title = title;
        this.description = description;
        this.link=link;
        this.topicId=topicId;
        this.date=date;
        this.userId=userId;
        this.comments = comments;
        this.username = username;
        this.upvotes=upvotes;
        this.downvotes=downvotes;

    }

    public PostResponseDTO() {}

    public Long getId() {
        return id;
    }

    public String getTitle() {
        return title;
    }

    public String getDescription() {
        return description;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getLink() {
        return link;
    }

    public void setLink(String link) {
        this.link = link;
    }

    public Long getTopicId() {
        return topicId;
    }

    public void setTopicId(Long topicId) {
        this.topicId = topicId;
    }

    public Date getDate() {
        return date;
    }

    public void setDate(Date date) {
        this.date = date;
    }

    public Long getUserId() {
        return userId;
    }

    public void setUserId(Long userId) {
        this.userId = userId;
    }

    public List<CommentResponseDTO> getComment() {
        return comments;
    }

    public void setComment(List<CommentResponseDTO> comment) {
        this.comments = comment;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public List<CommentResponseDTO> getComments() {
        return comments;
    }

    public void setComments(List<CommentResponseDTO> comments) {
        this.comments = comments;
    }


}