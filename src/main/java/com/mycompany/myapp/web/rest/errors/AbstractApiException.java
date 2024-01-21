package com.mycompany.myapp.web.rest.errors;

import java.util.Map;
import org.springframework.http.HttpStatus;

public abstract class AbstractApiException extends RuntimeException {

    protected HttpStatus httpStatus;

    protected Map<String, Object> body;

    public AbstractApiException(final HttpStatus httpStatus, final String code, final String description) {
        super();
        this.httpStatus = httpStatus;

        this.body = Map.of("code", code, "description", description);
    }

    public Map<String, Object> getBody() {
        return this.body;
    }

    public HttpStatus geHttpStatus() {
        return this.httpStatus;
    }
}
