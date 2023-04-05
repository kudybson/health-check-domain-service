package pl.akh.model.rq;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import lombok.Builder;
import lombok.Data;
import pl.akh.model.common.Groups;

import java.io.Serializable;
import java.util.List;

@Data
@Builder
public class CreateUserRQ implements Serializable {
    @NotBlank
    private String username;
    @NotBlank
    private String password;
    @NotBlank
    private String passwordConfirmation;
    @Email
    private String email;
    @NotBlank
    private String firstName;
    @NotBlank
    private String lastName;
    @NotNull
    private Boolean enabled;
    @NotEmpty
    private List<Groups> groups;

}
