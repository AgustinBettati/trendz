package facultad.trendz.model;

import javax.persistence.*;

@Entity
@Table(name = "post")
public class Post {
    @Id
    @GeneratedValue
    private Long id;


    @ManyToOne()
    @JoinColumn(name="topic_id", nullable=false)
    private Topic topic;


    public Post(Topic topic) {
        this.topic = topic;
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
}
