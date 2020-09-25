package agnesm.dev.controllers;

import agnesm.dev.exceptions.ApiException;
import org.slf4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.ResponseStatus;

@ControllerAdvice
public class ApiAdvice {

    @Autowired
    private final Logger logger;

    public ApiAdvice(Logger logger) {
        this.logger = logger;
    }

    @ResponseBody
    @ExceptionHandler(ApiException.class)
    @ResponseStatus(HttpStatus.BAD_GATEWAY)
    String apiHandler(ApiException ex) {
        logger.warn("Error when contacting Pokémon API: " + ex.getMessage());
        return ex.getMessage();
    }
}
