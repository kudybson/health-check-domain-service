package pl.akh.domainservicesvc.domain.exceptions;

public class ReceptionistNotFoundException extends DomainServiceException {

    public ReceptionistNotFoundException() {
    }

    public ReceptionistNotFoundException(String message) {
        super(message);
    }

    public ReceptionistNotFoundException(String message, Throwable cause) {
        super(message, cause);
    }

    public ReceptionistNotFoundException(Throwable cause) {
        super(cause);
    }

    public ReceptionistNotFoundException(String message, Throwable cause, boolean enableSuppression, boolean writableStackTrace) {
        super(message, cause, enableSuppression, writableStackTrace);
    }
}
