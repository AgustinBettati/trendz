
package facultad.trendz.vote;

import facultad.trendz.TestUtils;
import facultad.trendz.dto.MessageResponseDTO;
import facultad.trendz.dto.post.PostCreateDTO;
import facultad.trendz.dto.post.PostResponseDTO;
import facultad.trendz.dto.vote.VoteResponseDTO;
import facultad.trendz.dto.topic.TopicResponseDTO;
import facultad.trendz.dto.user.JwtResponseDTO;
import facultad.trendz.model.Post;
import facultad.trendz.model.User;
import facultad.trendz.model.Vote;
import facultad.trendz.repository.PostRepository;
import facultad.trendz.repository.UserRepository;
import facultad.trendz.repository.VoteRepository;
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
import java.util.Optional;

@RunWith(SpringRunner.class)
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
public class VoteDeleteTests  extends TestUtils {
    @LocalServerPort
    int randomServerPort;

    @Autowired
    PostRepository postRepository;
    @Autowired
    UserRepository userRepository;
    @Autowired
    VoteRepository voteRepository;


    @Test
    public void testDeleteVoteWhenPostIsDownVoted() throws URISyntaxException {

        RestTemplate restTemplate = new RestTemplate();

        //lOGIN WITH ADMIN
        ResponseEntity<JwtResponseDTO> loginResponse = loginUser("admin@gmail.com", "admin", randomServerPort);

        String jwtToken = loginResponse.getBody().getToken();


        //post new Topic
        ResponseEntity<TopicResponseDTO> topicResponse = postTopic(jwtToken, "testTopicDeleteDownvote", "test description", randomServerPort);

        //post new Post
        ResponseEntity<PostResponseDTO> postResponse = postPost(jwtToken, "testTitleDeleteDownvote", "test description", "testLink.com", topicResponse.getBody().getId(), randomServerPort);


        downvotePost(postResponse.getBody().getId(),jwtToken,randomServerPort);

        HttpHeaders headers = new HttpHeaders();
        headers.add("Authorization", "Bearer " + jwtToken);


        HttpEntity entity = new HttpEntity<>(headers);
        final String url = "http://localhost:" + randomServerPort + "/vote/"+postResponse.getBody().getId();
        URI uri = new URI(url);


        //WHEN DELETING A DOWNVOTE
        ResponseEntity<MessageResponseDTO> response = restTemplate.exchange(uri, HttpMethod.DELETE, entity,MessageResponseDTO.class);
        //THEN


        Assert.assertEquals(200, response.getStatusCodeValue());


        //verifying downvote has been deleted

        Assert.assertFalse(this.voteRepository.findByPostIdAndUserIdAndIsUpvote(postResponse.getBody().getUserId(),postResponse.getBody().getId(),!true).isPresent());

    }

    @Test
    public void testDeleteVoteWhenPostIsUpVoted() throws URISyntaxException {

        RestTemplate restTemplate = new RestTemplate();

        //lOGIN WITH ADMIN
        ResponseEntity<JwtResponseDTO> loginResponse = loginUser("admin@gmail.com", "admin", randomServerPort);

        String jwtToken = loginResponse.getBody().getToken();


        //post new Topic
        ResponseEntity<TopicResponseDTO> topicResponse = postTopic(jwtToken, "testTopicDeleteUpvote", "test description", randomServerPort);

        //post new Post
        ResponseEntity<PostResponseDTO> postResponse = postPost(jwtToken, "testTitleDeleteUpvote", "test description", "testLink.com", topicResponse.getBody().getId(), randomServerPort);


        upvotePost(postResponse.getBody().getId(),jwtToken,randomServerPort);

        HttpHeaders headers = new HttpHeaders();
        headers.add("Authorization", "Bearer " + jwtToken);


        HttpEntity entity = new HttpEntity<>(headers);
        final String url = "http://localhost:" + randomServerPort + "/vote/"+postResponse.getBody().getId();
        URI uri = new URI(url);


        //WHEN DELETING AN UPVOTE
        ResponseEntity<MessageResponseDTO> response = restTemplate.exchange(uri, HttpMethod.DELETE, entity,MessageResponseDTO.class);
        //THEN


        Assert.assertEquals(200, response.getStatusCodeValue());


        //verifying upvote has been deleted

        Assert.assertFalse(this.voteRepository.findByPostIdAndUserIdAndIsUpvote(postResponse.getBody().getUserId(),postResponse.getBody().getId(),true).isPresent());

    }

}