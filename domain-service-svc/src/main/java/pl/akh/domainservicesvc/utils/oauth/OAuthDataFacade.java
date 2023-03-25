package pl.akh.domainservicesvc.utils.oauth;

import jakarta.security.auth.message.AuthException;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import pl.akh.domainservicesvc.utils.oauth.exceptions.AuthenticationTypeNotSupportedException;
import pl.akh.domainservicesvc.utils.oauth.exceptions.UserNotAuthenticatedException;
import org.springframework.security.oauth2.jwt.Jwt;

import javax.naming.AuthenticationNotSupportedException;
import java.util.Collection;
import java.util.List;
import java.util.Map;

public class OAuthDataFacade {

    public boolean isAuthenticated() {
        try {
            Jwt auth = getAuth();
            return true;
        } catch (AuthException e) {
            return false;
        }
    }

    public String getId() throws AuthException {
        Jwt authentication = getAuth();
        return authentication.getClaimAsString("sub");
    }

    public String getUsername() throws AuthException {
        Jwt authentication = getAuth();
        return authentication.getClaimAsString("preferred_username");
    }

    public String getEmail() throws AuthException {
        Jwt authentication = getAuth();
        return authentication.getClaimAsString("email");
    }

    public String getFirstName() throws AuthException {
        Jwt authentication = getAuth();
        return authentication.getClaimAsString("given_name");
    }

    public String getLastname() throws AuthException {
        Jwt authentication = getAuth();
        return authentication.getClaimAsString("family_name");
    }

    public String getFullName() throws AuthException {
        Jwt authentication = getAuth();
        return authentication.getClaimAsString("name");
    }

    public Collection<String> getRoles() throws AuthException {
        Jwt authentication = getAuth();
        Map<String, List<String>> realmAccess = (Map<String, List<String>>) authentication.getClaim("realm_access");
        return realmAccess.get("roles");

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
