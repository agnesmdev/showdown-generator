package agnesm.dev.exceptions;

public class ApiException extends RuntimeException {

    public ApiException(int status, String body) {
        super("Could not contact Pokémon API, status: " + status + ", body: " + body);
    }
}
