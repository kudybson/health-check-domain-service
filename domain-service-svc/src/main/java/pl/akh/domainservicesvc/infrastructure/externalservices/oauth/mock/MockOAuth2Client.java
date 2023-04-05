package pl.akh.domainservicesvc.infrastructure.externalservices.oauth.mock;

import jakarta.servlet.UnavailableException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.stereotype.Service;
import pl.akh.domainservicesvc.infrastructure.externalservices.oauth.OAuth2Service;
import pl.akh.model.rq.CreateUserRQ;

@Slf4j
@Service
@ConditionalOnProperty(prefix = "keycloak-client", name = "type", havingValue = "mock")
public class MockOAuth2Client implements OAuth2Service {
    @Override
    public boolean createUser(CreateUserRQ createUserRQ) throws UnavailableException {
        log.info("User created.");
        return true;
    }
}
