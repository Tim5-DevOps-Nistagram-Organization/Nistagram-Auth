package rs.ac.uns.ftn.devops.tim5.nistagramauth.model.kafka;


import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
public class UserMessage extends Message {
    private String username;
    private String email;
    private String websiteUrl;

    public UserMessage(String topic, String replayTopic, String action,
                       String username, String email, String websiteUrl) {
        super(topic, replayTopic, action);
        this.username = username;
        this.email = email;
        this.websiteUrl = websiteUrl;
    }
}
