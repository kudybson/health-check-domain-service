package pl.akh.domainservicesvc.utils.oauth.exceptions;

import jakarta.security.auth.message.AuthException;

public class UserNotAuthenticatedException extends AuthException {
    public UserNotAuthenticatedException() {
    }

    public UserNotAuthenticatedException(String msg) {
        super(msg);
    }

    public UserNotAuthenticatedException(String msg, Throwable cause) {
        super(msg, cause);
    }

    public UserNotAuthenticatedException(Throwable cause) {
        super(cause);
    }
}
