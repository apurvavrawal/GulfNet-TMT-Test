package com.gulfnet.tmt.exceptions;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@RequiredArgsConstructor
public class GulfNetSecurityException extends RuntimeException {

    private final String errorCode;
    private final String errorMessage;
}
