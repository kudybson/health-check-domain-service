package pl.akh.domainservicesvc.infrastructure.externalservices.oauth.keycloak;

import jakarta.servlet.UnavailableException;
import lombok.extern.slf4j.Slf4j;
import org.keycloak.admin.client.Keycloak;
import org.keycloak.admin.client.KeycloakBuilder;
import org.keycloak.admin.client.token.TokenManager;
import org.keycloak.representations.AccessTokenResponse;
import org.keycloak.representations.idm.CredentialRepresentation;

import org.keycloak.representations.idm.UserRepresentation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.http.*;
import org.springframework.stereotype.Service;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.RestTemplate;
import pl.akh.domainservicesvc.infrastructure.externalservices.oauth.Groups;
import pl.akh.domainservicesvc.infrastructure.externalservices.oauth.OAuth2Service;
import pl.akh.domainservicesvc.infrastructure.externalservices.oauth.OAuth2User;

import java.time.Instant;
import java.util.*;
import java.util.stream.Collectors;
import java.util.stream.Stream;

@Service
@Slf4j
@ConditionalOnProperty(prefix = "keycloak-client", name = "type", havingValue = "keycloak")
public class OAuth2KeycloakAdapter implements OAuth2Service {

    private final String createUserPath = "/admin/realms/health-check/users";
    private KeycloakConfigProvider keycloakConfigProvider;
    private final Keycloak keycloak;
    private AccessTokenResponse accessToken;
    private Status lastCallStatus;
    private Instant refreshTokenDate;

    @Autowired
    public OAuth2KeycloakAdapter(KeycloakConfigProvider keycloakConfigProvider) {
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
    public boolean createUser(OAuth2User oauth2User) throws UnavailableException {
        RestTemplate restTemplate = new RestTemplate();
        HttpHeaders headers = new HttpHeaders();
        String token = getAccessToken();
        headers.set("Authorization", "Bearer " + token);
        HttpEntity<UserRepresentation> request = new HttpEntity<>(mapToUserRepresentation(oauth2User), headers);
        final String keycloakURL = keycloakConfigProvider.getKeycloakUrl() + createUserPath;
        ResponseEntity<Object> exchange;
        try {
            exchange = restTemplate.exchange(keycloakURL, HttpMethod.POST, request, Object.class);
        } catch (HttpClientErrorException.Conflict e) {
            log.error("error: ", e);
            throw new UnsupportedOperationException("Given username or email already exists.");
        }
        if (exchange.getStatusCode().is2xxSuccessful()) {
            log.info(exchange.toString());
            return true;
        }
        if (exchange.getStatusCode() == HttpStatus.CONFLICT) {
            log.error(exchange.toString());
            throw new UnsupportedOperationException("Given username or email already exists.");
        }
        keycloak.tokenManager().invalidate(token);
        return false;
    }

    @Override
    public Optional<UUID> getUUIDByUsername(String username) throws UnavailableException {
        if (username == null) throw new IllegalArgumentException();
        RestTemplate restTemplate = new RestTemplate();
        HttpHeaders headers = new HttpHeaders();
        final String keycloakURL = keycloakConfigProvider.getKeycloakUrl() + createUserPath;
        headers.set("Authorization", "Bearer " + getAccessToken());
        HttpEntity<Object> httpEntity = new HttpEntity<>(headers);


        Map<String, String> params = new HashMap<>();
        params.put("username", username);

        ResponseEntity<org.keycloak.representations.account.UserRepresentation[]> exchange = restTemplate.exchange(keycloakURL,
                HttpMethod.GET,
                httpEntity,
                org.keycloak.representations.account.UserRepresentation[].class,
                params);

        if (exchange.getStatusCode().is2xxSuccessful()) {
            if (exchange.getBody() != null) {
                List<UUID> uuidStream = Arrays.stream(exchange.getBody())
                        .filter(keycloakUser -> Objects.nonNull(keycloakUser.getUsername()))
                        .filter(keycloakUser -> Objects.equals(keycloakUser.getUsername().toLowerCase(), username.toLowerCase()))
                        .map(org.keycloak.representations.account.UserRepresentation::getId)
                        .map(UUID::fromString)
                        .toList();
                return uuidStream.stream().findFirst();
            }
        }
        log.error("Error during creating user");
        throw new UnavailableException("Authorization service is unavailable");
    }

    @Override
    public Optional<String> getEmailById(String id) throws UnavailableException {
        if (id == null) throw new IllegalArgumentException();
        RestTemplate restTemplate = new RestTemplate();
        HttpHeaders headers = new HttpHeaders();
        final String keycloakURL = keycloakConfigProvider.getKeycloakUrl() + createUserPath + "/" +id;
        headers.set("Authorization", "Bearer " + getAccessToken());
        HttpEntity<Object> httpEntity = new HttpEntity<>(headers);

        ResponseEntity<org.keycloak.representations.account.UserRepresentation> exchange = restTemplate.exchange(keycloakURL,
                HttpMethod.GET,
                httpEntity,
                org.keycloak.representations.account.UserRepresentation.class);

        if (exchange.getStatusCode().is2xxSuccessful()) {
            if (exchange.getBody() != null) {
                return Optional.ofNullable(exchange.getBody())
                        .map(org.keycloak.representations.account.UserRepresentation::getEmail);
            }
        }
        log.error("Error during creating user");
        throw new UnavailableException("Authorization service is unavailable");
    }

    private UserRepresentation mapToUserRepresentation(OAuth2User oauth2User) {
        UserRepresentation userRepresentation = new UserRepresentation();
        userRepresentation.setUsername(oauth2User.getUsername());
        userRepresentation.setEmail(oauth2User.getEmail());
        userRepresentation.setFirstName(oauth2User.getFirstName());
        userRepresentation.setLastName(oauth2User.getLastName());
        userRepresentation.setEmailVerified(false);
        userRepresentation.setEnabled(oauth2User.isEnabled());
        userRepresentation.setGroups(oauth2User.getGroups().stream().map(Groups::getGroup).collect(Collectors.toList()));

        CredentialRepresentation credentialRepresentation = new CredentialRepresentation();
        credentialRepresentation.setTemporary(true);
        credentialRepresentation.setType("password");
        credentialRepresentation.setValue(oauth2User.getPassword());
        userRepresentation.setCredentials(List.of(credentialRepresentation));

        return userRepresentation;
    }

    private String getAccessToken() throws UnavailableException {
        TokenManager tokenManager = keycloak.tokenManager();
        String accessToken = tokenManager.getAccessTokenString();
        if (accessToken == null || accessToken.isEmpty()) {
            try {
                tokenManager.refreshToken();
                accessToken = tokenManager.getAccessTokenString();
            } catch (Exception e) {
                throw new UnavailableException("Unable to refresh access token: " + e.getMessage());
            }
        }
        if (accessToken == null || accessToken.isEmpty()) {
            throw new UnavailableException("Keycloak server is unavailable.");
        }
        return accessToken;
    }

    private enum Status {
        SUCCESS, ERROR
    }
}
