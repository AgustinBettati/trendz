package facultad.trendz.controller;

import facultad.trendz.dto.SearchResponseDTO;
import facultad.trendz.service.PostService;
import facultad.trendz.service.TopicService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;

@CrossOrigin(origins = "*")
@Controller
public class SearchController {

    private TopicService topicService;
    private PostService postService;

    @Autowired
    public SearchController(TopicService topicService, PostService postService) {
        this.topicService = topicService;
        this.postService = postService;
    }

    @GetMapping("/search")
    public ResponseEntity<SearchResponseDTO> searchByTitle(@RequestParam String title){
        final SearchResponseDTO body = new SearchResponseDTO();
        body.setPosts(postService.findPostByTitle(title));
        body.setTopics(topicService.findTopicByTitle(title));

        final HttpStatus status = HttpStatus.OK;
        return new ResponseEntity<>(body,status);
    }
}
