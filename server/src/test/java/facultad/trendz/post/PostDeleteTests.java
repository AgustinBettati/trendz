package facultad.trendz.post;

import facultad.trendz.TestUtils;
import facultad.trendz.dto.MessageResponseDTO;
import facultad.trendz.dto.post.PostEditDTO;
import facultad.trendz.dto.post.PostResponseDTO;
import facultad.trendz.dto.topic.TopicResponseDTO;
import facultad.trendz.dto.user.JwtResponseDTO;
import facultad.trendz.repository.PostRepository;
import org.junit.Assert;
import org.junit.BeforeClass;
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
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.RestTemplate;

import java.net.URI;
import java.net.URISyntaxException;

@RunWith(SpringRunner.class)
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
public class PostDeleteTests extends TestUtils {

    @LocalServerPort
    int randomServerPort;

    @Autowired
    PostRepository postRepository;

    @Test
    public void testDelete() throws URISyntaxException {
        //GIVEN
        RestTemplate restTemplate = new RestTemplate();
        ResponseEntity<JwtResponseDTO> response = loginUser("admin@gmail.com", "admin", randomServerPort);
        String adminJwt = response.getBody().getToken();

        ResponseEntity<TopicResponseDTO> topicResponse = postTopic(adminJwt, "testDeletePost", "description", randomServerPort);

        Long topicId = topicResponse.getBody().getId();

        ResponseEntity<PostResponseDTO> postResponse = postPost(adminJwt, "TestDeletePost", "description", "testurl.com", topicId, randomServerPort);

        HttpHeaders headers = new HttpHeaders();
        headers.add("Authorization", "Bearer " + adminJwt);

        HttpEntity<PostEditDTO> postDeleteEntity = new HttpEntity<>(headers);
        final String postDeleteUrl = String.format("http://localhost:%d/post/%d", randomServerPort, postResponse.getBody().getId());
        URI postDeleteUri = new URI(postDeleteUrl);

        //WHEN
        ResponseEntity<MessageResponseDTO> deleteResponse = restTemplate.exchange(postDeleteUri, HttpMethod.DELETE, postDeleteEntity, MessageResponseDTO.class);

        //THEN
        Assert.assertEquals(200, deleteResponse.getStatusCodeValue());
        Assert.assertTrue(deleteResponse.getBody().getMessage().contains("Successfully deleted Post"));

        Assert.assertFalse(postRepository.existsById(postResponse.getBody().getId()));
    }

    @Test
    public void testDeleteWithInvalidId() throws URISyntaxException {
        //GIVEN
        RestTemplate restTemplate = new RestTemplate();
        ResponseEntity<JwtResponseDTO> response = loginUser("admin@gmail.com", "admin", randomServerPort);
        String adminJwt = response.getBody().getToken();

        ResponseEntity<TopicResponseDTO> topicResponse = postTopic(adminJwt, "testDeletePost2", "description", randomServerPort);

        Long topicId = topicResponse.getBody().getId();

        ResponseEntity<PostResponseDTO> postResponse = postPost(adminJwt, "TestDeletePost2", "description", "testurl.com", topicId, randomServerPort);

        HttpHeaders headers = new HttpHeaders();
        headers.add("Authorization", "Bearer " + adminJwt);

        HttpEntity<PostEditDTO> postDeleteEntity = new HttpEntity<>(headers);
        final String postDeleteUrl = String.format("http://localhost:%d/post/%d", randomServerPort, Long.MAX_VALUE);
        URI postDeleteUri = new URI(postDeleteUrl);

        try {
        //WHEN
            restTemplate.exchange(postDeleteUri, HttpMethod.DELETE, postDeleteEntity, MessageResponseDTO.class);

        //THEN
            Assert.fail();
        } catch (HttpClientErrorException e){
            Assert.assertEquals(404,e.getRawStatusCode());
            Assert.assertTrue(e.getResponseBodyAsString().contains("Requested post not found"));

            Assert.assertTrue(postRepository.existsById(postResponse.getBody().getId()));
        }
    }

    @Test
    public void testDeleteWithInvalidUser() throws URISyntaxException {
        //GIVEN
        RestTemplate restTemplate = new RestTemplate();
        //login with admin(could be any user) for post creation
        ResponseEntity<JwtResponseDTO> response = loginUser("admin@gmail.com", "admin", randomServerPort);
        String adminJwt = response.getBody().getToken();

        //login with non admin user for post delete
        ResponseEntity<JwtResponseDTO> userResponse = loginUser("user@gmail.com", "user", randomServerPort);
        String userJwt = userResponse.getBody().getToken();


        ResponseEntity<TopicResponseDTO> topicResponse = postTopic(adminJwt, "testDeletePost3", "description", randomServerPort);
        Long topicId = topicResponse.getBody().getId();

        //post created by admin
        ResponseEntity<PostResponseDTO> postResponse = postPost(adminJwt, "TestDeletePost3", "description", "testurl.com", topicId, randomServerPort);

        //header jwt set to user which is not the creator of the post
        HttpHeaders headers = new HttpHeaders();
        headers.add("Authorization", "Bearer " + userJwt);

        HttpEntity<PostEditDTO> postDeleteEntity = new HttpEntity<>(headers);
        final String postDeleteUrl = String.format("http://localhost:%d/post/%d", randomServerPort, postResponse.getBody().getId());
        URI postDeleteUri = new URI(postDeleteUrl);

        try {
            //WHEN
            restTemplate.exchange(postDeleteUri, HttpMethod.DELETE, postDeleteEntity, MessageResponseDTO.class);

            //THEN
            Assert.fail();
        } catch (HttpClientErrorException e){
            Assert.assertEquals(401,e.getRawStatusCode());
            Assert.assertTrue(e.getResponseBodyAsString().contains("Unauthorized access"));

            Assert.assertTrue(postRepository.existsById(postResponse.getBody().getId()));
        }
    }

    @Test
    public void testDeleteByAdmin() throws URISyntaxException {
        //GIVEN
        RestTemplate restTemplate = new RestTemplate();

        //login with regular user for post creation
        ResponseEntity<JwtResponseDTO> userResponse = loginUser("user@gmail.com", "user", randomServerPort);
        String userJwt = userResponse.getBody().getToken();

        //login with admin for post delete
        ResponseEntity<JwtResponseDTO> adminResponse = loginUser("admin@gmail.com", "admin", randomServerPort);
        String adminJwt = adminResponse.getBody().getToken();

        //topic can only be created by admins
        ResponseEntity<TopicResponseDTO> topicResponse = postTopic(adminJwt, "testDeletePost4", "description", randomServerPort);
        Long topicId = topicResponse.getBody().getId();

        //post created by regular user
        ResponseEntity<PostResponseDTO> postResponse = postPost(userJwt, "TestDeletePost4", "description", "testurl.com", topicId, randomServerPort);

        HttpHeaders headers = new HttpHeaders();
        headers.add("Authorization", "Bearer " + adminJwt);

        HttpEntity<PostEditDTO> postDeleteEntity = new HttpEntity<>(headers);
        final String postDeleteUrl = String.format("http://localhost:%d/post/%d", randomServerPort, postResponse.getBody().getId());
        URI postDeleteUri = new URI(postDeleteUrl);

        //WHEN
        ResponseEntity<MessageResponseDTO> deleteResponse = restTemplate.exchange(postDeleteUri, HttpMethod.DELETE, postDeleteEntity, MessageResponseDTO.class);

        //THEN
        Assert.assertEquals(200, deleteResponse.getStatusCodeValue());
        Assert.assertTrue(deleteResponse.getBody().getMessage().contains("Successfully deleted Post"));

        Assert.assertFalse(postRepository.existsById(postResponse.getBody().getId()));
    }


}
