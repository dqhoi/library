package com.app.library.exception.object;

import org.springframework.http.HttpStatus;

public class LibraryException extends RuntimeException{
    private final HttpStatus status;
    private final String message;


    public LibraryException(HttpStatus status, String message) {
        super();
        this.status = status;
        this.message = message;
    }
    public LibraryException(HttpStatus status, String message,Throwable exception) {
        super(exception);
        this.status = status;
        this.message = message;
    }
    public HttpStatus getStatus() {
        return status;
    }

    public String getMessage() {
        return message;
    }
}
