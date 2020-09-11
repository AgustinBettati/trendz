package facultad.trendz.model;
import javax.persistence.*;
import java.sql.Date;
import java.util.List;

@Entity
@Table(name = "topic")
public class Topic {
    @Id
    @GeneratedValue
    private Long id;

    private String name;
    private String description;
    private Date date;

    @OneToMany(mappedBy = "topic")
     private List<Post> posts;


    public  Topic(String name, String description, Date date) {
        this.name = name;
        this.description = description;
        this.date = date;
    }

    public Topic() {

    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public Date getDate() {
        return date;
    }

    public void setDate(Date date) {
        this.date = date;
    }

    public List<Post> getPosts() {
        return posts;
    }

    public void setPosts(List<Post> posts) {
        this.posts = posts;
    }
}
