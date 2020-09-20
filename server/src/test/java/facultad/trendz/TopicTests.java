package facultad.trendz;

import facultad.trendz.dto.*;
import facultad.trendz.dto.user.*;
import facultad.trendz.dto.user.JwtResponseDTO;
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

@RunWith(SpringRunner.class)
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
public class TopicTests {

    @LocalServerPort
    int randomServerPort;

    @Autowired
    TopicRepository topicRepository;

    @Test
    public void testDeleteTopic() throws URISyntaxException {
        //GIVEN
        RestTemplate restTemplate = new RestTemplate();
        ResponseEntity<JwtResponseDTO> loginResponse = loginUser("admin@gmail.com", "admin");
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

        ResponseEntity<List<TopicResponseDTO>> topicsResponse = restTemplate.exchange(topicsUri, HttpMethod.GET, entity, new ParameterizedTypeReference<List<TopicResponseDTO>>() {});

        topicsResponse.getBody().forEach(topicResponseDTO -> Assert.assertNotEquals(topicId, topicResponseDTO.getId())); // assert get topics doesn't return the deleted topic
    }

    @Test
    public void testDeleteInvalidTopic() throws URISyntaxException {
        //GIVEN
        RestTemplate restTemplate = new RestTemplate();
        ResponseEntity<JwtResponseDTO> loginResponse = loginUser("admin@gmail.com", "admin");

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
    public void testGetPopularTopics() throws URISyntaxException {
        //GIVEN
        RestTemplate restTemplate = new RestTemplate();
        ResponseEntity<JwtResponseDTO> loginResponse = loginUser("admin@gmail.com", "admin");
        String jwtToken = loginResponse.getBody().getToken();
        HttpHeaders headers = new HttpHeaders();
        headers.add("Authorization", "Bearer " + jwtToken);

        //post new topics
        TopicCreateDTO topic1 = new TopicCreateDTO("Topic1", "description");
        TopicCreateDTO topic2 = new TopicCreateDTO("Topic2", "description");
        TopicCreateDTO topic3 = new TopicCreateDTO("Topic3", "description");

        HttpEntity<TopicCreateDTO> topicEntity1 = new HttpEntity<>(topic1, headers);
        HttpEntity<TopicCreateDTO> topicEntity2 = new HttpEntity<>(topic2, headers);
        HttpEntity<TopicCreateDTO> topicEntity3 = new HttpEntity<>(topic3, headers);

        final String topicsUrl = "http://localhost:" + randomServerPort + "/topic";
        URI topicsUri = new URI(topicsUrl);

        ResponseEntity<TopicResponseDTO> response1 = restTemplate.postForEntity(topicsUri, topicEntity1, TopicResponseDTO.class);
        ResponseEntity<TopicResponseDTO> response2 = restTemplate.postForEntity(topicsUri, topicEntity2, TopicResponseDTO.class);
        ResponseEntity<TopicResponseDTO> response3 = restTemplate.postForEntity(topicsUri, topicEntity3, TopicResponseDTO.class);

        //add different amount of posts to each topic

        //3 posts por topic#3
        PostCreateDTO post1 = new PostCreateDTO("Post1","description","testUrl",response3.getBody().getId());
        PostCreateDTO post2 = new PostCreateDTO("Post2","description","testUrl",response3.getBody().getId());
        PostCreateDTO post3 = new PostCreateDTO("Post3","description","testUrl",response3.getBody().getId());

        //2 posts por topic#1
        PostCreateDTO post4 = new PostCreateDTO("Post4","description","testUrl",response1.getBody().getId());
        PostCreateDTO post5 = new PostCreateDTO("Post5","description","testUrl",response1.getBody().getId());

        //1 post por topic#2
        PostCreateDTO post6 = new PostCreateDTO("Post6","description","testUrl",response2.getBody().getId());

        HttpEntity<PostCreateDTO> postEntity1 = new HttpEntity<>(post1, headers);
        HttpEntity<PostCreateDTO> postEntity2 = new HttpEntity<>(post2, headers);
        HttpEntity<PostCreateDTO> postEntity3 = new HttpEntity<>(post3, headers);
        HttpEntity<PostCreateDTO> postEntity4 = new HttpEntity<>(post4, headers);
        HttpEntity<PostCreateDTO> postEntity5 = new HttpEntity<>(post5, headers);
        HttpEntity<PostCreateDTO> postEntity6 = new HttpEntity<>(post6, headers);

        final String postsUrl = "http://localhost:" + randomServerPort + "/post";
        URI postsUri = new URI(postsUrl);

        restTemplate.postForEntity(postsUri,postEntity1,PostResponseDTO.class);
        restTemplate.postForEntity(postsUri,postEntity2,PostResponseDTO.class);
        restTemplate.postForEntity(postsUri,postEntity3,PostResponseDTO.class);
        restTemplate.postForEntity(postsUri,postEntity4,PostResponseDTO.class);
        restTemplate.postForEntity(postsUri,postEntity5,PostResponseDTO.class);
        restTemplate.postForEntity(postsUri,postEntity6,PostResponseDTO.class);


        HttpEntity<TopicCreateDTO> entity = new HttpEntity<>(headers);
        //WHEN
        ResponseEntity<List<TopicResponseDTO>> response = restTemplate.exchange(topicsUri, HttpMethod.GET, entity, new ParameterizedTypeReference<List<TopicResponseDTO>>() {});
        //THEN
        Assert.assertEquals(200, response.getStatusCodeValue());

        Assert.assertEquals("Topic3", response.getBody().get(0).getTitle()); //topic#3 with 3 posts
        Assert.assertEquals("Topic1", response.getBody().get(1).getTitle()); //topic#1 with 2 posts
        Assert.assertEquals("Topic2", response.getBody().get(2).getTitle()); //topic#2 with 1 post

    }

    @Test
    public void testTopicCreation() throws URISyntaxException {
        //GIVEN
        RestTemplate restTemplate = new RestTemplate();

        ResponseEntity<JwtResponseDTO> loginResponse = loginUser("admin@gmail.com", "admin");

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

        ResponseEntity<JwtResponseDTO> loginResponse = loginUser("admin@gmail.com", "admin");

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


    private ResponseEntity<JwtResponseDTO> loginUser(String email, String password) throws URISyntaxException {
        RestTemplate restTemplate = new RestTemplate();
        final String loginUrl = "http://localhost:" + randomServerPort + "/login";
        URI loginUri = new URI(loginUrl);
        HttpHeaders loginHeaders = new HttpHeaders();
        LoginDTO loginDTO = new LoginDTO(email, password);
        HttpEntity<LoginDTO> loginRequest = new HttpEntity<>(loginDTO, loginHeaders);
        return restTemplate.postForEntity(loginUri, loginRequest, JwtResponseDTO.class);
    }
}
