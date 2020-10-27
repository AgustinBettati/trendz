package facultad.trendz.model;

import org.hibernate.annotations.LazyCollection;
import org.hibernate.annotations.LazyCollectionOption;

import javax.persistence.*;
import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "user")
public class User {
    @Id
    @GeneratedValue(strategy=GenerationType.IDENTITY)
    private Long id;

    private String email;

    private String username;

    private String password;

    @ManyToOne
    private Role role;

    @OneToMany(cascade = CascadeType.ALL, mappedBy = "user")
    @LazyCollection(LazyCollectionOption.FALSE)
    private List<Post> posts;

    @OneToMany(cascade = CascadeType.ALL, mappedBy = "user")
    @LazyCollection(LazyCollectionOption.FALSE)
    private List<Comment> comments;

    @OneToMany(cascade = CascadeType.ALL, mappedBy = "user")
    @LazyCollection(LazyCollectionOption.FALSE)
    private List<Vote> votes ;


    @ManyToMany(mappedBy = "upvotes")
    @LazyCollection(LazyCollectionOption.FALSE)
    private List<Post> upvotedPosts = new ArrayList<>();


    @ManyToMany(mappedBy = "downvotes")
    @LazyCollection(LazyCollectionOption.FALSE)
    private List<Post> downvotedPosts = new ArrayList<>();

    public User() {
    }

    public User(String email, String username, String password, Role role) {
        this.email = email;
        this.username = username;
        this.password = password;
        this.role = role;
        this.downvotedPosts= new ArrayList<>();
        this.upvotedPosts=new ArrayList<>();
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public Role getRole() {
        return role;
    }

    public void setRole(Role role) {
        this.role = role;
    }

    public List<Post> getPosts() {
        return posts;
    }

    public void setPosts(List<Post> posts) {
        this.posts = posts;
    }

    public List<Comment> getComments() {
        return comments;
    }

    public void setComments(List<Comment> comments) {
        this.comments = comments;
    }

    public List<Post> getUpvotedPosts() {
        return upvotedPosts;
    }

    public void setUpvotedPosts(List<Post> upvotedPosts) {
        this.upvotedPosts = upvotedPosts;
    }

    public List<Post> getDownvotedPosts() {
        return downvotedPosts;
    }

    public void setDownvotedPosts(List<Post> downvotedPosts) {
        this.downvotedPosts = downvotedPosts;
    }

    public boolean isUpvoted(Post post){
        return upvotedPosts.contains(post);
    }

    public boolean isDownvoted(Post post){
        return downvotedPosts.contains(post);
    }
}
