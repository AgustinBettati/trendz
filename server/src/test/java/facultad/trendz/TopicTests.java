package facultad.trendz;

import facultad.trendz.dto.*;
import facultad.trendz.dto.user.JwtResponseDTO;
import facultad.trendz.dto.user.LoginDTO;
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
import org.springframework.web.client.RestTemplate;

import java.net.URI;
import java.net.URISyntaxException;
import java.util.Date;
import java.util.List;

@RunWith(SpringRunner.class)
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
public class TopicTests {
    @LocalServerPort
    int randomServerPort;

    @Autowired
    TopicRepository topicRepository;

    @Test
    public void testGetPopularTopics() throws URISyntaxException {
        //GIVEN
        RestTemplate restTemplate = new RestTemplate();
        ResponseEntity<JwtResponseDTO> loginResponse = loginUser("admin@gmail.com", "admin");
        String jwtToken = loginResponse.getBody().getToken();
        HttpHeaders headers = new HttpHeaders();
        headers.add("Authorization", "Bearer " + jwtToken);

        //post new topics
        TopicCreateDTO topic1 = new TopicCreateDTO("Topic1", "description", new Date());
        TopicCreateDTO topic2 = new TopicCreateDTO("Topic2", "description", new Date());
        TopicCreateDTO topic3 = new TopicCreateDTO("Topic3", "description", new Date());

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
        PostCreateDTO post1 = new PostCreateDTO("Post1","description","testUrl",new Date(),response3.getBody().getId());
        PostCreateDTO post2 = new PostCreateDTO("Post2","description","testUrl",new Date(),response3.getBody().getId());
        PostCreateDTO post3 = new PostCreateDTO("Post3","description","testUrl",new Date(),response3.getBody().getId());

        //2 posts por topic#1
        PostCreateDTO post4 = new PostCreateDTO("Post4","description","testUrl",new Date(),response1.getBody().getId());
        PostCreateDTO post5 = new PostCreateDTO("Post5","description","testUrl",new Date(),response1.getBody().getId());

        //1 post por topic#2
        PostCreateDTO post6 = new PostCreateDTO("Post6","description","testUrl",new Date(),response2.getBody().getId());

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