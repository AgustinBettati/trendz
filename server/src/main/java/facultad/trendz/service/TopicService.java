package facultad.trendz.service;

import facultad.trendz.dto.TopicCreateDTO;
import facultad.trendz.dto.TopicResponseDTO;
import facultad.trendz.exception.TopicExistsException;
import facultad.trendz.model.Topic;
import facultad.trendz.repository.TopicRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class TopicService {

    private final TopicRepository topicRepository;

    @Autowired
    public TopicService(TopicRepository topicRepository) {
        this.topicRepository = topicRepository;
    }

    public TopicResponseDTO saveTopic(TopicCreateDTO topicResponseDTO){
        final Topic topic = new Topic( topicResponseDTO.getTitle(), topicResponseDTO.getDescription(),topicResponseDTO.getDate());
        topicRepository.save(topic);
        return new TopicResponseDTO(topic.getId(), topic.getTitle(), topic.getDescription(), topic.getCreationDate());
    }

    public void validateTopicTitle(String title) throws TopicExistsException{
        if(topicRepository.existsByTitle(title))
            throw new TopicExistsException("Title " + title + " already in use");
    }
}
