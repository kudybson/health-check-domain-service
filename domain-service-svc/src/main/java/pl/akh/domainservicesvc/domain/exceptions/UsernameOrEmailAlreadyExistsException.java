package pl.akh.domainservicesvc.domain.exceptions;

public class UsernameOrEmailAlreadyExistsException extends DomainServiceException {
    public UsernameOrEmailAlreadyExistsException() {
    }

    public UsernameOrEmailAlreadyExistsException(String message) {
        super(message);
    }

    public UsernameOrEmailAlreadyExistsException(String message, Throwable cause) {
        super(message, cause);
    }

    public UsernameOrEmailAlreadyExistsException(Throwable cause) {
        super(cause);
    }

    public UsernameOrEmailAlreadyExistsException(String message, Throwable cause, boolean enableSuppression, boolean writableStackTrace) {
        super(message, cause, enableSuppression, writableStackTrace);
    }
}
