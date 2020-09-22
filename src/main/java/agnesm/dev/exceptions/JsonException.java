package agnesm.dev.exceptions;

public class JsonException extends RuntimeException {

    public JsonException(String message) {
        super("Could not read data from Pokémon API, message: " + message);
    }
}
