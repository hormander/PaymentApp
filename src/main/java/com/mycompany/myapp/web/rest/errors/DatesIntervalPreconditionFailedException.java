package com.mycompany.myapp.web.rest.errors;

import org.springframework.http.HttpStatus;

public class DatesIntervalPreconditionFailedException extends AbstractApiException {

    public DatesIntervalPreconditionFailedException(final String description) {
        super(HttpStatus.PRECONDITION_FAILED, "API000", description);
    }
}
