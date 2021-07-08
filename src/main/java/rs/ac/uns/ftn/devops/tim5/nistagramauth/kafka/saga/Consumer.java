package rs.ac.uns.ftn.devops.tim5.nistagramauth.kafka.saga;

import com.google.gson.Gson;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Service;
import rs.ac.uns.ftn.devops.tim5.nistagramauth.kafka.Constants;
import rs.ac.uns.ftn.devops.tim5.nistagramauth.model.kafka.UserMessage;
import rs.ac.uns.ftn.devops.tim5.nistagramauth.service.UserService;

import javax.mail.MessagingException;

@Service
public class Consumer {

    private final UserService userService;
    private final Gson gson;

    @Autowired
    public Consumer(UserService userService, Gson gson) {
        this.userService = userService;
        this.gson = gson;
    }

    @KafkaListener(topics = Constants.USER_AUTH_TOPIC, groupId = Constants.GROUP)
    public void getMessage(String msg) throws MessagingException {
        UserMessage message = gson.fromJson(msg, UserMessage.class);
        userService.deleteAndSendNotification(message.getUsername());
    }
}
