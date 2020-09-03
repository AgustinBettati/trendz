package facultad.trendz;

import facultad.trendz.dto.JwtResponseDTO;
import facultad.trendz.dto.LoginDTO;
import facultad.trendz.dto.UserCreateDTO;
import facultad.trendz.dto.UserResponseDTO;
import facultad.trendz.model.ERole;
import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
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
public class UserProfileTests {

    @LocalServerPort
    int randomServerPort;


    @Test
    public void testUserProfileData() throws URISyntaxException {
        //GIVEN
        RestTemplate restTemplate = new RestTemplate();

        ResponseEntity<UserResponseDTO> registerResponse = registerUser(
                "testUsername08", "testEmail08@gmail.com", "testPassword", "user"); // Post new user

        ResponseEntity<JwtResponseDTO> loginResponse = loginUser("testEmail08@gmail.com", "testPassword"); // Login with new User to get JWT

        String jwtToken = loginResponse.getBody().getToken();
        long userId = registerResponse.getBody().getId();

        HttpHeaders headers = new HttpHeaders();
        headers.add("Authorization", "Bearer " + jwtToken);
        HttpEntity<String> entity = new HttpEntity<>(headers);

        final String getProfileUrl = String.format("http://localhost:%d/user/%d", randomServerPort, userId);
        URI getProfileUri = new URI(getProfileUrl);

        //WHEN
        ResponseEntity<UserResponseDTO> response = restTemplate.exchange(getProfileUri, HttpMethod.GET, entity, UserResponseDTO.class); // get new user profile data, used exchange() method to add Authorization header

        //THEN
        Assert.assertEquals(200, response.getStatusCodeValue());
        Assert.assertNotNull(response.getBody());
        Assert.assertEquals("testEmail08@gmail.com", response.getBody().getEmail());
        Assert.assertEquals("testUsername08", response.getBody().getUsername());
        Assert.assertEquals(ERole.ROLE_USER, response.getBody().getRole().getEnumRole());

    }

    @Test
    public void testInvalidUserProfileData() throws URISyntaxException {
        //GIVEN
        RestTemplate restTemplate = new RestTemplate();

        registerUser("testUsername09", "testEmail09@gmail.com", "testPassword", "user"); // Post new user

        ResponseEntity<JwtResponseDTO> loginResponse = loginUser("testEmail09@gmail.com", "testPassword"); // Login with new User to get JWT

        String jwtToken = loginResponse.getBody().getToken();
        Long userId = Long.MAX_VALUE; // unused id

        HttpHeaders headers = new HttpHeaders();
        headers.add("Authorization", "Bearer " + jwtToken);
        HttpEntity<String> entity = new HttpEntity<>(headers);

        final String getProfileUrl = String.format("http://localhost:%d/user/%d", randomServerPort, userId);
        URI getProfileUri = new URI(getProfileUrl);

        try {
        //WHEN
            restTemplate.exchange(getProfileUri, HttpMethod.GET, entity, UserResponseDTO.class);// getting profile data for invalid userId

        //THEN
            Assert.fail();
        } catch (HttpClientErrorException e) {

            Assert.assertEquals(404, e.getRawStatusCode());
            Assert.assertTrue(e.getResponseBodyAsString().contains("Requested user not found"));
        }

    }

    private ResponseEntity<UserResponseDTO> registerUser(String username, String email, String password, String role) throws URISyntaxException {
        RestTemplate restTemplate = new RestTemplate();
        final String registerUrl = "http://localhost:" + randomServerPort + "/user";
        URI registerUri = new URI(registerUrl);
        HttpHeaders registerHeaders = new HttpHeaders();
        UserCreateDTO userCreateDTO = new UserCreateDTO(email, username, password, role);
        HttpEntity<UserCreateDTO> registerRequest = new HttpEntity<>(userCreateDTO, registerHeaders);
        return restTemplate.postForEntity(registerUri, registerRequest, UserResponseDTO.class);
    }

    private ResponseEntity<JwtResponseDTO> loginUser(String email, String password) throws URISyntaxException {
        RestTemplate restTemplate = new RestTemplate();
        final String loginUrl = "http://localhost:" + randomServerPort + "/login";
        URI loginUri = new URI(loginUrl);
        HttpHeaders loginHeaders = new HttpHeaders();
        LoginDTO loginDTO = new LoginDTO(email, password);
        HttpEntity<LoginDTO> loginRequest = new HttpEntity<>(loginDTO, loginHeaders);
        return restTemplate.postForEntity(loginUri, loginRequest, JwtResponseDTO.class);
    }


}
