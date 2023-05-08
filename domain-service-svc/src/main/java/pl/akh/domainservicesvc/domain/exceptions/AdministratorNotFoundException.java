package pl.akh.domainservicesvc.domain.exceptions;

public class AdministratorNotFoundException extends DomainServiceException{

    public AdministratorNotFoundException() {
    }

    public AdministratorNotFoundException(String message) {
        super(message);
    }

    public AdministratorNotFoundException(String message, Throwable cause) {
        super(message, cause);
    }

    public AdministratorNotFoundException(Throwable cause) {
        super(cause);
    }

    public AdministratorNotFoundException(String message, Throwable cause, boolean enableSuppression, boolean writableStackTrace) {
        super(message, cause, enableSuppression, writableStackTrace);
    }
}
