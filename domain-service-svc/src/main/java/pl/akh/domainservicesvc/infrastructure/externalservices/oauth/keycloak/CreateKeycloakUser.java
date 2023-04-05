package pl.akh.domainservicesvc.infrastructure.externalservices.oauth.keycloak;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import lombok.Builder;
import lombok.Data;
import pl.akh.model.common.Groups;

import java.util.List;

@Data
@Builder
public class CreateKeycloakUser {
    private String username;
    private String email;
    private String firstName;
    private String lastName;
    private Boolean enabled;
    private List<Groups> groups;

    private List<KeycloakCredentials> credentials;
}
