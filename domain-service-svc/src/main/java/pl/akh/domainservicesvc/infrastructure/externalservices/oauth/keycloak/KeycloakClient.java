package pl.akh.domainservicesvc.infrastructure.externalservices.oauth.keycloak;

import jakarta.servlet.UnavailableException;
import lombok.extern.slf4j.Slf4j;
import org.keycloak.admin.client.Keycloak;
import org.keycloak.admin.client.KeycloakBuilder;
import org.keycloak.representations.AccessTokenResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;
import pl.akh.domainservicesvc.infrastructure.externalservices.oauth.OAuth2Service;
import pl.akh.model.rq.CreateUserRQ;

import java.util.List;

@Service
@Slf4j
@ConditionalOnProperty(prefix = "keycloak-client", name = "type", havingValue = "keycloak")
public class KeycloakClient implements OAuth2Service {

    private final String createUserPath = "/admin/realms/health-check/users";
    private KeycloakConfigProvider keycloakConfigProvider;
    private final Keycloak keycloak;
    private AccessTokenResponse accessToken;
    private Status lastCallStatus;

    @Autowired
    public KeycloakClient(KeycloakConfigProvider keycloakConfigProvider) {
        this.keycloakConfigProvider = keycloakConfigProvider;
        this.keycloak = KeycloakBuilder.builder()
                .serverUrl(keycloakConfigProvider.getKeycloakUrl())
                .clientId(keycloakConfigProvider.getClientId())
                .username(keycloakConfigProvider.getUsername())
                .password(keycloakConfigProvider.getPassword())
                .realm(keycloakConfigProvider.getRealm())
                .grantType("password")
                .build();

    }

    @Override
    public boolean createUser(CreateUserRQ createUserRQ) throws UnavailableException {
        RestTemplate restTemplate = new RestTemplate();
        HttpHeaders headers = new HttpHeaders();
        headers.set("Authorization", "Bearer " + getAccessToken());
        HttpEntity<CreateKeycloakUser> request = new HttpEntity<>(mapToKeycloakRequest(createUserRQ), headers);
        final String keycloakURL = keycloakConfigProvider.getKeycloakUrl() + createUserPath;
        ResponseEntity<Object> exchange = restTemplate.exchange(keycloakURL, HttpMethod.POST, request, Object.class);
        if (exchange.getStatusCode().is2xxSuccessful()) {
            log.info(exchange.toString());
            return true;
        }
        log.error("Error during creating user");
        return false;
    }

    private CreateKeycloakUser mapToKeycloakRequest(CreateUserRQ createUserRQ) {
        KeycloakCredentials credentials = KeycloakCredentials.builder()
                .temporary(true)
                .value(createUserRQ.getPassword())
                .type("password")
                .build();
        return CreateKeycloakUser.builder()
                .enabled(true)
                .groups(List.of(Groups.ADMIN_GROUP))
                .firstName(createUserRQ.getFirstName())
                .lastName(createUserRQ.getLastName())
                .email(createUserRQ.getEmail())
                .username(createUserRQ.getUsername())
                .credentials(List.of(credentials))
                .build();
    }

    private String getAccessToken() throws UnavailableException {
        if (accessToken == null || Status.ERROR.equals(lastCallStatus)) {
            this.accessToken = keycloak.tokenManager().getAccessToken();
        }
        if (accessToken.getError() != null) {
            this.lastCallStatus = Status.ERROR;
            throw new UnavailableException("Keycloak server is unavailable.");
        }
        this.lastCallStatus = Status.SUCCESS;
        return this.accessToken.getToken();

    }

    private enum Status {
        SUCCESS, ERROR
    }
}
