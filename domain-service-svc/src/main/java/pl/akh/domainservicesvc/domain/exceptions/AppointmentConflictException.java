package pl.akh.domainservicesvc.domain.exceptions;

public class AppointmentConflictException extends DomainServiceException {

    public AppointmentConflictException() {
    }

    public AppointmentConflictException(String message) {
        super(message);
    }

    public AppointmentConflictException(String message, Throwable cause) {
        super(message, cause);
    }

    public AppointmentConflictException(Throwable cause) {
        super(cause);
    }

    public AppointmentConflictException(String message, Throwable cause, boolean enableSuppression, boolean writableStackTrace) {
        super(message, cause, enableSuppression, writableStackTrace);
    }
}
