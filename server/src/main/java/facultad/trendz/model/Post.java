package facultad.trendz.model;

import org.hibernate.annotations.LazyCollection;
import org.hibernate.annotations.LazyCollectionOption;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

@Entity
@Table(name = "post")
public class Post {
    @Id
    @GeneratedValue
    private Long id;

    private String title;

    @Column(length=40000)
    private String description;

    private String link;

    private Date date;

    @OneToMany(cascade = CascadeType.ALL, mappedBy = "post")
    @LazyCollection(LazyCollectionOption.FALSE)
    private List<Comment> comments;

    @ManyToOne()
    @JoinColumn(name="topic_id", nullable=false)
    private Topic topic;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name="user_id", nullable=false)
    private User user;

    @ManyToMany(cascade = { CascadeType.ALL })
    @JoinTable(
            name = "Upvote",
            joinColumns = { @JoinColumn(name = "post_id") },
            inverseJoinColumns = { @JoinColumn(name = "user_id") }
    )
    @LazyCollection(LazyCollectionOption.FALSE)
    private List<User> upvotes = new ArrayList<>();


    @ManyToMany(cascade = { CascadeType.ALL })
    @JoinTable(
            name = "Downvote",
            joinColumns = { @JoinColumn(name = "post_id") },
            inverseJoinColumns = { @JoinColumn(name = "user_id") }
    )
    @LazyCollection(LazyCollectionOption.FALSE)
    private List<User> downvotes = new ArrayList<>();



    public Post(String title, String description, String link, Date date, Topic topic, User user) {
        this.title = title;
        this.description = description;
        this.link = link;
        this.date = date;
        this.topic = topic;
        this.user = user;
        this.upvotes=new ArrayList<>();
        this.downvotes=new ArrayList<>();
    }

    public Post() {

    }


    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Topic getTopic() {
        return topic;
    }

    public void setTopic(Topic topic) {
        this.topic = topic;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getDescription() {
        return description;
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

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }

    public List<Comment> getComments() {
        return comments;
    }

    public void setComments(List<Comment> comments) {
        this.comments = comments;
    }

    public void addComment(Comment comment){
        comments.add(comment);
    }

    public List<User> getUpvotes() {
        return upvotes;
    }

    public void setUpvotes(List<User> upvotes) {
        this.upvotes = upvotes;
    }

    public List<User> getDownvotes() {
        return downvotes;
    }

    public void setDownvotes(List<User> downvotes) {
        this.downvotes = downvotes;
    }
}
