package facultad.trendz;

import facultad.trendz.dto.PostCreateDTO;
import facultad.trendz.dto.PostResponseDTO;
import facultad.trendz.dto.TopicCreateDTO;
import facultad.trendz.dto.TopicResponseDTO;
import facultad.trendz.dto.user.JwtResponseDTO;
import facultad.trendz.dto.user.LoginDTO;
import facultad.trendz.model.Post;
import facultad.trendz.repository.PostRepository;
import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.web.server.LocalServerPort;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseEntity;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.RestTemplate;

import java.net.URI;
import java.net.URISyntaxException;
import java.util.Optional;

@RunWith(SpringRunner.class)
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
public class PostTests {

    @LocalServerPort
    int randomServerPort;

    @Autowired
    PostRepository postRepository;

    @Test
    public void testPostCreation() throws URISyntaxException {
        //GIVEN
        ResponseEntity<JwtResponseDTO> loginResponse = loginUser("admin@gmail.com", "admin");

        String jwtToken = loginResponse.getBody().getToken();

        //post new Topic
        ResponseEntity<TopicResponseDTO> topicResponse = postTopic(jwtToken, "testTopic4", "test description");

        //WHEN
        ResponseEntity<PostResponseDTO> postResponse = postPost(jwtToken, "testTitle4", "test description", "testLink.com", topicResponse.getBody().getId());

        //THEN
        Assert.assertEquals(201, postResponse.getStatusCodeValue());
        Assert.assertEquals("testTitle4", postResponse.getBody().getTitle());
        Assert.assertEquals("test description", postResponse.getBody().getDescription());
        Assert.assertEquals("testLink.com", postResponse.getBody().getLink());

        Optional<Post> post = postRepository.findById(postResponse.getBody().getId());
        Assert.assertTrue(post.isPresent());
        Assert.assertEquals("testTitle4", post.get().getTitle());
        Assert.assertEquals("test description", post.get().getDescription());
        Assert.assertEquals("testLink.com", post.get().getLink());
    }

    @Test
    public void testPostCreationWithInvalidTitle() throws URISyntaxException {
        //GIVEN
        ResponseEntity<JwtResponseDTO> loginResponse = loginUser("admin@gmail.com", "admin");

        String jwtToken = loginResponse.getBody().getToken();

        //post new Topic
        ResponseEntity<TopicResponseDTO> topicResponse = postTopic(jwtToken, "testTopic5", "test description");

        //post new post
        postPost(jwtToken, "usedPostTitle", "test description", "testLink.com", topicResponse.getBody().getId());
        Long bodyId = topicResponse.getBody().getId();

        try {
        //WHEN trying to create a new post with an already used title
            postPost(jwtToken, "usedPostTitle", "test description", "testLink.com", bodyId);

        //THEN
            Assert.fail();
        } catch (HttpClientErrorException e) {
            Assert.assertEquals(409, e.getRawStatusCode());
            Assert.assertTrue(e.getResponseBodyAsString().contains("Title usedPostTitle already in use"));
        }
    }

    private ResponseEntity<TopicResponseDTO> postTopic(String jwt, String title, String description) throws URISyntaxException {
        RestTemplate restTemplate = new RestTemplate();

        HttpHeaders headers = new HttpHeaders();
        headers.add("Authorization", "Bearer " + jwt);

        TopicCreateDTO topic = new TopicCreateDTO(title, description);
        HttpEntity<TopicCreateDTO> topicEntity = new HttpEntity<>(topic, headers);

        final String url = "http://localhost:" + randomServerPort + "/topic";
        URI uri = new URI(url);

        return restTemplate.postForEntity(uri, topicEntity, TopicResponseDTO.class);
    }

    private ResponseEntity<PostResponseDTO> postPost(String jwt, String title, String description, String link, Long topicId) throws URISyntaxException {
        RestTemplate restTemplate = new RestTemplate();

        HttpHeaders headers = new HttpHeaders();
        headers.add("Authorization", "Bearer " + jwt);

        PostCreateDTO topic = new PostCreateDTO(title, description, link, topicId);
        HttpEntity<PostCreateDTO> topicEntity = new HttpEntity<>(topic, headers);

        final String url = "http://localhost:" + randomServerPort + "/post";
        URI uri = new URI(url);

        return restTemplate.postForEntity(uri, topicEntity, PostResponseDTO.class);
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
