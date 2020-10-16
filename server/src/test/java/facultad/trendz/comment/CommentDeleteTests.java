package facultad.trendz.comment;

import facultad.trendz.TestUtils;
import facultad.trendz.dto.MessageResponseDTO;
import facultad.trendz.dto.comment.CommentCreateDTO;
import facultad.trendz.dto.comment.CommentResponseDTO;
import facultad.trendz.dto.post.PostResponseDTO;
import facultad.trendz.dto.topic.TopicResponseDTO;
import facultad.trendz.dto.user.JwtResponseDTO;
import facultad.trendz.model.Comment;
import facultad.trendz.repository.CommentRepository;
import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.web.server.LocalServerPort;
import org.springframework.http.ResponseEntity;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.web.client.HttpClientErrorException;

import java.net.URISyntaxException;
import java.util.Optional;

@RunWith(SpringRunner.class)
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
public class CommentDeleteTests extends TestUtils {

    @LocalServerPort
    int randomServerPort;

    @Autowired
    CommentRepository commentRepository;

    @Test
    public void testCommentDelete() throws URISyntaxException {
        //GIVEN
        ResponseEntity<JwtResponseDTO> loginResponse = loginUser("admin@gmail.com", "admin", randomServerPort);
        String adminJwt = loginResponse.getBody().getToken();

        ResponseEntity<TopicResponseDTO> topicResponse = postTopic(adminJwt, "testDeleteComment Topic", "description", randomServerPort);

        Long topicId = topicResponse.getBody().getId();

        ResponseEntity<PostResponseDTO> postResponse = postPost(adminJwt, "testDeleteComment Post", "description", "testurl.com", topicId, randomServerPort);

        CommentCreateDTO comment = new CommentCreateDTO("test comment");

        ResponseEntity<CommentResponseDTO> commentResponse = addCommentToPost(comment, postResponse.getBody().getId(), adminJwt, randomServerPort);

        //WHEN
        ResponseEntity<MessageResponseDTO> commentDeleteResponse = deleteComment(commentResponse.getBody().getId(), adminJwt, randomServerPort);

        //THEN
        Assert.assertEquals(200, commentDeleteResponse.getStatusCodeValue());
        Assert.assertTrue(commentDeleteResponse.getBody().getMessage().contains("Comment deleted successfully"));

        Optional<Comment> foundComment = commentRepository.findById(commentResponse.getBody().getId());
        Assert.assertTrue(foundComment.isPresent());
        Assert.assertTrue(foundComment.get().isDeleted());
    }

    @Test
    public void testDeleteCommentByInvalidUser() throws URISyntaxException {
        //GIVEN
        ResponseEntity<JwtResponseDTO> adminLoginResponse = loginUser("admin@gmail.com", "admin", randomServerPort);
        String adminJwt = adminLoginResponse.getBody().getToken();

        ResponseEntity<TopicResponseDTO> topicResponse = postTopic(adminJwt, "testDeleteComment Topic 2", "description", randomServerPort);

        Long topicId = topicResponse.getBody().getId();

        ResponseEntity<PostResponseDTO> postResponse = postPost(adminJwt, "testDeleteComment Post 2", "description", "testurl.com", topicId, randomServerPort);

        CommentCreateDTO comment = new CommentCreateDTO("test comment");

        ResponseEntity<CommentResponseDTO> commentResponse = addCommentToPost(comment, postResponse.getBody().getId(), adminJwt, randomServerPort);
        Long commentId = commentResponse.getBody().getId();

        ResponseEntity<JwtResponseDTO> userLoginResponse = loginUser("user@gmail.com", "user", randomServerPort);
        String userJwt = userLoginResponse.getBody().getToken();

        //WHEN
        try {
            deleteComment(commentId, userJwt, randomServerPort);

            //THEN
            Assert.fail();
        } catch (HttpClientErrorException e) {
            Assert.assertEquals(401, e.getRawStatusCode());
            Assert.assertTrue(e.getResponseBodyAsString().contains("Unauthorized access"));
        }
    }

    @Test
    public void testDeleteCommentByAdmin() throws URISyntaxException {
        //GIVEN
        //admin will create topic, post and delete comment created by user
        ResponseEntity<JwtResponseDTO> adminLoginResponse = loginUser("admin@gmail.com", "admin", randomServerPort);
        String adminJwt = adminLoginResponse.getBody().getToken();

        //user will create comment
        ResponseEntity<JwtResponseDTO> userLoginResponse = loginUser("admin@gmail.com", "admin", randomServerPort);
        String userJwt = userLoginResponse.getBody().getToken();

        ResponseEntity<TopicResponseDTO> topicResponse = postTopic(adminJwt, "testDeleteComment Topic 3", "description", randomServerPort);

        Long topicId = topicResponse.getBody().getId();

        ResponseEntity<PostResponseDTO> postResponse = postPost(adminJwt, "testDeleteComment Post 3", "description", "testurl.com", topicId, randomServerPort);

        CommentCreateDTO comment = new CommentCreateDTO("test comment");

        ResponseEntity<CommentResponseDTO> commentResponse = addCommentToPost(comment, postResponse.getBody().getId(), userJwt, randomServerPort);

        //WHEN
        ResponseEntity<MessageResponseDTO> commentDeleteResponse = deleteComment(commentResponse.getBody().getId(), adminJwt, randomServerPort);

        //THEN
        Assert.assertEquals(200, commentDeleteResponse.getStatusCodeValue());
        Assert.assertTrue(commentDeleteResponse.getBody().getMessage().contains("Comment deleted successfully"));

        Optional<Comment> foundComment = commentRepository.findById(commentResponse.getBody().getId());
        Assert.assertTrue(foundComment.isPresent());
        Assert.assertTrue(foundComment.get().isDeleted());
    }
}
