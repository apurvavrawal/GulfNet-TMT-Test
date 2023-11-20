package com.gulfnet.tmt.exceptions;

import lombok.Getter;

@Getter
public class ValidationException extends RuntimeException {

    private final String errorCode;
    private final String errorMessage;

    public ValidationException(String errorCode, String errorMessage) {
        this.errorCode = errorCode;
        this.errorMessage = errorMessage;
    }
}
