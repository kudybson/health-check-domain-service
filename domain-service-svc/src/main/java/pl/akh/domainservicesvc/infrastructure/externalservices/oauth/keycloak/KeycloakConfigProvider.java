package pl.akh.domainservicesvc.infrastructure.externalservices.oauth.keycloak;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.stereotype.Component;

@Component
@ConditionalOnProperty(prefix = "keycloak-client", name = "type", havingValue = "keycloak")
public class KeycloakConfigProvider {
    @Value("${keycloak-client.url}")
    private String keycloakUrl;
    @Value("${keycloak-client.admin.username}")
    private String username;
    @Value("${keycloak-client.admin.password}")
    private String password;
    @Value("${keycloak-client.realm}")
    private String realm;
    @Value("${keycloak-client.client-id}")
    private String clientId;

    public String getKeycloakUrl() {
        return keycloakUrl;
    }

    public void setKeycloakUrl(String keycloakUrl) {
        this.keycloakUrl = keycloakUrl;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getRealm() {
        return realm;
    }

    public void setRealm(String realm) {
        this.realm = realm;
    }

    public String getClientId() {
        return clientId;
    }

    public void setClientId(String clientId) {
        this.clientId = clientId;
    }
}
