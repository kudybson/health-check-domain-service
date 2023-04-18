package pl.akh.domainservicesvc.domain.exceptions;

public class DomainServiceException extends Exception {
    public DomainServiceException() {
    }

    public DomainServiceException(String message) {
        super(message);
    }

    public DomainServiceException(String message, Throwable cause) {
        super(message, cause);
    }

    public DomainServiceException(Throwable cause) {
        super(cause);
    }

    public DomainServiceException(String message, Throwable cause, boolean enableSuppression, boolean writableStackTrace) {
        super(message, cause, enableSuppression, writableStackTrace);
    }
}
