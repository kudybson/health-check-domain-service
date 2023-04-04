package pl.akh.domainservicesvc.infrastructure.externalservices.oauth;

import jakarta.servlet.UnavailableException;
import pl.akh.model.rq.CreateUserRQ;

public interface OAuth2Service {
    void createUser(CreateUserRQ createUserRQ) throws UnavailableException;
}
