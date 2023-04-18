package pl.akh.domainservicesvc.domain.exceptions;

public class DepartmentNotFountException extends DomainServiceException {
    public DepartmentNotFountException() {
    }

    public DepartmentNotFountException(String message) {
        super(message);
    }

    public DepartmentNotFountException(String message, Throwable cause) {
        super(message, cause);
    }

    public DepartmentNotFountException(Throwable cause) {
        super(cause);
    }

    public DepartmentNotFountException(String message, Throwable cause, boolean enableSuppression, boolean writableStackTrace) {
        super(message, cause, enableSuppression, writableStackTrace);
    }
}
