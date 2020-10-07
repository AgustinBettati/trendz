package facultad.trendz.comment;

import facultad.trendz.TestUtils;
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
public class CommentTests extends TestUtils {

    @LocalServerPort
    int randomServerPort;

    @Autowired
    CommentRepository commentRepository;

    @Test
    public void testCreateComment() throws URISyntaxException {
        //GIVEN
        ResponseEntity<JwtResponseDTO> response = loginUser("admin@gmail.com", "admin", randomServerPort);
        String jwt = response.getBody().getToken();

        ResponseEntity<TopicResponseDTO> topicResponse = postTopic(jwt, "testCreateComment Topic", "description", randomServerPort);

        Long topicId = topicResponse.getBody().getId();

        ResponseEntity<PostResponseDTO> postResponse = postPost(jwt, "testCreateComment Post", "description", "testurl.com", topicId, randomServerPort);

        CommentCreateDTO comment = new CommentCreateDTO("test comment");

        //WHEN
        ResponseEntity<CommentResponseDTO> commentResponse = addCommentToPost(comment, postResponse.getBody().getId(), jwt, randomServerPort);

        //THEN
        Assert.assertEquals(201, commentResponse.getStatusCodeValue());
        Assert.assertEquals("test comment", commentResponse.getBody().getContent());
        Assert.assertEquals(postResponse.getBody().getId(), commentResponse.getBody().getPostId());

        Optional<Comment> foundComment = commentRepository.findById(commentResponse.getBody().getId());
        Assert.assertTrue(foundComment.isPresent());

        Assert.assertEquals("admin@gmail.com", foundComment.get().getUser().getEmail());
        Assert.assertEquals(postResponse.getBody().getId(), foundComment.get().getPost().getId());
    }

    @Test
    public void testCreateCommentWithInvalidPostId() throws URISyntaxException {
        //GIVEN
        ResponseEntity<JwtResponseDTO> response = loginUser("admin@gmail.com", "admin", randomServerPort);
        String jwt = response.getBody().getToken();

        CommentCreateDTO comment = new CommentCreateDTO("test comment");

        try {
            //WHEN
            addCommentToPost(comment, Long.MAX_VALUE, jwt, randomServerPort);
            //THEN
            Assert.fail();
        } catch (HttpClientErrorException e) {
            Assert.assertEquals(404, e.getRawStatusCode());
            Assert.assertTrue(e.getResponseBodyAsString().contains("Requested post not found"));
        }
    }

    @Test
    public void editComment() throws URISyntaxException {
        //GIVEN

        //login with user to create new post
        ResponseEntity<JwtResponseDTO> response = loginUser("admin@gmail.com", "admin", randomServerPort);
        String adminJwt = response.getBody().getToken();

        ResponseEntity<TopicResponseDTO> topicResponse = postTopic(adminJwt, "testEditComment Topic 2", "description", randomServerPort);

        Long topicId = topicResponse.getBody().getId();

        ResponseEntity<PostResponseDTO> postResponse = postPost(adminJwt, "testEditComment Post 2", "description", "testurl.com", topicId, randomServerPort);

        //add comment with regular user
        ResponseEntity<JwtResponseDTO> userResponse = loginUser("user@gmail.com", "user", randomServerPort);
        String userJwt = userResponse.getBody().getToken();

        CommentCreateDTO comment = new CommentCreateDTO("test comment");

        ResponseEntity<CommentResponseDTO> commentResponse = addCommentToPost(comment, postResponse.getBody().getId(), userJwt, randomServerPort);

        //WHEN
        ResponseEntity<CommentResponseDTO> editCommentResponse = editComment("edited test comment", userJwt, randomServerPort, commentResponse.getBody().getId());

        //THEN
        Assert.assertEquals(200, editCommentResponse.getStatusCodeValue());
        Assert.assertEquals("edited test comment", editCommentResponse.getBody().getContent());
        Assert.assertEquals(commentResponse.getBody().getId(), editCommentResponse.getBody().getId());
        Assert.assertEquals(postResponse.getBody().getId(), editCommentResponse.getBody().getPostId());
        Assert.assertEquals("user", editCommentResponse.getBody().getUsername());
        Assert.assertNotNull(editCommentResponse.getBody().getEditDate());

        Optional<Comment> foundComment = commentRepository.findById(editCommentResponse.getBody().getId());

        Assert.assertTrue(foundComment.isPresent());
        Assert.assertEquals("edited test comment", foundComment.get().getContent());
        Assert.assertEquals(commentResponse.getBody().getId(), foundComment.get().getId());
        Assert.assertEquals(postResponse.getBody().getId(), foundComment.get().getPost().getId());
        Assert.assertEquals("user", foundComment.get().getUser().getUsername());
        Assert.assertNotNull(foundComment.get().getEditDate());
    }

    @Test
    public void editCommentWithInvalidUser() throws URISyntaxException {
        //GIVEN

        //login with user to create new post
        ResponseEntity<JwtResponseDTO> response = loginUser("admin@gmail.com", "admin", randomServerPort);
        String adminJwt = response.getBody().getToken();

        ResponseEntity<TopicResponseDTO> topicResponse = postTopic(adminJwt, "testEditComment Topic 3", "description", randomServerPort);

        Long topicId = topicResponse.getBody().getId();

        ResponseEntity<PostResponseDTO> postResponse = postPost(adminJwt, "testEditComment Post 3", "description", "testurl.com", topicId, randomServerPort);

        CommentCreateDTO comment = new CommentCreateDTO("test comment");

        ResponseEntity<CommentResponseDTO> commentResponse = addCommentToPost(comment, postResponse.getBody().getId(), adminJwt, randomServerPort);
        Long commentId = commentResponse.getBody().getId();

        ResponseEntity<JwtResponseDTO> userResponse = loginUser("user@gmail.com", "user", randomServerPort);
        String userJwt = userResponse.getBody().getToken();


        try {
        //WHEN
            editComment("edited test comment", userJwt, randomServerPort, commentId);
        //THEN
            Assert.fail();
        } catch (HttpClientErrorException e){
            Assert.assertEquals(401, e.getRawStatusCode());
        }
    }
}
