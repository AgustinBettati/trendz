package facultad.trendz.vote;

import facultad.trendz.TestUtils;
import facultad.trendz.dto.post.PostCreateDTO;
import facultad.trendz.dto.post.PostResponseDTO;
import facultad.trendz.dto.vote.VoteResponseDTO;
import facultad.trendz.dto.topic.TopicResponseDTO;
import facultad.trendz.dto.user.JwtResponseDTO;
import facultad.trendz.model.Post;
import facultad.trendz.model.User;
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
public class VoteTests  extends TestUtils {
    @LocalServerPort
    int randomServerPort;

    @Autowired
    PostRepository postRepository;
    @Autowired
    UserRepository userRepository;
    @Autowired
    VoteRepository voteRepository;

    @Test
    public void testUpvotePost() throws URISyntaxException {

        RestTemplate restTemplate = new RestTemplate();

        //lOGIN WITH ADMIN
        ResponseEntity<JwtResponseDTO> loginResponse = loginUser("admin@gmail.com", "admin", randomServerPort);

        String jwtToken = loginResponse.getBody().getToken();


        //post new Topic
        ResponseEntity<TopicResponseDTO> topicResponse = postTopic(jwtToken, "testTopicUpvotedPost", "test description", randomServerPort);

        //post new Post
        ResponseEntity<PostResponseDTO> postResponse = postPost(jwtToken, "testTitleUpvotedPost", "test description", "testLink.com", topicResponse.getBody().getId(), randomServerPort);




        HttpHeaders headers = new HttpHeaders();
        headers.add("Authorization", "Bearer " + jwtToken);


        HttpEntity entity = new HttpEntity<>(headers);
        final String url = "http://localhost:" + randomServerPort + "/post/"+postResponse.getBody().getId()+"/upvote";
        URI uri = new URI(url);


        //WHEN MAKING AN UPVOTE IN A POST
        ResponseEntity<VoteResponseDTO> response = restTemplate.exchange(uri, HttpMethod.POST, entity, VoteResponseDTO.class);
        //THEN
        Optional<Post> post = postRepository.findById(response.getBody().getPostId()); // Post also updated on db
        Assert.assertTrue(post.isPresent());
        Optional<User> user= userRepository.findById(response.getBody().getUserId()); // Post also updated on db
        Assert.assertTrue(post.isPresent());
        Assert.assertTrue(user.isPresent());
        Assert.assertEquals(201, response.getStatusCodeValue());
        Assert.assertEquals(postResponse.getBody().getId(), response.getBody().getPostId()); //keeps same id as original post


        Assert.assertTrue(this.voteRepository.findByPostIdAndUserIdAndIsUpvote(postResponse.getBody().getUserId(),postResponse.getBody().getId(),true).isPresent());
        Assert.assertEquals(user.get().getEmail(),"admin@gmail.com");



    }

    @Test
    public void testDownvotePost() throws URISyntaxException {

        RestTemplate restTemplate = new RestTemplate();

        //lOGIN WITH ADMIN
        ResponseEntity<JwtResponseDTO> loginResponse = loginUser("admin@gmail.com", "admin", randomServerPort);

        String jwtToken = loginResponse.getBody().getToken();


        //post new Topic
        ResponseEntity<TopicResponseDTO> topicResponse = postTopic(jwtToken, "testTopicDownvotedPost", "test description", randomServerPort);

        //post new Post
        ResponseEntity<PostResponseDTO> postResponse = postPost(jwtToken, "testTitleDownvotedPost", "test description", "testLink.com", topicResponse.getBody().getId(), randomServerPort);




        HttpHeaders headers = new HttpHeaders();
        headers.add("Authorization", "Bearer " + jwtToken);


        HttpEntity entity = new HttpEntity<>(headers);
        final String url = "http://localhost:" + randomServerPort + "/post/"+postResponse.getBody().getId()+"/downvote";
        URI uri = new URI(url);


        //WHEN MAKING AN UPVOTE IN A POST
        ResponseEntity<VoteResponseDTO> response = restTemplate.exchange(uri, HttpMethod.POST, entity, VoteResponseDTO.class);
        //THEN
        Optional<Post> post = postRepository.findById(response.getBody().getPostId()); // Post also updated on db
        Assert.assertTrue(post.isPresent());
        Optional<User> user= userRepository.findById(response.getBody().getUserId()); // Post also updated on db
        Assert.assertTrue(post.isPresent());
        Assert.assertTrue(user.isPresent());
        Assert.assertEquals(201, response.getStatusCodeValue());
        Assert.assertEquals(postResponse.getBody().getId(), response.getBody().getPostId()); //keeps same id as original post
        Assert.assertTrue(this.voteRepository.findByPostIdAndUserIdAndIsUpvote(postResponse.getBody().getUserId(),postResponse.getBody().getId(),!true).isPresent());
        Assert.assertEquals(user.get().getEmail(),"admin@gmail.com");




    }

    @Test
    public void testUpvotePostWhenPostIsDownVoted() throws URISyntaxException {

        RestTemplate restTemplate = new RestTemplate();

        //lOGIN WITH ADMIN
        ResponseEntity<JwtResponseDTO> loginResponse = loginUser("admin@gmail.com", "admin", randomServerPort);

        String jwtToken = loginResponse.getBody().getToken();


        //post new Topic
        ResponseEntity<TopicResponseDTO> topicResponse = postTopic(jwtToken, "testTopicUpvotedPost1", "test description", randomServerPort);

        //post new Post
        ResponseEntity<PostResponseDTO> postResponse = postPost(jwtToken, "testTitleUpvotedPost1", "test description", "testLink.com", topicResponse.getBody().getId(), randomServerPort);


        downvotePost(postResponse.getBody().getId(),jwtToken,randomServerPort);

        HttpHeaders headers = new HttpHeaders();
        headers.add("Authorization", "Bearer " + jwtToken);


        HttpEntity entity = new HttpEntity<>(headers);
        final String url = "http://localhost:" + randomServerPort + "/post/"+postResponse.getBody().getId()+"/upvote";
        URI uri = new URI(url);


        //WHEN MAKING AN UPVOTE IN A POST
        ResponseEntity<VoteResponseDTO> response = restTemplate.exchange(uri, HttpMethod.POST, entity, VoteResponseDTO.class);
        //THEN
        Optional<Post> post = postRepository.findById(response.getBody().getPostId()); // Post also updated on db
        Assert.assertTrue(post.isPresent());
        Optional<User> user= userRepository.findById(response.getBody().getUserId()); // Post also updated on db
        Assert.assertTrue(post.isPresent());
        Assert.assertTrue(user.isPresent());
        Assert.assertEquals(201, response.getStatusCodeValue());
        Assert.assertEquals(postResponse.getBody().getId(), response.getBody().getPostId()); //keeps same id as original post

        //verifying downvote has been deleted

        Assert.assertFalse(this.voteRepository.findByPostIdAndUserIdAndIsUpvote(postResponse.getBody().getUserId(),postResponse.getBody().getId(),!true).isPresent());

        Assert.assertTrue(this.voteRepository.findByPostIdAndUserIdAndIsUpvote(postResponse.getBody().getUserId(),postResponse.getBody().getId(),true).isPresent());
        Assert.assertEquals(user.get().getEmail(),"admin@gmail.com");




    }
    @Test
    public void testDownvotePostWhenPostIsUpvoted() throws URISyntaxException {

        RestTemplate restTemplate = new RestTemplate();

        //lOGIN WITH ADMIN
        ResponseEntity<JwtResponseDTO> loginResponse = loginUser("admin@gmail.com", "admin", randomServerPort);

        String jwtToken = loginResponse.getBody().getToken();


        //post new Topic
        ResponseEntity<TopicResponseDTO> topicResponse = postTopic(jwtToken, "testTopicDownvotedPost2", "test description", randomServerPort);

        //post new Post
        ResponseEntity<PostResponseDTO> postResponse = postPost(jwtToken, "testTitleDownvotedPost2", "test description", "testLink.com", topicResponse.getBody().getId(), randomServerPort);


    upvotePost(postResponse.getBody().getId(),jwtToken,randomServerPort);

        HttpHeaders headers = new HttpHeaders();
        headers.add("Authorization", "Bearer " + jwtToken);


        HttpEntity entity = new HttpEntity<>(headers);
        final String url = "http://localhost:" + randomServerPort + "/post/"+postResponse.getBody().getId()+"/downvote";
        URI uri = new URI(url);


        //WHEN MAKING AN UPVOTE IN A POST
        ResponseEntity<VoteResponseDTO> response = restTemplate.exchange(uri, HttpMethod.POST, entity, VoteResponseDTO.class);
        //THEN
        Optional<Post> post = postRepository.findById(response.getBody().getPostId()); // Post also updated on db
        Assert.assertTrue(post.isPresent());
        Optional<User> user= userRepository.findById(response.getBody().getUserId()); // Post also updated on db
        Assert.assertTrue(post.isPresent());
        Assert.assertTrue(user.isPresent());
        Assert.assertEquals(201, response.getStatusCodeValue());
        Assert.assertEquals(postResponse.getBody().getId(), response.getBody().getPostId()); //keeps same id as original post


       //veryfing upvote has been deleted
        Assert.assertFalse(this.voteRepository.findByPostIdAndUserIdAndIsUpvote(postResponse.getBody().getUserId(),postResponse.getBody().getId(),true).isPresent());


        Assert.assertTrue(this.voteRepository.findByPostIdAndUserIdAndIsUpvote(postResponse.getBody().getUserId(),postResponse.getBody().getId(),!true).isPresent());
        Assert.assertEquals(user.get().getEmail(),"admin@gmail.com");



    }









}
