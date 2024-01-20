package com.mycompany.myapp.web.rest.errors;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(HttpStatus.FORBIDDEN)
public class Api000Exception extends AbstractApiException {

    public Api000Exception(String description) {
        super("API000", description);
    }
}
