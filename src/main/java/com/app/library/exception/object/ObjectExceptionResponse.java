package com.app.library.exception.object;

import lombok.Data;

@Data
public class ObjectExceptionResponse {
    private  String message ;
    public ObjectExceptionResponse(String message) {
        this.message = message;
    }
}
