package facultad.trendz.user;

import facultad.trendz.TestUtils;
import facultad.trendz.dto.comment.CommentCreateDTO;
import facultad.trendz.dto.post.SimplePostResponseDTO;
import facultad.trendz.dto.topic.TopicResponseDTO;
import facultad.trendz.dto.user.JwtResponseDTO;
import facultad.trendz.dto.user.UserInfoDTO;
import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
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
import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;

@RunWith(SpringRunner.class)
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
public class UserLastPostsTests extends TestUtils {

    @LocalServerPort
    int randomServerPort;

    @Test
    public void TestUserLastPosts() throws URISyntaxException {
        //GIVEN
        ResponseEntity<JwtResponseDTO> loginResponse = loginUser("admin@gmail.com", "admin", randomServerPort);
        String adminJwt = loginResponse.getBody().getToken();

        ResponseEntity<TopicResponseDTO> topicResponse = postTopic(adminJwt, "testUserLastPosts Topic", "description", randomServerPort);

        Long topicId = topicResponse.getBody().getId();

        postPost(adminJwt, "testUserLastPosts Post 1", "description", "testurl.com", topicId, randomServerPort);
        postPost(adminJwt, "testUserLastPosts Post 2", "description", "testurl.com", topicId, randomServerPort);
        postPost(adminJwt, "testUserLastPosts Post 3", "description", "testurl.com", topicId, randomServerPort);
        postPost(adminJwt, "testUserLastPosts Post 4", "description", "testurl.com", topicId, randomServerPort);
        postPost(adminJwt, "testUserLastPosts Post 5", "description", "testurl.com", topicId, randomServerPort);
        postPost(adminJwt, "testUserLastPosts Post 6", "description", "testurl.com", topicId, randomServerPort);

        RestTemplate restTemplate = new RestTemplate();
        final String url = "http://localhost:" + randomServerPort + "/user/lastposts";
        URI uri = new URI(url);
        HttpHeaders headers = new HttpHeaders();
        headers.add("Authorization", "Bearer " + adminJwt);
        HttpEntity<CommentCreateDTO> entity = new HttpEntity<>(headers);

        //WHEN
        ResponseEntity<List<SimplePostResponseDTO>> response = restTemplate.exchange(uri, HttpMethod.GET, entity, new ParameterizedTypeReference<List<SimplePostResponseDTO>>() {});

        //THEN
        Assert.assertEquals(200, response.getStatusCodeValue());

        List<SimplePostResponseDTO> responsePosts = response.getBody();
        List<SimplePostResponseDTO> sortedResponsePosts = responsePosts.stream().sorted(Comparator.comparing(SimplePostResponseDTO::getDate).reversed()).collect(Collectors.toList());
        Assert.assertEquals(sortedResponsePosts, responsePosts);

        Assert.assertEquals(5, responsePosts.size());
        Assert.assertEquals("testUserLastPosts Post 6",sortedResponsePosts.get(0).getTitle()); // newest
        Assert.assertEquals("testUserLastPosts Post 5",sortedResponsePosts.get(1).getTitle());
        Assert.assertEquals("testUserLastPosts Post 4",sortedResponsePosts.get(2).getTitle());
        Assert.assertEquals("testUserLastPosts Post 3",sortedResponsePosts.get(3).getTitle());
        Assert.assertEquals("testUserLastPosts Post 2",sortedResponsePosts.get(4).getTitle()); // 5th oldest
    }

    @Test
    public void TestUserByIdLastPosts() throws URISyntaxException {
        //GIVEN
        ResponseEntity<JwtResponseDTO> loginResponse = loginUser("admin@gmail.com", "admin", randomServerPort);
        String adminJwt = loginResponse.getBody().getToken();

        ResponseEntity<TopicResponseDTO> topicResponse = postTopic(adminJwt, "testUserByIdLastPosts Topic", "description", randomServerPort);

        Long topicId = topicResponse.getBody().getId();

        postPost(adminJwt, "testUserByIdLastPosts Post 1", "description", "testurl.com", topicId, randomServerPort);
        postPost(adminJwt, "testUserByIdLastPosts Post 2", "description", "testurl.com", topicId, randomServerPort);
        postPost(adminJwt, "testUserByIdLastPosts Post 3", "description", "testurl.com", topicId, randomServerPort);
        postPost(adminJwt, "testUserByIdLastPosts Post 4", "description", "testurl.com", topicId, randomServerPort);
        postPost(adminJwt, "testUserByIdLastPosts Post 5", "description", "testurl.com", topicId, randomServerPort);
        postPost(adminJwt, "testUserByIdLastPosts Post 6", "description", "testurl.com", topicId, randomServerPort);

        RestTemplate restTemplate = new RestTemplate();
        final String url = "http://localhost:" + randomServerPort + "/user/" + 5 ;
        URI uri = new URI(url);
        HttpHeaders headers = new HttpHeaders();
        headers.add("Authorization", "Bearer " + adminJwt);
        HttpEntity<CommentCreateDTO> entity = new HttpEntity<>(headers);

        //WHEN
        ResponseEntity<UserInfoDTO> response = restTemplate.exchange(uri, HttpMethod.GET, entity, UserInfoDTO.class);

        //THEN
        Assert.assertEquals(200, response.getStatusCodeValue());

        Assert.assertEquals("admin@gmail.com",response.getBody().getuserInfo().getEmail());

        List<SimplePostResponseDTO> responsePosts = response.getBody().getPosts();
        List<SimplePostResponseDTO> sortedResponsePosts = responsePosts.stream().sorted(Comparator.comparing(SimplePostResponseDTO::getDate).reversed()).collect(Collectors.toList());
        Assert.assertEquals(sortedResponsePosts, responsePosts);

        Assert.assertEquals(5, responsePosts.size());
        Assert.assertEquals("testUserByIdLastPosts Post 6",sortedResponsePosts.get(0).getTitle()); // newest
        Assert.assertEquals("testUserByIdLastPosts Post 5",sortedResponsePosts.get(1).getTitle());
        Assert.assertEquals("testUserByIdLastPosts Post 4",sortedResponsePosts.get(2).getTitle());
        Assert.assertEquals("testUserByIdLastPosts Post 3",sortedResponsePosts.get(3).getTitle());
        Assert.assertEquals("testUserByIdLastPosts Post 2",sortedResponsePosts.get(4).getTitle()); // 5th oldest
    }


}
