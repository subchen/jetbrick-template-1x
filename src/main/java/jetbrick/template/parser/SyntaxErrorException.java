package jetbrick.template.parser;

public class SyntaxErrorException extends RuntimeException {
    private static final long serialVersionUID = 1L;

    public SyntaxErrorException() {
        super();
    }

    public SyntaxErrorException(String message, Throwable cause) {
        super(message, cause);
    }

    public SyntaxErrorException(String message) {
        super(message);
    }

    public SyntaxErrorException(Throwable cause) {
        super(cause);
    }
}
