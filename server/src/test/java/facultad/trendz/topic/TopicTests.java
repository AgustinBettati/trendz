package facultad.trendz.topic;

import facultad.trendz.TestUtils;
import facultad.trendz.dto.*;
import facultad.trendz.dto.post.PostCreateDTO;
import facultad.trendz.dto.post.PostPageDTO;
import facultad.trendz.dto.post.PostResponseDTO;
import facultad.trendz.dto.post.SimplePostResponseDTO;
import facultad.trendz.dto.topic.TopicCreateDTO;
import facultad.trendz.dto.topic.TopicPageDTO;
import facultad.trendz.dto.topic.TopicResponseDTO;
import facultad.trendz.dto.user.JwtResponseDTO;
import facultad.trendz.model.Topic;
import facultad.trendz.repository.PostRepository;
import facultad.trendz.repository.TopicRepository;
import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.web.server.LocalServerPort;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.RestTemplate;

import java.net.URI;
import java.net.URISyntaxException;
import java.util.List;
import java.util.Optional;

@RunWith(SpringRunner.class)
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
public class TopicTests extends TestUtils {

    @LocalServerPort
    int randomServerPort;

    @Autowired
    TopicRepository topicRepository;

    @Autowired
    PostRepository postRepository;

    @Test
    public void testDeleteTopic() throws URISyntaxException {
        //GIVEN
        RestTemplate restTemplate = new RestTemplate();
        ResponseEntity<JwtResponseDTO> loginResponse = loginUser("admin@gmail.com", "admin", randomServerPort);
        String jwtToken = loginResponse.getBody().getToken();
        HttpHeaders headers = new HttpHeaders();
        headers.add("Authorization", "Bearer " + jwtToken);

        TopicCreateDTO topic = new TopicCreateDTO("Topic4", "description");
        HttpEntity<TopicCreateDTO> topicEntity = new HttpEntity<>(topic, headers);

        final String topicsUrl = "http://localhost:" + randomServerPort + "/topic";
        URI topicsUri = new URI(topicsUrl);

        ResponseEntity<TopicResponseDTO> response = restTemplate.postForEntity(topicsUri, topicEntity, TopicResponseDTO.class);
        Long topicId = response.getBody().getId();

        HttpEntity<TopicCreateDTO> entity = new HttpEntity<>(headers);

        final String deleteTopicUrl = String.format("http://localhost:%d/topic/%d", randomServerPort, topicId);
        URI deleteTopicUri = new URI(deleteTopicUrl);

        //WHEN
        ResponseEntity<MessageResponseDTO> deleteResponse = restTemplate.exchange(deleteTopicUri, HttpMethod.DELETE, entity, MessageResponseDTO.class);

        //THEN
        Assert.assertEquals(200, deleteResponse.getStatusCodeValue());
        Assert.assertTrue(topicRepository.getTopicById(topicId).isDeleted());

        topicRepository.findAllByDeletedIsFalse().forEach(topic1 -> Assert.assertNotEquals(topicId, topic1.getId()));
    }

    @Test
    public void testDeleteInvalidTopic() throws URISyntaxException {
        //GIVEN
        RestTemplate restTemplate = new RestTemplate();
        ResponseEntity<JwtResponseDTO> loginResponse = loginUser("admin@gmail.com", "admin", randomServerPort);

        String jwtToken = loginResponse.getBody().getToken();
        HttpHeaders headers = new HttpHeaders();
        headers.add("Authorization", "Bearer " + jwtToken);

        Long topicId = Long.MAX_VALUE;
        HttpEntity<TopicCreateDTO> entity = new HttpEntity<>(headers);

        final String deleteTopicUrl = String.format("http://localhost:%d/topic/%d", randomServerPort, topicId);
        URI deleteTopicUri = new URI(deleteTopicUrl);

        try {
            //WHEN
            restTemplate.exchange(deleteTopicUri, HttpMethod.DELETE, entity, MessageResponseDTO.class);

            //THEN
            Assert.fail();
        } catch (HttpClientErrorException e){
            Assert.assertEquals(404,e.getRawStatusCode());
            Assert.assertTrue(e.getResponseBodyAsString().contains("Requested topic not found"));
        }
    }

    @Test
    public void testTopicsByDate() throws URISyntaxException {
        //GIVEN
        RestTemplate restTemplate = new RestTemplate();
        ResponseEntity<JwtResponseDTO> loginResponse = loginUser("admin@gmail.com", "admin", randomServerPort);
        String jwtToken = loginResponse.getBody().getToken();
        HttpHeaders headers = new HttpHeaders();
        headers.add("Authorization", "Bearer " + jwtToken);

        //post new topics
        TopicCreateDTO topic1 = new TopicCreateDTO("T1", "description");
        TopicCreateDTO topic2 = new TopicCreateDTO("T2", "description");
        TopicCreateDTO topic3 = new TopicCreateDTO("T3", "description");

        HttpEntity<TopicCreateDTO> topicEntity1 = new HttpEntity<>(topic1, headers);
        HttpEntity<TopicCreateDTO> topicEntity2 = new HttpEntity<>(topic2, headers);
        HttpEntity<TopicCreateDTO> topicEntity3 = new HttpEntity<>(topic3, headers);

        final int page = 0;
        final int pageSize = 5;
        final String topicsUrl = String.format("http://localhost:%d/topic?page=%d&size=%d", randomServerPort, page, pageSize );
        URI topicsUri = new URI(topicsUrl);

        restTemplate.postForEntity(topicsUri, topicEntity1, TopicResponseDTO.class);
        restTemplate.postForEntity(topicsUri, topicEntity2, TopicResponseDTO.class);
        restTemplate.postForEntity(topicsUri, topicEntity3, TopicResponseDTO.class);


        HttpEntity<TopicCreateDTO> entity = new HttpEntity<>(headers);
        //WHEN
        ResponseEntity<TopicPageDTO> response = restTemplate.exchange(topicsUri, HttpMethod.GET, entity, TopicPageDTO.class);
        //THEN
        Assert.assertEquals(200, response.getStatusCodeValue());

        Assert.assertEquals("T3", response.getBody().getTopics().get(0).getTitle());
        Assert.assertEquals("T2", response.getBody().getTopics().get(1).getTitle());
        Assert.assertEquals("T1", response.getBody().getTopics().get(2).getTitle());
        Assert.assertEquals(pageSize, response.getBody().getPageSize());
        Assert.assertEquals(page, response.getBody().getPageNumber());
        Assert.assertEquals(Math.ceil((double) topicRepository.findAllByDeletedIsFalse().size() / (double) pageSize), response.getBody().getTotalPages(), 0.0);
    }

    @Test
    public void testTopicCreation() throws URISyntaxException {
        //GIVEN
        RestTemplate restTemplate = new RestTemplate();

        ResponseEntity<JwtResponseDTO> loginResponse = loginUser("admin@gmail.com", "admin", randomServerPort);

        String jwtToken = loginResponse.getBody().getToken();
        HttpHeaders headers = new HttpHeaders();
        headers.add("Authorization", "Bearer " + jwtToken);

        TopicCreateDTO body = new TopicCreateDTO("TestTitle", "Test Description");

        HttpEntity<TopicCreateDTO> entity = new HttpEntity<>(body, headers);
        final String createTopicUrl = "http://localhost:" + randomServerPort + "/topic";
        URI createTopicUri = new URI(createTopicUrl);

        //WHEN
        ResponseEntity<TopicResponseDTO> response = restTemplate.postForEntity(createTopicUri, entity, TopicResponseDTO.class);

        //THEN
        Assert.assertEquals(201, response.getStatusCodeValue());
        Assert.assertEquals("TestTitle", response.getBody().getTitle());
        Assert.assertEquals("Test Description", response.getBody().getDescription());
    }

    @Test
    public void testInvalidTitle() throws URISyntaxException {
        //GIVEN
        RestTemplate restTemplate = new RestTemplate();

        ResponseEntity<JwtResponseDTO> loginResponse = loginUser("admin@gmail.com", "admin", randomServerPort);

        String jwtToken = loginResponse.getBody().getToken();
        HttpHeaders headers = new HttpHeaders();
        headers.add("Authorization", "Bearer " + jwtToken);

        TopicCreateDTO body = new TopicCreateDTO("TestTitle1", "Test Description");

        HttpEntity<TopicCreateDTO> entity = new HttpEntity<>(body, headers);
        final String createTopicUrl = "http://localhost:" + randomServerPort + "/topic";
        URI createTopicUri = new URI(createTopicUrl);

        restTemplate.postForEntity(createTopicUri, entity, TopicResponseDTO.class); // post 1st topic

        TopicCreateDTO body2 = new TopicCreateDTO("TestTitle1", "Description");

        HttpEntity<TopicCreateDTO> entity2 = new HttpEntity<>(body2, headers);

        try {
        //WHEN
            restTemplate.postForEntity(createTopicUri, entity2, TopicResponseDTO.class); // post 2nd topic with same title as first

        //THEN
            Assert.fail();
        } catch (HttpClientErrorException e){
            Assert.assertEquals(409,e.getRawStatusCode());
            Assert.assertTrue(e.getResponseBodyAsString().contains("Title TestTitle1 already in use"));
        }
    }

    @Test
    public void testGetPostsByTopic() throws URISyntaxException {
        //GIVEN
        RestTemplate restTemplate = new RestTemplate();
        ResponseEntity<JwtResponseDTO> loginResponse = loginUser("admin@gmail.com", "admin", randomServerPort);
        String jwtToken = loginResponse.getBody().getToken();
        HttpHeaders headers = new HttpHeaders();
        headers.add("Authorization", "Bearer " + jwtToken);

        //post new topics
        TopicCreateDTO topic1 = new TopicCreateDTO("Topic1", "description");


        HttpEntity<TopicCreateDTO> topicEntity1 = new HttpEntity<>(topic1, headers);

        final String topicsUrl = "http://localhost:" + randomServerPort + "/topic";
        URI topicsUri = new URI(topicsUrl);

        ResponseEntity<TopicResponseDTO> response1 = restTemplate.postForEntity(topicsUri, topicEntity1, TopicResponseDTO.class);



        // posts for topic#1
        PostCreateDTO post1 = new PostCreateDTO("Post1","description","testUrl",response1.getBody().getId());
        PostCreateDTO post2 = new PostCreateDTO("Post2","description","testUrl",response1.getBody().getId());
        PostCreateDTO post3 = new PostCreateDTO("Post3","description","testUrl",response1.getBody().getId());

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

        Assert.assertEquals("Post3", response.getBody().getPosts().get(0).getTitle());
        Assert.assertEquals("Post2", response.getBody().getPosts().get(1).getTitle());
        Assert.assertEquals("Post1", response.getBody().getPosts().get(2).getTitle());

        Assert.assertEquals(3, response.getBody().getPageSize());
        Assert.assertEquals(page, response.getBody().getPageNumber());
        Assert.assertEquals(Math.ceil( 3.0 / (double) pageSize), response.getBody().getTotalPages(), 0.0);
    }
}
