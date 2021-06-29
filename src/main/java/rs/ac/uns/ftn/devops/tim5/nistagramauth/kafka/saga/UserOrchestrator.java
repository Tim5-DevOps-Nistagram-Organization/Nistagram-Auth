package rs.ac.uns.ftn.devops.tim5.nistagramauth.kafka.saga;

import com.google.gson.Gson;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;
import rs.ac.uns.ftn.devops.tim5.nistagramauth.kafka.Constants;
import rs.ac.uns.ftn.devops.tim5.nistagramauth.model.User;
import rs.ac.uns.ftn.devops.tim5.nistagramauth.model.kafka.UserMessage;

@Service
public class UserOrchestrator {

    private final KafkaTemplate<String, String> kafkaTemplate;
    private final Gson gson;

    @Autowired
    public UserOrchestrator(KafkaTemplate<String, String> kafkaTemplate, Gson gson) {
        this.kafkaTemplate = kafkaTemplate;
        this.gson = gson;
    }

    @Async
    public void startSaga(User user) {
        UserMessage message = new UserMessage(
                Constants.USER_TOPIC, Constants.USER_ORCHESTRATOR_TOPIC, Constants.START_ACTION,
                user.getUsername(), user.getEmail(), user.getWebsiteUrl());
        this.kafkaTemplate.send(message.getTopic(), gson.toJson(message));
    }

    @KafkaListener(topics = Constants.USER_ORCHESTRATOR_TOPIC, groupId = Constants.GROUP)
    public void getMessageOrchestrator(String msg) {
        UserMessage message = gson.fromJson(msg, UserMessage.class);
        if (message.getAction().equals(Constants.DONE_ACTION) &&
                !message.getReplayTopic().equals(Constants.SEARCH_TOPIC)) {
            if (message.getReplayTopic().equals(Constants.USER_TOPIC)) {
                message.setDetails(Constants.POST_TOPIC, Constants.USER_ORCHESTRATOR_TOPIC, Constants.START_ACTION);
            } else if (message.getReplayTopic().equals(Constants.POST_TOPIC)) {
                message.setDetails(Constants.SEARCH_TOPIC, Constants.USER_ORCHESTRATOR_TOPIC, Constants.START_ACTION);
            }
            kafkaTemplate.send(message.getTopic(), gson.toJson(message));
        } else if (message.getAction().equals(Constants.ERROR_ACTION) ||
                message.getAction().equals(Constants.ROLLBACK_DONE_ACTION)) {
            switch (message.getReplayTopic()) {
                case Constants.SEARCH_TOPIC:
                    message.setDetails(Constants.POST_TOPIC, Constants.USER_ORCHESTRATOR_TOPIC, Constants.ROLLBACK_ACTION);
                    break;
                case Constants.POST_TOPIC:
                    message.setDetails(Constants.USER_TOPIC, Constants.USER_ORCHESTRATOR_TOPIC, Constants.ROLLBACK_ACTION);
                    break;
                case Constants.USER_TOPIC:
                    message.setDetails(Constants.USER_AUTH_TOPIC, Constants.USER_ORCHESTRATOR_TOPIC, Constants.ROLLBACK_ACTION);
                    break;
            }
            kafkaTemplate.send(message.getTopic(), gson.toJson(message));
        }
    }
}
