package com.mycompany.myapp.web.rest.errors;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(HttpStatus.PRECONDITION_FAILED)
public class DatesIntervalPreconditionFailed extends AbstractApiException {

    public DatesIntervalPreconditionFailed(String description) {
        super("API000", description);
    }
}
