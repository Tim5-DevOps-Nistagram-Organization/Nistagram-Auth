package rs.ac.uns.ftn.devops.tim5.nistagramauth.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.validation.constraints.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class UserDTO {
    @NotNull(message = "Username can not be null")
    @NotBlank(message = "Username can not be blank")
    private String username;

    @NotNull(message = "Password is required")
    @Size(min = 8, message = "Password min length is 8")
    @Pattern(regexp = ".*[0-9].*", message = "Password must have at least one number")
    @Pattern(regexp = ".*[a-z].*", message = "Password must have at least one lowercase letter")
    @Pattern(regexp = ".*[A-Z].*", message = "Password must have at least one uppercase letter")
    private String password;


    @NotNull(message = "Email can not be null")
    @NotBlank(message = "Email can not be blank")
    @Email(message="Email should be valid")
    private String email;
}
