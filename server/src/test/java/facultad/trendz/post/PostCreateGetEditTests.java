package facultad.trendz.post;

import facultad.trendz.TestUtils;
import facultad.trendz.dto.post.PostCreateDTO;
import facultad.trendz.dto.post.PostEditDTO;
import facultad.trendz.dto.post.PostResponseDTO;
import facultad.trendz.dto.topic.TopicResponseDTO;
import facultad.trendz.dto.user.JwtResponseDTO;
import facultad.trendz.model.Post;
import facultad.trendz.repository.PostRepository;
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
public class PostCreateGetEditTests extends TestUtils {

    @LocalServerPort
    int randomServerPort;

    @Autowired
    PostRepository postRepository;

    @Test
    public void testPostCreation() throws URISyntaxException {
        //GIVEN
        ResponseEntity<JwtResponseDTO> loginResponse = loginUser("admin@gmail.com", "admin", randomServerPort);

        String jwtToken = loginResponse.getBody().getToken();

        //post new Topic
        ResponseEntity<TopicResponseDTO> topicResponse = postTopic(jwtToken, "testTopic4", "test description", randomServerPort);

        //WHEN
        ResponseEntity<PostResponseDTO> postResponse = postPost(jwtToken, "testTitle4", "test description", "testLink.com", topicResponse.getBody().getId(), randomServerPort);

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
        ResponseEntity<JwtResponseDTO> loginResponse = loginUser("admin@gmail.com", "admin", randomServerPort);

        String jwtToken = loginResponse.getBody().getToken();

        //post new Topic
        ResponseEntity<TopicResponseDTO> topicResponse = postTopic(jwtToken, "testTopic5", "test description", randomServerPort);

        //post new post
        postPost(jwtToken, "usedPostTitle", "test description", "testLink.com", topicResponse.getBody().getId(), randomServerPort);
        Long bodyId = topicResponse.getBody().getId();

        try {
            //WHEN trying to create a new post with an already used title
            postPost(jwtToken, "usedPostTitle", "test description", "testLink.com", bodyId, randomServerPort);

            //THEN
            Assert.fail();
        } catch (HttpClientErrorException e) {
            Assert.assertEquals(409, e.getRawStatusCode());
            Assert.assertTrue(e.getResponseBodyAsString().contains("Title usedPostTitle already in use"));
        }
    }

    public void testPostEdit() throws URISyntaxException {
        //GIVEN
        RestTemplate restTemplate = new RestTemplate();

        ResponseEntity<JwtResponseDTO> loginResponse = loginUser("admin@gmail.com", "admin", randomServerPort);

        String jwtToken = loginResponse.getBody().getToken();

        //post new Topic
        ResponseEntity<TopicResponseDTO> topicResponse = postTopic(jwtToken, "testTopic", "test description", randomServerPort);

        //post new Post
        ResponseEntity<PostResponseDTO> postResponse = postPost(jwtToken, "testTitle", "test description", "testLink.com", topicResponse.getBody().getId(), randomServerPort);

        HttpHeaders headers = new HttpHeaders();
        headers.add("Authorization", "Bearer " + jwtToken);

        PostEditDTO editedPost = new PostEditDTO("newTitle", "New description", "newLink.com");
        HttpEntity<PostEditDTO> postEditEntity = new HttpEntity<>(editedPost, headers);
        final String postEditUrl = String.format("http://localhost:%d/post/%d", randomServerPort, postResponse.getBody().getId());
        URI postEditUri = new URI(postEditUrl);

        //WHEN
        ResponseEntity<PostResponseDTO> response = restTemplate.exchange(postEditUri, HttpMethod.PUT, postEditEntity, PostResponseDTO.class);

        //THEN
        Assert.assertEquals(200, response.getStatusCodeValue());
        Assert.assertEquals(postResponse.getBody().getId(), response.getBody().getId()); //keeps same id as original post
        Assert.assertEquals("newTitle", response.getBody().getTitle()); //title is edited to new title
        Assert.assertEquals("New description", response.getBody().getDescription()); // description is edited to new description
        Assert.assertEquals("newLink.com", response.getBody().getLink()); // link is edited to new link

        Optional<Post> post = postRepository.findById(response.getBody().getId()); // Post also updated on db
        Assert.assertTrue(post.isPresent());
        Assert.assertEquals("newTitle", post.get().getTitle());
        Assert.assertEquals("New description", post.get().getDescription());
        Assert.assertEquals("newLink.com", post.get().getLink());
    }

    @Test
    public void testPostEditSingleValue() throws URISyntaxException {
        //GIVEN
        RestTemplate restTemplate = new RestTemplate();

        ResponseEntity<JwtResponseDTO> loginResponse = loginUser("admin@gmail.com", "admin", randomServerPort);

        String jwtToken = loginResponse.getBody().getToken();

        //post new Topic
        ResponseEntity<TopicResponseDTO> topicResponse = postTopic(jwtToken, "testTopic2", "test description", randomServerPort);

        //post new Post
        ResponseEntity<PostResponseDTO> postResponse = postPost(jwtToken, "testTitle2", "test description", "testLink.com", topicResponse.getBody().getId(), randomServerPort);

        HttpHeaders headers = new HttpHeaders();
        headers.add("Authorization", "Bearer " + jwtToken);

        PostEditDTO editedPost = new PostEditDTO("newTitle2", null, null);
        HttpEntity<PostEditDTO> postEditEntity = new HttpEntity<>(editedPost, headers);
        final String postEditUrl = String.format("http://localhost:%d/post/%d", randomServerPort, postResponse.getBody().getId());
        URI postEditUri = new URI(postEditUrl);

        //WHEN
        ResponseEntity<PostResponseDTO> response = restTemplate.exchange(postEditUri, HttpMethod.PUT, postEditEntity, PostResponseDTO.class);

        //THEN
        Assert.assertEquals(200, response.getStatusCodeValue());
        Assert.assertEquals(postResponse.getBody().getId(), response.getBody().getId()); //keeps same id as original post
        Assert.assertEquals("newTitle2", response.getBody().getTitle()); //title is edited to new title
        Assert.assertEquals("test description", response.getBody().getDescription()); // description remains unchanged
        Assert.assertEquals("testLink.com", response.getBody().getLink()); // link remains unchanged

        Optional<Post> post = postRepository.findById(response.getBody().getId()); // Post also updated on db
        Assert.assertTrue(post.isPresent());
        Assert.assertEquals("newTitle2", post.get().getTitle());
        Assert.assertEquals("test description", post.get().getDescription());
        Assert.assertEquals("testLink.com", post.get().getLink());
    }

    @Test
    public void testPostEditWithInvalidTitle() throws URISyntaxException {
        //GIVEN
        RestTemplate restTemplate = new RestTemplate();

        ResponseEntity<JwtResponseDTO> loginResponse = loginUser("admin@gmail.com", "admin", randomServerPort);

        String jwtToken = loginResponse.getBody().getToken();

        //post new Topic
        ResponseEntity<TopicResponseDTO> topicResponse = postTopic(jwtToken, "testTopic3", "test description", randomServerPort);

        //post new Post
        ResponseEntity<PostResponseDTO> postResponse = postPost(jwtToken, "testTitle3", "test description", "testLink.com", topicResponse.getBody().getId(), randomServerPort);
        //post another Post
        postPost(jwtToken, "usedTestTitle", "test description", "testLink.com", topicResponse.getBody().getId(), randomServerPort);

        HttpHeaders headers = new HttpHeaders();
        headers.add("Authorization", "Bearer " + jwtToken);

        PostEditDTO editedPost = new PostEditDTO("usedTestTitle", "description", "link");
        HttpEntity<PostEditDTO> postEditEntity = new HttpEntity<>(editedPost, headers);
        final String postEditUrl = String.format("http://localhost:%d/post/%d", randomServerPort, postResponse.getBody().getId());
        URI postEditUri = new URI(postEditUrl);

        try {
        //WHEN editing post with an already used title
            restTemplate.exchange(postEditUri, HttpMethod.PUT, postEditEntity, PostResponseDTO.class);
        //THEN
            Assert.fail();
        } catch (HttpClientErrorException e) {
            Assert.assertEquals(409, e.getRawStatusCode());
            Assert.assertTrue(e.getResponseBodyAsString().contains("Title usedTestTitle already in use"));
        }
    }

    @Test
    public void testPostEditWithInvalidId() throws URISyntaxException {
        //GIVEN
        RestTemplate restTemplate = new RestTemplate();

        ResponseEntity<JwtResponseDTO> loginResponse = loginUser("admin@gmail.com", "admin", randomServerPort);

        String jwtToken = loginResponse.getBody().getToken();

        HttpHeaders headers = new HttpHeaders();
        headers.add("Authorization", "Bearer " + jwtToken);

        PostEditDTO editedPost = new PostEditDTO("newTitle", "description", "link");
        HttpEntity<PostEditDTO> postEditEntity = new HttpEntity<>(editedPost, headers);
        final String postEditUrl = String.format("http://localhost:%d/post/%d", randomServerPort, Long.MAX_VALUE);
        URI postEditUri = new URI(postEditUrl);

        try {
        //WHEN
            restTemplate.exchange(postEditUri, HttpMethod.PUT, postEditEntity, PostResponseDTO.class);
        //THEN
            Assert.fail();
        } catch (HttpClientErrorException e) {
            Assert.assertEquals(404, e.getRawStatusCode());
            Assert.assertTrue(e.getResponseBodyAsString().contains("Requested post not found"));
        }
    }

    @Test
    public void testGetPost() throws URISyntaxException {
        //GIVEN
        ResponseEntity<JwtResponseDTO> loginResponse = loginUser("admin@gmail.com", "admin", randomServerPort);

        String jwtToken = loginResponse.getBody().getToken();

        //post new Topic
        ResponseEntity<TopicResponseDTO> topicResponse = postTopic(jwtToken, "testTopic6", "test description", randomServerPort);

        //post new post
        ResponseEntity<PostResponseDTO> postResponse = postPost(jwtToken, "testTitle6", "test description", "testLink.com", topicResponse.getBody().getId(), randomServerPort);

        RestTemplate restTemplate = new RestTemplate();

        HttpHeaders headers = new HttpHeaders();
        headers.add("Authorization", "Bearer " + jwtToken);


        HttpEntity<PostCreateDTO> getEntity = new HttpEntity<>(headers);

        final String url = "http://localhost:" + randomServerPort + "/post/"+postResponse.getBody().getId();
        URI uri = new URI(url);



        ResponseEntity<PostResponseDTO> response = restTemplate.exchange(uri, HttpMethod.GET, getEntity, new ParameterizedTypeReference<PostResponseDTO>() {});
        //THEN
        Assert.assertEquals(200, response.getStatusCodeValue());
        Assert.assertEquals("testTitle6", response.getBody().getTitle());
        Assert.assertEquals("test description", response.getBody().getDescription());
        Assert.assertEquals("testLink.com", response.getBody().getLink());
    }

    @Test
    public void testGetNonExistentPost() throws URISyntaxException {
        //GIVEN
        ResponseEntity<JwtResponseDTO> loginResponse = loginUser("admin@gmail.com", "admin", randomServerPort);

        String jwtToken = loginResponse.getBody().getToken();


        RestTemplate restTemplate = new RestTemplate();

        HttpHeaders headers = new HttpHeaders();
        headers.add("Authorization", "Bearer " + jwtToken);


        HttpEntity<PostCreateDTO> getEntity = new HttpEntity<>(headers);

        final String url = "http://localhost:" + randomServerPort + "/post/"+99;
        URI uri = new URI(url);

        try{ResponseEntity<PostResponseDTO> response = restTemplate.exchange(uri, HttpMethod.GET, getEntity, new ParameterizedTypeReference<PostResponseDTO>() {});}
        catch (HttpClientErrorException e){
            Assert.assertEquals("404",e.getMessage().substring(0,3) );

        }

    }
}
