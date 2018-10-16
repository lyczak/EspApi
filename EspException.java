public class EspException extends Exception {
    private String errorCode;

    public EspException(String message, String errorCode) {
        super(message);
        this.errorCode = errorCode;
    }

    public String getErrorCode() {
        return this.errorCode;
    }
}
