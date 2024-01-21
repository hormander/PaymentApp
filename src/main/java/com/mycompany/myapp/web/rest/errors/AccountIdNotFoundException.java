package com.mycompany.myapp.web.rest.errors;

import org.springframework.http.HttpStatus;

public class AccountIdNotFoundException extends AbstractApiException {

    public AccountIdNotFoundException(final String description) {
        super(HttpStatus.NOT_FOUND, "API000", description);
    }
}
