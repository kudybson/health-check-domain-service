package pl.akh.domainservicesvc.infrastructure.externalservices.oauth;

import jakarta.servlet.UnavailableException;
import pl.akh.domainservicesvc.domain.exceptions.UsernameOrEmailAlreadyExistsException;

import java.util.Optional;
import java.util.UUID;

public interface OAuth2Service {
    boolean createUser(OAuth2User oauth2User) throws UnavailableException, UsernameOrEmailAlreadyExistsException;

    Optional<UUID> getUUIDByUsername(String username) throws UnavailableException;
    Optional<String> getEmailById(String username) throws UnavailableException;
}
