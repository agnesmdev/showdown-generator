package agnesm.dev.controllers;

import agnesm.dev.exceptions.JsonException;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.ResponseStatus;

import javax.inject.Inject;
import org.slf4j.Logger;

@ControllerAdvice
public class JsonAdvice {

    private Logger logger;

    public JsonAdvice(Logger logger) {
        this.logger = logger;
    }

    @ResponseBody
    @ExceptionHandler(JsonException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    String jsonHandler(JsonException ex) {
        logger.error("Could not read data from Pokémon API, message: " + ex.getMessage());
        return ex.getMessage();
    }
}
