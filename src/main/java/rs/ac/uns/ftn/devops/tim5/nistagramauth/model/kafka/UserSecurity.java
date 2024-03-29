package rs.ac.uns.ftn.devops.tim5.nistagramauth.model.kafka;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class UserSecurity {

    private String username;
    private String role;

}
