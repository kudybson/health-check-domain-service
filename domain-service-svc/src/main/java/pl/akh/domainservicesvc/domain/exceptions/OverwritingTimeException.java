package pl.akh.domainservicesvc.domain.exceptions;

public class OverwritingTimeException extends Exception {
    public OverwritingTimeException() {
    }

    public OverwritingTimeException(String message) {
        super(message);
    }

    public OverwritingTimeException(String message, Throwable cause) {
        super(message, cause);
    }

    public OverwritingTimeException(Throwable cause) {
        super(cause);
    }

    public OverwritingTimeException(String message, Throwable cause, boolean enableSuppression, boolean writableStackTrace) {
        super(message, cause, enableSuppression, writableStackTrace);
    }
}
