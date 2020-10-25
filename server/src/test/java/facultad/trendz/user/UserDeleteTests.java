package facultad.trendz.user;

import facultad.trendz.TestUtils;
import facultad.trendz.dto.MessageResponseDTO;
import facultad.trendz.dto.user.*;
import facultad.trendz.repository.UserRepository;
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

@RunWith(SpringRunner.class)
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
public class UserDeleteTests extends TestUtils {

    @LocalServerPort
    int randomServerPort;

    @Autowired
    private UserRepository userRepository;

    @Test
    public void successfullyDeleteUserTest() throws URISyntaxException {
        //given
        final String deleteUrl = "http://localhost:" + randomServerPort + "/user";
        URI deleteUri = new URI(deleteUrl);
        RestTemplate restTemplate= new RestTemplate();
        ResponseEntity<JwtResponseDTO> responseEntity=loginUser("1@gmail.com","1", randomServerPort);
        HttpHeaders deleteHeader= new HttpHeaders();
        deleteHeader.add("Authorization","Bearer "+responseEntity.getBody().getToken());
        HttpEntity<JwtResponseDTO> httpEntity=new HttpEntity<>(deleteHeader);
        //when
        ResponseEntity<MessageResponseDTO> responseEntity1= restTemplate.exchange(deleteUri, HttpMethod.DELETE,httpEntity, MessageResponseDTO.class);
        //then
        Assert.assertEquals(200,responseEntity1.getStatusCodeValue());
        Assert.assertTrue(userRepository.existsByEmail("1@gmail.com"));
        Assert.assertTrue(userRepository.findByEmail("1@gmail.com").isDeleted());
    }
}
