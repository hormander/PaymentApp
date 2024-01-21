package com.mycompany.myapp.web.rest.errors;

import org.springframework.http.HttpStatus;

public class Api000Exception extends AbstractApiException {

    public Api000Exception(final String description) {
        super(HttpStatus.FORBIDDEN, "API000", description);
    }
}
