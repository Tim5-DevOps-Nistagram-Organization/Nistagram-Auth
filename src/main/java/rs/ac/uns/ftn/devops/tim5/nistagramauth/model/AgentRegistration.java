package rs.ac.uns.ftn.devops.tim5.nistagramauth.model;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import rs.ac.uns.ftn.devops.tim5.nistagramauth.model.enums.AgentRegistrationEnum;

import javax.persistence.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Entity
public class AgentRegistration {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;
    private String username;
    private String email;
    private String websiteUrl;
    @Enumerated(EnumType.STRING)
    private AgentRegistrationEnum state;
}
