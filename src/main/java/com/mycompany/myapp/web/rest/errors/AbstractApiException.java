package com.mycompany.myapp.web.rest.errors;

import java.util.HashMap;
import java.util.Map;

public abstract class AbstractApiException extends RuntimeException {

    protected Map<String, Object> body = new HashMap<String, Object>();

    public AbstractApiException() {
        super();
    }

    public AbstractApiException(final String code, final String description) {
        super();
        this.body = Map.of("code", code, "description", description);
    }

    public Map<String, Object> getBody() {
        return this.body;
    }
}
