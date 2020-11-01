package facultad.trendz.dto;

import facultad.trendz.dto.post.SimplePostResponseDTO;
import facultad.trendz.dto.topic.TopicResponseDTO;

import java.util.List;

public class SearchResponseDTO {
    private List<SimplePostResponseDTO> posts;
    private List<TopicResponseDTO> topics;

    public SearchResponseDTO(List<SimplePostResponseDTO> posts, List<TopicResponseDTO> topics) {
        this.posts = posts;
        this.topics = topics;
    }

    public SearchResponseDTO() {
    }

    public List<SimplePostResponseDTO> getPosts() {
        return posts;
    }

    public void setPosts(List<SimplePostResponseDTO> posts) {
        this.posts = posts;
    }

    public List<TopicResponseDTO> getTopics() {
        return topics;
    }

    public void setTopics(List<TopicResponseDTO> topics) {
        this.topics = topics;
    }
}
