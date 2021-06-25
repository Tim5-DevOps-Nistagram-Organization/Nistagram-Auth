package rs.ac.uns.ftn.devops.tim5.nistagramauth.kafka.security;

import com.google.gson.Gson;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.messaging.handler.annotation.SendTo;
import org.springframework.stereotype.Service;
import rs.ac.uns.ftn.devops.tim5.nistagramauth.kafka.Constants;
import rs.ac.uns.ftn.devops.tim5.nistagramauth.model.User;
import rs.ac.uns.ftn.devops.tim5.nistagramauth.model.kafka.UserSecurity;
import rs.ac.uns.ftn.devops.tim5.nistagramauth.security.TokenUtils;
import rs.ac.uns.ftn.devops.tim5.nistagramauth.service.UserService;

@Service
public class CheckToken {

    private final TokenUtils tokenUtils;
    private final UserService userService;
    private final Gson gson;

    @Autowired
    public CheckToken(TokenUtils tokenUtils, UserService userService, Gson gson) {
        this.tokenUtils = tokenUtils;
        this.userService = userService;
        this.gson = gson;
    }

    @KafkaListener(topics = Constants.AUTH_TOPIC, groupId = Constants.GROUP)
    @SendTo
    public String handle(String authToken) {
        UserSecurity userSecurity = new UserSecurity(null, null);
        String username = tokenUtils.getUsernameFromToken(authToken);
        if (username != null) {
            User user = userService.findByUsername(username);
            if (user != null && user.isVerified() && tokenUtils.validateToken(authToken, user.getUsername())) {
                userSecurity.setUsername(user.getUsername());
                userSecurity.setRole(user.getRole().toString());
            }
        }
        return gson.toJson(userSecurity);
    }
}
