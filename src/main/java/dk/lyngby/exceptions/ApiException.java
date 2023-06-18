package dk.lyngby.exceptions;

public class ApiException extends Exception{

    private final int statusCode;
    private Object object;

    public ApiException(int statusCode, String message, Throwable cause) {
        super(message, cause);
        this.statusCode = statusCode;
    }

    public ApiException(int statusCode, Object object, String message, Throwable cause) {
        super(message, cause);
        this.object = object;
        this.statusCode = statusCode;
    }

    public ApiException(String message, int statusCode) {
        super(message);
        this.statusCode = statusCode;
    }

    public int getStatusCode() {
        return statusCode;
    }

    public Object getObject() {
        return object;
    }
}
