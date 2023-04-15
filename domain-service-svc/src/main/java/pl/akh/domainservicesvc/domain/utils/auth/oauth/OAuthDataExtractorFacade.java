package pl.akh.domainservicesvc.domain.utils.auth.oauth;

import jakarta.security.auth.message.AuthException;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import pl.akh.domainservicesvc.domain.utils.auth.AuthDataExtractor;
import pl.akh.domainservicesvc.domain.utils.auth.oauth.exceptions.AuthenticationTypeNotSupportedException;
import pl.akh.domainservicesvc.domain.utils.auth.oauth.exceptions.UserNotAuthenticatedException;
import org.springframework.security.oauth2.jwt.Jwt;

import java.util.*;

public class OAuthDataExtractorFacade implements AuthDataExtractor {

    @Override
    public boolean isAuthenticated() {
        try {
            getAuth();
            return true;
        } catch (AuthException e) {
            return false;
        }
    }

    @Override
    public UUID getId() throws AuthException {
        return UUID.fromString(getAuth().getClaimAsString("sub"));
    }

    @Override
    public Optional<String> getUsername() throws AuthException {
        return Optional.ofNullable(getAuth().getClaimAsString("preferred_username"));
    }

    @Override
    public Optional<String> getEmail() throws AuthException {
        return Optional.ofNullable(getAuth().getClaimAsString("email"));
    }

    @Override
    public Optional<String> getFirstName() throws AuthException {
        return Optional.ofNullable(getAuth().getClaimAsString("given_name"));
    }

    @Override
    public Optional<String> getLastname() throws AuthException {
        return Optional.ofNullable(getAuth().getClaimAsString("family_name"));
    }

    @Override
    public Optional<String> getFullName() throws AuthException {
        return Optional.ofNullable(getAuth().getClaimAsString("name"));
    }

    @Override
    public Collection<String> getRoles() throws AuthException {
        Jwt authentication = getAuth();
        Map<String, List<String>> realmAccess = (Map<String, List<String>>) authentication.getClaim("realm_access");
        return realmAccess.get("roles");

    }

    @Override
    public Optional<Boolean> isEmailVerified() throws AuthException {
        return Optional.ofNullable(getAuth().getClaimAsBoolean("email_verified"));
    }

    private Jwt getAuth() throws AuthException {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if (authentication == null) {
            throw new UserNotAuthenticatedException("Authentication is null.");
        }
        Object principal = authentication.getPrincipal();
        if (principal == null) {
            throw new UserNotAuthenticatedException("Principal is null.");
        }

        if (!(principal instanceof Jwt)) {
            throw new AuthenticationTypeNotSupportedException("Only JWT auth is supported.");
        }
        return (Jwt) principal;
    }
}
