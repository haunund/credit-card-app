package process.common;

public class TechnicalRuntimeException extends RuntimeException {

    private static final long serialVersionUID = 1L;

    public TechnicalRuntimeException(String message) {
        super(message);
    }

    public TechnicalRuntimeException(String message, Throwable t) {
        super(message, t);
    }
}
