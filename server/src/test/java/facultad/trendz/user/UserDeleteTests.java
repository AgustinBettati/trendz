package facultad.trendz.user;

import facultad.trendz.TestUtils;
import facultad.trendz.dto.MessageResponseDTO;
import facultad.trendz.dto.post.PostCreateDTO;
import facultad.trendz.dto.post.PostResponseDTO;
import facultad.trendz.dto.topic.TopicResponseDTO;
import facultad.trendz.dto.user.*;
import facultad.trendz.model.Post;
import facultad.trendz.model.Topic;
import facultad.trendz.repository.PostRepository;
import facultad.trendz.repository.TopicRepository;
import facultad.trendz.repository.UserRepository;
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
import java.util.Optional;

@RunWith(SpringRunner.class)
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
public class UserDeleteTests extends TestUtils {

    @LocalServerPort
    int randomServerPort;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private TopicRepository topicRepository;

    @Autowired
    private PostRepository postRepository;

    @Test
    public void successfullyDeleteUserTest() throws URISyntaxException {
        //given
        ResponseEntity<JwtResponseDTO> responseEntity = loginUser("1@gmail.com", "1", randomServerPort);
        //when
        ResponseEntity<MessageResponseDTO> responseEntity1 = deleteUser(responseEntity.getBody().getToken(), randomServerPort);
        //then
        Assert.assertEquals(200,responseEntity1.getStatusCodeValue());
        Assert.assertTrue(userRepository.existsByEmail("1@gmail.com"));
        Assert.assertTrue(userRepository.findByEmail("1@gmail.com").isDeleted());
    }

    @Test
    public void topicNotDeletedWhenDeletingUserTest() throws URISyntaxException {
        //GIVEN
        registerUser("deletedUser", "deletedUser@gmail.com", "password", "admin", randomServerPort);
        ResponseEntity<JwtResponseDTO> loginResponse = loginUser("deletedUser@gmail.com", "password", randomServerPort);

        String jwtToken = loginResponse.getBody().getToken();

        ResponseEntity<TopicResponseDTO> topicResponse = postTopic(jwtToken, "TopicNotDeleted I", "description", randomServerPort);

        //WHEN
        ResponseEntity<MessageResponseDTO> responseEntity1 = deleteUser(jwtToken, randomServerPort);
        //THEN
        Assert.assertEquals(200, responseEntity1.getStatusCodeValue());
        Assert.assertTrue(userRepository.existsByEmail("deletedUser@gmail.com"));
        Assert.assertTrue(userRepository.findByEmail("deletedUser@gmail.com").isDeleted());

        Optional<Topic> topic = topicRepository.findById(topicResponse.getBody().getId());
        Assert.assertTrue(topic.isPresent()); // topic remains in db
        Assert.assertFalse(topic.get().isDeleted()); // topic deleted prop remains as false
    }


    @Test
    public void postsNotDeletedWhenDeletingUserTest() throws URISyntaxException {
        //GIVEN
        registerUser("deletedUser2", "deletedUser2@gmail.com", "password", "admin", randomServerPort);
        ResponseEntity<JwtResponseDTO> loginResponse = loginUser("deletedUser2@gmail.com", "password", randomServerPort);

        String jwtToken = loginResponse.getBody().getToken();

        ResponseEntity<TopicResponseDTO> topicResponse = postTopic(jwtToken, "TopicNotDeleted II", "description", randomServerPort);

        ResponseEntity<PostResponseDTO> postResponse = postPost(jwtToken, "PostNotDeleted II", "description", "link", topicResponse.getBody().getId(), randomServerPort);

        //WHEN
        ResponseEntity<MessageResponseDTO> responseEntity1 = deleteUser(jwtToken, randomServerPort);
        //THEN
        Assert.assertEquals(200, responseEntity1.getStatusCodeValue());
        Assert.assertTrue(userRepository.existsByEmail("deletedUser2@gmail.com"));
        Assert.assertTrue(userRepository.findByEmail("deletedUser2@gmail.com").isDeleted());

        Optional<Post> post = postRepository.findById(postResponse.getBody().getId());
        Assert.assertTrue(post.isPresent()); // post remains in db
    }
}
