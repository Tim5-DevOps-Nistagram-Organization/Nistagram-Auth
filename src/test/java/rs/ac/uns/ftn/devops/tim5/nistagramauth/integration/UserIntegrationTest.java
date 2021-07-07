package rs.ac.uns.ftn.devops.tim5.nistagramauth.integration;


import org.junit.Test;
import org.junit.jupiter.api.Order;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.test.annotation.Rollback;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit4.SpringRunner;
import rs.ac.uns.ftn.devops.tim5.nistagramauth.constants.UserConstants;
import rs.ac.uns.ftn.devops.tim5.nistagramauth.dto.AccessTokenDTO;
import rs.ac.uns.ftn.devops.tim5.nistagramauth.dto.LoginDTO;
import rs.ac.uns.ftn.devops.tim5.nistagramauth.dto.UserDTO;
import rs.ac.uns.ftn.devops.tim5.nistagramauth.exception.ResourceNotFoundException;
import rs.ac.uns.ftn.devops.tim5.nistagramauth.model.Role;
import rs.ac.uns.ftn.devops.tim5.nistagramauth.model.User;
import rs.ac.uns.ftn.devops.tim5.nistagramauth.model.VerificationToken;
import rs.ac.uns.ftn.devops.tim5.nistagramauth.service.UserService;
import rs.ac.uns.ftn.devops.tim5.nistagramauth.service.VerificationTokenService;

import javax.transaction.Transactional;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.Objects;

import static org.junit.Assert.*;

@ActiveProfiles("test")
@RunWith(SpringRunner.class)
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@AutoConfigureMockMvc
@Transactional
public class UserIntegrationTest {

    @Autowired
    private TestRestTemplate restTemplate;

    @Autowired
    private UserService userService;

    @Autowired
    private VerificationTokenService verificationTokenService;


    /*
     *  Method test User send request for registration
     *  check if user is added and if is not verified and if role is REGULAR
     *
     *  Then automatically do verification and
     *  test if all data is good after verification
     *  Verification is done on service level because saga start in controller
     *  and for saga is needed all other service which participate in saga
     *
     *  @Rollback(false) because testUserLogin_Success tests login on registered user
     *
     * */
    @Test
    @Rollback(false)
    @Order(1)
    public void testRegistration_Success() throws ResourceNotFoundException, URISyntaxException {
        URI uri = new URI(UserConstants.USER_REGISTRATION_URI);
        UserDTO userDTO = new UserDTO(UserConstants.USERNAME, UserConstants.PASSWORD, UserConstants.EMAIL);
        HttpEntity<UserDTO> req = new HttpEntity<>(userDTO, new HttpHeaders());
        ResponseEntity<String> res = restTemplate.exchange(uri, HttpMethod.POST, req, String.class);

        assertEquals(res.getBody(), UserConstants.USER_SUCCESS_LOGIN_MESSAGE);
        User user = userService.findByEmail(UserConstants.EMAIL);
        assertFalse(user.isVerified());
        assertEquals(user.getRole().toString(), Role.ROLE_REGULAR.toString());

        // verify
        VerificationToken verificationToken = verificationTokenService
                .getVerificationTokenByUsername(user.getUsername());

        User verifiedUser = userService.verifiedUserEmail(verificationToken.getToken());
        assertTrue(verifiedUser.isVerified());
        assertEquals(verifiedUser.getRole().toString(), Role.ROLE_REGULAR.toString());
        assertEquals(verifiedUser.getUsername(), UserConstants.USERNAME);
        assertEquals(verifiedUser.getEmail(), UserConstants.EMAIL);

    }

    /*
     *   Method test User login.
     *   User send username and password and should get token in response
     *
     * */
    @Test
    @Order(2)
    public void testUserLogin_Success() throws URISyntaxException {
        URI uri = new URI(UserConstants.USER_LOGIN_URI);
        LoginDTO userLoginDTO = new LoginDTO(UserConstants.USERNAME, UserConstants.PASSWORD);
        HttpEntity<LoginDTO> req = new HttpEntity<>(userLoginDTO, new HttpHeaders());
        ResponseEntity<AccessTokenDTO> res = restTemplate.exchange(uri, HttpMethod.POST, req, AccessTokenDTO.class);
        assertNotEquals("", Objects.requireNonNull(res.getBody()).getToken());
    }

    /*
     *   Method test User login.
     *   User send invalid_username and password
     *   Login should return null
     *
     * */
    @Test
    @Order(3)
    public void testUserLogin_UserNotFound() throws URISyntaxException {
        URI uri = new URI(UserConstants.USER_LOGIN_URI);
        LoginDTO userLoginDTO = new LoginDTO(UserConstants.USERNAME_INVALID, UserConstants.PASSWORD);
        HttpEntity<LoginDTO> req = new HttpEntity<>(userLoginDTO, new HttpHeaders());
        ResponseEntity<AccessTokenDTO> res = restTemplate.exchange(uri, HttpMethod.POST, req, AccessTokenDTO.class);
        assertNull(Objects.requireNonNull(res.getBody()).getToken());
    }


}
