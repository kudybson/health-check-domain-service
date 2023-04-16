package pl.akh.model.rq;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class CreateUserRQ {
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

}
