package facultad.trendz.dto.post;

import java.util.Date;
import java.util.List;

public class SimplePostResponseDTO {

    private Long id;
    private String title;
    private String description;
    private String link;
    private Date date;
    private Long topicId;
    private String topicTitle;
    private Long userId;
    private String username;
    private boolean deleted;
    private List<Long> upvotes;
    private List<Long> downvotes;


    public SimplePostResponseDTO(Long id, String title, String description, String link, Date date,Long topicId,Long userId, String username, String topicTitle, boolean deleted, List<Long> upvotes, List<Long> downvotes){
        this.id = id;
        this.title = title;
        this.description = description;
        this.link=link;
        this.topicId=topicId;
        this.date=date;
        this.userId=userId;
        this.username = username;
        this.topicTitle = topicTitle;
        this.deleted = deleted;
        this.upvotes = upvotes;
        this.downvotes = downvotes;
    }

    public SimplePostResponseDTO() {}

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

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getTopicTitle() {
        return topicTitle;
    }

    public void setTopicTitle(String topicTitle) {
        this.topicTitle = topicTitle;
    }

    public boolean isDeleted() {
        return deleted;
    }

    public void setDeleted(boolean deleted) {
        this.deleted = deleted;
    }

    public List<Long> getUpvotes() {
        return upvotes;
    }

    public void setUpvotes(List<Long> upvotes) {
        this.upvotes = upvotes;
    }

    public List<Long> getDownvotes() {
        return downvotes;
    }

    public void setDownvotes(List<Long> downvotes) {
        this.downvotes = downvotes;
    }
}
