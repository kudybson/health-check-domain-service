package pl.akh.domainservicesvc.infrastructure.externalservices.oauth.keycloak;

import jakarta.servlet.UnavailableException;
import lombok.extern.slf4j.Slf4j;
import org.keycloak.admin.client.Keycloak;
import org.keycloak.admin.client.KeycloakBuilder;
import org.keycloak.admin.client.token.TokenManager;
import org.keycloak.admin.client.token.TokenService;
import org.keycloak.representations.AccessTokenResponse;
import org.keycloak.representations.idm.CredentialRepresentation;

import org.keycloak.representations.idm.UserRepresentation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;
import pl.akh.domainservicesvc.infrastructure.externalservices.oauth.Groups;
import pl.akh.domainservicesvc.infrastructure.externalservices.oauth.OAuth2Service;
import pl.akh.domainservicesvc.infrastructure.externalservices.oauth.CreateOauth2User;

import java.time.Instant;
import java.time.LocalDateTime;
import java.util.*;
import java.util.stream.Collectors;

@Service
@Slf4j
@ConditionalOnProperty(prefix = "keycloak-client", name = "type", havingValue = "keycloak")
public class KeycloakClient implements OAuth2Service {

    private final String createUserPath = "/admin/realms/health-check/users";
    private KeycloakConfigProvider keycloakConfigProvider;
    private final Keycloak keycloak;
    private AccessTokenResponse accessToken;
    private Status lastCallStatus;
    private Instant refreshTokenDate;

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
    public boolean createUser(CreateOauth2User createOauth2User) throws UnavailableException {
        RestTemplate restTemplate = new RestTemplate();
        HttpHeaders headers = new HttpHeaders();
        String token = getAccessToken();
        headers.set("Authorization", "Bearer " + token);
        HttpEntity<UserRepresentation> request = new HttpEntity<>(mapToUserRepresentation(createOauth2User), headers);
        final String keycloakURL = keycloakConfigProvider.getKeycloakUrl() + createUserPath;
        ResponseEntity<Object> exchange = restTemplate.exchange(keycloakURL, HttpMethod.POST, request, Object.class);
        if (exchange.getStatusCode().is2xxSuccessful()) {
            log.info(exchange.toString());
            return true;
        }
        log.error("Error during creating user");
        keycloak.tokenManager().invalidate(token);
        return false;
    }

    @Override
    public UUID getUUIDByUsername(String username) throws UnavailableException {
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
        org.keycloak.representations.account.UserRepresentation[] body = exchange.getBody();

        if (exchange.getStatusCode().is2xxSuccessful()) {
            if (body != null) {
                String id = Arrays.stream(body).filter(x -> Objects.equals(x.getUsername(), username))
                        .findFirst()
                        .orElseThrow(() -> new UnavailableException(""))
                        .getId();
                return UUID.fromString(id);
            }
        }
        log.error("Error during creating user");
        return null;
    }

    private UserRepresentation mapToUserRepresentation(CreateOauth2User createOauth2User) {
        UserRepresentation userRepresentation = new UserRepresentation();
        userRepresentation.setUsername(createOauth2User.getUsername());
        userRepresentation.setEmail(createOauth2User.getEmail());
        userRepresentation.setFirstName(createOauth2User.getFirstName());
        userRepresentation.setLastName(createOauth2User.getLastName());
        userRepresentation.setEmailVerified(false);
        userRepresentation.setEnabled(createOauth2User.isEnabled());
        userRepresentation.setGroups(createOauth2User.getGroups().stream().map(Groups::getGroup).collect(Collectors.toList()));

        CredentialRepresentation credentialRepresentation = new CredentialRepresentation();
        credentialRepresentation.setTemporary(true);
        credentialRepresentation.setType("password");
        credentialRepresentation.setValue(createOauth2User.getPassword());
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
