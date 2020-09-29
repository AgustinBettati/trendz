package facultad.trendz.dto.post;


import java.util.Date;

public class PostGetDTO {

    private Long id;
    private String title;
    private String description;
    private String link;
    private Date date;
    private long userId;
    private String username;


    public PostGetDTO(Long id, String title, String description, String link, Date date, long userId, String username){
        this.id = id;
        this.title = title;
        this.description = description;
        this.link=link;
        this.date=date;
        this.userId=userId;
        this.username=username;
    }

    public PostGetDTO() {}

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

    public Date getDate() {
        return date;
    }

    public void setDate(Date date) {
        this.date = date;
    }

    public long getUserId() {
        return userId;
    }

    public void setUserId(long userId) {
        this.userId = userId;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }
}
