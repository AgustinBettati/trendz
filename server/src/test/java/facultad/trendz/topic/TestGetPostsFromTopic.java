package facultad.trendz.topic;

import facultad.trendz.TestUtils;
import facultad.trendz.dto.post.PostCreateDTO;
import facultad.trendz.dto.post.PostPageDTO;
import facultad.trendz.dto.post.PostResponseDTO;
import facultad.trendz.dto.topic.TopicCreateDTO;
import facultad.trendz.dto.topic.TopicResponseDTO;
import facultad.trendz.dto.user.JwtResponseDTO;
import facultad.trendz.repository.TopicRepository;
import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.web.server.LocalServerPort;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.web.client.RestTemplate;

import java.net.URI;
import java.net.URISyntaxException;

@RunWith(SpringRunner.class)
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
public class TestGetPostsFromTopic extends TestUtils {

    @LocalServerPort
    int randomServerPort;

    @Autowired
    TopicRepository topicRepository;

    @Test
    public void testGetPostsByTopic() throws URISyntaxException {
        //GIVEN
        RestTemplate restTemplate = new RestTemplate();
        ResponseEntity<JwtResponseDTO> loginResponse = loginUser("admin@gmail.com", "admin", randomServerPort);
        String jwtToken = loginResponse.getBody().getToken();
        HttpHeaders headers = new HttpHeaders();
        headers.add("Authorization", "Bearer " + jwtToken);

        //post new topics
        TopicCreateDTO topic1 = new TopicCreateDTO("Topic 1", "description");


        HttpEntity<TopicCreateDTO> topicEntity1 = new HttpEntity<>(topic1, headers);

        final String topicsUrl = "http://localhost:" + randomServerPort + "/topic";
        URI topicsUri = new URI(topicsUrl);

        ResponseEntity<TopicResponseDTO> response1 = restTemplate.postForEntity(topicsUri, topicEntity1, TopicResponseDTO.class);



        // posts for topic#1
        PostCreateDTO post1 = new PostCreateDTO("Post 1","description","testUrl",response1.getBody().getId());
        PostCreateDTO post2 = new PostCreateDTO("Post 2","description","testUrl",response1.getBody().getId());
        PostCreateDTO post3 = new PostCreateDTO("Post 3","description","testUrl",response1.getBody().getId());

        HttpEntity<PostCreateDTO> postEntity1 = new HttpEntity<>(post1, headers);
        HttpEntity<PostCreateDTO> postEntity2 = new HttpEntity<>(post2, headers);
        HttpEntity<PostCreateDTO> postEntity3 = new HttpEntity<>(post3, headers);

        final String postsUrl = "http://localhost:" + randomServerPort + "/post";
        URI postsUri = new URI(postsUrl);

        restTemplate.postForEntity(postsUri,postEntity1,PostResponseDTO.class);
        restTemplate.postForEntity(postsUri,postEntity2,PostResponseDTO.class);
        restTemplate.postForEntity(postsUri,postEntity3,PostResponseDTO.class);

        final int page = 0;
        final int pageSize = 5;
        final String topicsUrl2 = String.format("http://localhost:%d/topicposts/%d?page=%d&size=%d", randomServerPort,response1.getBody().getId(), page, pageSize );
        URI topicsUri2 = new URI(topicsUrl2);

        HttpEntity<TopicCreateDTO> entity = new HttpEntity<>(headers);
        //WHEN
        ResponseEntity<PostPageDTO> response = restTemplate.exchange(topicsUri2, HttpMethod.GET, entity, PostPageDTO.class);
        //THEN
        Assert.assertEquals(200, response.getStatusCodeValue());

        Assert.assertEquals("Post 3", response.getBody().getPosts().get(0).getTitle());
        Assert.assertEquals("Post 2", response.getBody().getPosts().get(1).getTitle());
        Assert.assertEquals("Post 1", response.getBody().getPosts().get(2).getTitle());

    }

}
