package facultad.trendz.service;

import facultad.trendz.dto.post.SimplePostResponseDTO;
import facultad.trendz.dto.topic.TopicCreateDTO;
import facultad.trendz.dto.topic.TopicPageDTO;
import facultad.trendz.dto.topic.TopicResponseDTO;
import facultad.trendz.exception.topic.TopicExistsException;
import facultad.trendz.exception.topic.TopicNotFoundException;
import facultad.trendz.model.Topic;
import facultad.trendz.repository.TopicRepository;
import facultad.trendz.repository.VoteRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.support.PagedListHolder;
import org.springframework.stereotype.Service;

import java.util.*;
import java.util.stream.Collectors;

@Service
public class TopicService {

    private final TopicRepository topicRepository;
    private final VoteRepository voteRepository;

    @Autowired
    public TopicService(TopicRepository topicRepository, VoteRepository voteRepository) {
        this.topicRepository = topicRepository;
        this.voteRepository = voteRepository;
    }

    public TopicResponseDTO saveTopic(TopicCreateDTO topicCreateDTO){
        final Topic topic = new Topic( topicCreateDTO.getTitle(), topicCreateDTO.getDescription(),new Date());
        topicRepository.save(topic);
        return new TopicResponseDTO(topic.getId(), topic.getTitle(), topic.getDescription(), topic.getCreationDate());
    }

    public void validateTopicTitle(String title) {
        if (topicRepository.existsByTitle(title))
            throw new TopicExistsException("Title " + title + " already in use");
    }

    public List<TopicResponseDTO> getTopicsByPopularity() {
        List<Topic> topics = topicRepository.findAllByDeletedIsFalse();

        topics.sort(Comparator.comparingInt((Topic topic) -> topic.getPosts().size()).reversed());

        List<TopicResponseDTO> topicResponses = new ArrayList<>(topics.size());
        for (Topic topic : topics) {
            topicResponses.add(new TopicResponseDTO(topic.getId(), topic.getTitle(), topic.getDescription(), topic.getCreationDate()));
        }
        return topicResponses;
    }

    public void deleteTopic(long topicId) {
        Optional<Topic> topic = topicRepository.findById(topicId);
        if (!topic.isPresent()) throw new TopicNotFoundException();

        topic.get().setDeleted(true);
        topic.get().getPosts().forEach(post -> post.setDeleted(true));
        topicRepository.save(topic.get());
    }

    public List<SimplePostResponseDTO> getTopicPosts(Long topicId) {
        return topicRepository.findByIdAndDeletedIsFalse(topicId).map(topic ->
            topic.getPosts().stream()
                .filter(post -> !post.isDeleted())
                .map(post -> new SimplePostResponseDTO(post.getId(),
                        post.getTitle(),
                        post.getDescription(),
                        post.getLink(),
                        post.getDate(),
                        post.getTopic().getId(),
                        post.getUser().getId(),
                        post.getUser().getUsername(),
                        post.getTopic().getTitle(),
                        post.isDeleted(),
                        voteRepository.findByPostIdAndIsUpvote(post.getId(),true).stream().map(vote -> vote.getUser().getId()).collect(Collectors.toList()),
                        voteRepository.findByPostIdAndIsUpvote(post.getId(),true).stream().map(vote -> vote.getUser().getId()).collect(Collectors.toList())))
                .collect(Collectors.toList())
        ).orElseThrow(TopicNotFoundException::new);
    }

    public TopicResponseDTO getTopicById(Long topicId) {
        return topicRepository.findByIdAndDeletedIsFalse(topicId).map(topic ->
                new TopicResponseDTO(topicId, topic.getTitle(), topic.getDescription(), topic.getCreationDate())
        ).orElseThrow(TopicNotFoundException::new);
    }

    public List<TopicResponseDTO> findTopicByTitle(String title, int amount) {
        return topicRepository.findByTitleIgnoreCaseContainingAndDeletedIsFalse(title)
                .stream()
                .sorted(Comparator.comparing(Topic::getCreationDate).reversed())
                .limit(amount)
                .map(topic -> new TopicResponseDTO(topic.getId(), topic.getTitle(), topic.getDescription(), topic.getCreationDate()))
                .collect(Collectors.toList());
    }

    public TopicPageDTO getPagedTopicsByPopularity(int page, int size) {
        List<TopicResponseDTO> sortedTopics = topicRepository.findAllByDeletedIsFalse()
                .stream().sorted(Comparator.comparingInt((Topic topic) -> topic.getPosts().size()).reversed())
                .map(topic -> new TopicResponseDTO(topic.getId(), topic.getTitle(), topic.getDescription(), topic.getCreationDate()))
                .collect(Collectors.toList());

        PagedListHolder<TopicResponseDTO> pages = new PagedListHolder<>(sortedTopics);
        pages.setPageSize(size);
        pages.setPage(page);
        return new TopicPageDTO(pages.getPageList(), pages.getPage(), pages.getPageSize(), pages.getPageCount());
    }
}
