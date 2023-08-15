package com.app.library.exception.object;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(HttpStatus.BAD_REQUEST)//trạng thái trả về cho client
public class ObjectException extends RuntimeException{
    public ObjectException(String message) {
        super(message);
    }
}
