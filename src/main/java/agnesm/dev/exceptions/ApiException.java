package agnesm.dev.exceptions;

public class ApiException extends RuntimeException {

    public ApiException(int status, String body) {
        super("status: " + status + ", body: " + body);
    }
}
