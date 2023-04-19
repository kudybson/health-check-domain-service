package pl.akh.model.rq;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class AdministratorRQ {
    @NotNull
    private Long departmentId;
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
