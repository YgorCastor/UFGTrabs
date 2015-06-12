package exceptions;

public class InvalidQtdPlayersException extends Exception {

    public InvalidQtdPlayersException() {
        super();
    }

    public InvalidQtdPlayersException(String message) {
        super(message);
    }

    public InvalidQtdPlayersException(String message, Throwable cause) {
        super(message, cause);
    }

    public InvalidQtdPlayersException(Throwable cause) {
        super(cause);
    }

    protected InvalidQtdPlayersException(String message, Throwable cause, boolean enableSuppression, boolean writableStackTrace) {
        super(message, cause, enableSuppression, writableStackTrace);
    }
}
