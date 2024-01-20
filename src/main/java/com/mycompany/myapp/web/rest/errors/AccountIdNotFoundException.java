package com.mycompany.myapp.web.rest.errors;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(HttpStatus.NOT_FOUND)
public class AccountIdNotFoundException extends AbstractApiException {

    public AccountIdNotFoundException(String description) {
        super("API000", description);
    }
}