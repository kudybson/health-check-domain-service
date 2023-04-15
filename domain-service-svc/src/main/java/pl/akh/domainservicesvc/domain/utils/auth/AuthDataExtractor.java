package pl.akh.domainservicesvc.domain.utils.auth;

import jakarta.security.auth.message.AuthException;

import java.util.Collection;
import java.util.Optional;
import java.util.UUID;

public interface AuthDataExtractor {
    boolean isAuthenticated();

    UUID getId() throws AuthException;

    Optional<String> getUsername() throws AuthException;

    Optional<String> getEmail() throws AuthException;

    Optional<String> getFirstName() throws AuthException;

    Optional<String> getLastname() throws AuthException;

    Optional<String> getFullName() throws AuthException;

    Collection<String> getRoles() throws AuthException;

    Optional<Boolean> isEmailVerified() throws AuthException;
}
