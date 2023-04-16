package pl.akh.domainservicesvc.infrastructure.externalservices.oauth.keycloak;

import lombok.Builder;
import lombok.Data;

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
