package rs.ac.uns.ftn.devops.tim5.nistagramauth.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Pattern;
import javax.validation.constraints.Size;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class LoginDTO {
    @NotNull(message = "Username can not be null")
    @NotBlank(message = "Username can not be blank")
    private String username;

    @NotNull(message = "Password is required")
    @Size(min = 8, message = "Password min length is 8")
    @Pattern(regexp = ".*[0-9].*", message = "Password must have at least one number")
    @Pattern(regexp = ".*[a-z].*", message = "Password must have at least one lowercase letter")
    @Pattern(regexp = ".*[A-Z].*", message = "Password must have at least one uppercase letter")
    private String password;
}
