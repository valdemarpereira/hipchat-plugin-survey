package com.valdemar.pool.resource;

import com.fasterxml.jackson.annotation.JsonInclude;
import org.springframework.hateoas.ResourceSupport;
import org.springframework.http.HttpStatus;

import java.util.List;

public final class Message<T> {

    private final HttpStatus status;
    private final String error;
    private final T message;

    private Message(HttpStatus status, String error, T message) {
        this.status = status;
        this.error = error;
        this.message = message;
    }

    public static Message of(HttpStatus status, String error, ResourceSupport message){
        return new Message(status, error, message);
    }

    public static Message ofError(HttpStatus status, String error){
        return new Message(status, error, null);
    }

    public static Message ofOK(ResourceSupport message){
        return new Message(HttpStatus.OK, null, message);
    }

    public static Message ofOK(List<?> message){
        return new Message(HttpStatus.OK, null, message);
    }

    public static Message ofNotFound(String errorMsg){
        return new Message(HttpStatus.NOT_FOUND, errorMsg, null);
    }

    public static Message ofBadRequest(String errorMsg){
        return new Message(HttpStatus.BAD_REQUEST, errorMsg, null);
    }

    public HttpStatus getStatus() {
        return status;
    }

    @JsonInclude(JsonInclude.Include.NON_NULL)
    public String getError() {
        return error;
    }

    @JsonInclude(JsonInclude.Include.NON_NULL)
    public T getMessage() {
        return message;
    }
}
