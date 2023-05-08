package pl.akh.domainservicesvc.domain.exceptions;

public class TreatmentNotFoundException extends DomainServiceException {

    public TreatmentNotFoundException() {
    }

    public TreatmentNotFoundException(String message) {
        super(message);
    }

    public TreatmentNotFoundException(String message, Throwable cause) {
        super(message, cause);
    }

    public TreatmentNotFoundException(Throwable cause) {
        super(cause);
    }

    public TreatmentNotFoundException(String message, Throwable cause, boolean enableSuppression, boolean writableStackTrace) {
        super(message, cause, enableSuppression, writableStackTrace);
    }
}
