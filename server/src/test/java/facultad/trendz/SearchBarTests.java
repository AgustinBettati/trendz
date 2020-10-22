package facultad.trendz;

import facultad.trendz.dto.SearchResponseDTO;
import facultad.trendz.dto.topic.TopicResponseDTO;
import facultad.trendz.dto.user.JwtResponseDTO;
import facultad.trendz.repository.PostRepository;
import facultad.trendz.repository.TopicRepository;
import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.web.server.LocalServerPort;
import org.springframework.http.ResponseEntity;
import org.springframework.test.context.junit4.SpringRunner;

import java.net.URISyntaxException;

@RunWith(SpringRunner.class)
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
public class SearchBarTests extends TestUtils{

    @Autowired
    PostRepository postRepository;

    @Autowired
    TopicRepository topicRepository;

    @LocalServerPort
    int randomServerPort;

    @Test
    public void testSearchByTitle() throws URISyntaxException {
        //GIVEN
        ResponseEntity<JwtResponseDTO> loginResponse = loginUser("admin@gmail.com", "admin", randomServerPort);
        String jwt = loginResponse.getBody().getToken();

        ResponseEntity<TopicResponseDTO> topicResponse = postTopic(jwt, "test search Topic I", "description", randomServerPort);
        ResponseEntity<TopicResponseDTO> topicResponse2 = postTopic(jwt, "test search Topic II", "description", randomServerPort);
        ResponseEntity<TopicResponseDTO> topicResponse3 = postTopic(jwt, "test search Topic III", "description", randomServerPort);

        postPost(jwt, "test search Post I", "description", "testurl.com", topicResponse.getBody().getId(), randomServerPort);
        postPost(jwt, "test search Post II", "description", "testurl.com", topicResponse2.getBody().getId(), randomServerPort);
        postPost(jwt, "test search Post III", "description", "testurl.com", topicResponse3.getBody().getId(), randomServerPort);

        //WHEN
        ResponseEntity<SearchResponseDTO> response1 = searchByTitle("III", jwt, randomServerPort); // should return both topic #1 and post #1
        ResponseEntity<SearchResponseDTO> response2 = searchByTitle("test search topic", jwt, randomServerPort); // should return all topics
        ResponseEntity<SearchResponseDTO> response3 = searchByTitle("", jwt, randomServerPort); // should return all topics and posts
        ResponseEntity<SearchResponseDTO> response4 = searchByTitle("lorem ipsum", jwt, randomServerPort); // shouldn't return any post or topic

        //THEN
        Assert.assertEquals(200, response1.getStatusCodeValue());
        Assert.assertEquals(1, response1.getBody().getTopics().size());
        Assert.assertEquals(1, response1.getBody().getPosts().size());
        Assert.assertTrue(response1.getBody().getTopics().stream().anyMatch(post ->post.getTitle().equals("test search Topic III")));
        Assert.assertTrue(response1.getBody().getPosts().stream().anyMatch(post ->post.getTitle().equals("test search Post III")));


        Assert.assertEquals(200, response2.getStatusCodeValue());
        Assert.assertEquals(3, response2.getBody().getTopics().size());
        Assert.assertEquals(0, response2.getBody().getPosts().size());
        Assert.assertTrue(response2.getBody().getTopics().stream().anyMatch(post ->post.getTitle().equals("test search Topic I")));
        Assert.assertTrue(response2.getBody().getTopics().stream().anyMatch(post ->post.getTitle().equals("test search Topic II")));
        Assert.assertTrue(response2.getBody().getTopics().stream().anyMatch(post ->post.getTitle().equals("test search Topic III")));

        Assert.assertEquals(200, response3.getStatusCodeValue());
        Assert.assertTrue(response3.getBody().getTopics().size() >= 3); // could be bigger than 3 if run alongside with other tests
        Assert.assertTrue(response3.getBody().getPosts().size() >= 3);
        Assert.assertTrue(response3.getBody().getTopics().stream().anyMatch(post ->post.getTitle().equals("test search Topic I")));
        Assert.assertTrue(response3.getBody().getTopics().stream().anyMatch(post ->post.getTitle().equals("test search Topic II")));
        Assert.assertTrue(response3.getBody().getTopics().stream().anyMatch(post ->post.getTitle().equals("test search Topic III")));
        Assert.assertTrue(response3.getBody().getPosts().stream().anyMatch(post ->post.getTitle().equals("test search Post I")));
        Assert.assertTrue(response3.getBody().getPosts().stream().anyMatch(post ->post.getTitle().equals("test search Post II")));
        Assert.assertTrue(response3.getBody().getPosts().stream().anyMatch(post ->post.getTitle().equals("test search Post III")));

        Assert.assertEquals(200, response4.getStatusCodeValue());
        Assert.assertEquals(0, response4.getBody().getTopics().size());
        Assert.assertEquals(0, response4.getBody().getPosts().size());
    }
}
