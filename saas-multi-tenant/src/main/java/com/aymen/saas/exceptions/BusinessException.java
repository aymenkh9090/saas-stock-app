package com.aymen.saas.exceptions;

import lombok.Getter;

@Getter
public class BusinessException extends RuntimeException {

    private String message;

    public BusinessException(final String message) {
        super(message);
        this.message = message;
    }


}
