package pl.akh.domainservicesvc.utils.oauth.exceptions;

import jakarta.security.auth.message.AuthException;

public class AuthenticationTypeNotSupportedException extends AuthException {
    public AuthenticationTypeNotSupportedException() {
    }

    public AuthenticationTypeNotSupportedException(String msg) {
        super(msg);
    }
}
