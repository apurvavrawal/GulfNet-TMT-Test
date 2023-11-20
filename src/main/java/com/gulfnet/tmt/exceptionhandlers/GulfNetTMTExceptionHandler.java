package com.gulfnet.tmt.exceptionhandlers;

import com.gulfnet.tmt.exceptions.GulfNetSecurityException;
import com.gulfnet.tmt.exceptions.GulfNetTMTException;
import com.gulfnet.tmt.exceptions.ValidationException;
import com.gulfnet.tmt.model.response.ErrorDto;
import com.gulfnet.tmt.model.response.ResponseDto;
import io.jsonwebtoken.JwtException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.context.request.ServletWebRequest;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

import java.util.List;

@ControllerAdvice
public class GulfNetTMTExceptionHandler extends ResponseEntityExceptionHandler {

    @ExceptionHandler(GulfNetSecurityException.class)
    public ResponseEntity<Object> handleSecurityException(GulfNetSecurityException ex) {
        ErrorDto errorDto = ErrorDto.builder()
                .errorCode(ex.getErrorCode())
                .errorMessage(ex.getErrorMessage())
                .build();
        return generateResponseWithErrors(List.of(errorDto));
    }

    @ExceptionHandler(GulfNetTMTException.class)
    public ResponseEntity<Object> handleGulfNetTMTException(GulfNetTMTException ex) {
        ErrorDto errorDto = ErrorDto.builder()
                .errorCode(ex.getErrorCode())
                .errorMessage(ex.getErrorMessage())
                .build();
        return generateResponseWithErrors(List.of(errorDto));
    }

    @ExceptionHandler(ValidationException.class)
    public ResponseEntity<Object> handleValidationException(ValidationException ex) {
        ErrorDto errorDto = ErrorDto.builder()
                .errorCode(ex.getErrorCode())
                .errorMessage(ex.getErrorMessage())
                .build();
        return generateResponseWithErrors(List.of(errorDto));
    }

    @ExceptionHandler(value = {JwtException.class})
    public ResponseEntity<Object> handleJwtException(JwtException ex, WebRequest request) {
        String requestUri = ((ServletWebRequest)request).getRequest().getRequestURI();
        logger.error("Error in Authorization ", ex);
        ErrorDto errorDto = ErrorDto.builder()
                .errorCode(String.valueOf(HttpStatus.UNAUTHORIZED.value()))
                .errorMessage(ex.getMessage())
                .build();

        return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(
                ResponseDto.builder()
                        .status(1)
                        .errors(List.of(errorDto))
                        .build());
    }

    @ExceptionHandler(value = {Exception.class})
    public ResponseEntity<Object> handleGenericException(Exception ex, WebRequest request) {
        String requestUri = ((ServletWebRequest)request).getRequest().getRequestURI();
        logger.error("Error in handleGenericException ", ex);
        ErrorDto errorDto = ErrorDto.builder()
                .errorCode(String.valueOf(HttpStatus.INTERNAL_SERVER_ERROR.value()))
                .errorMessage(ex.getMessage())
                .build();
        return ResponseEntity.internalServerError().body(
                ResponseDto.builder()
                        .status(1)
                        .errors(List.of(errorDto))
                        .build());
    }

    @ExceptionHandler(value = { IllegalArgumentException.class, IllegalStateException.class })
    protected ResponseEntity<Object> handleConflict(RuntimeException ex, WebRequest request) {
        String requestUri = ((ServletWebRequest)request).getRequest().getRequestURI();
        logger.error("Error in handleConflict ", ex);
        ErrorDto errorDto = ErrorDto.builder()
                .errorCode(String.valueOf(HttpStatus.CONFLICT.value()))
                .errorMessage(ex.getMessage())
                .build();
        return ResponseEntity.status(HttpStatus.CONFLICT).body(
                ResponseDto.builder()
                        .status(1)
                        .errors(List.of(errorDto))
                        .build());
    }

    private ResponseEntity<Object> generateResponseWithErrors(List<ErrorDto> errors) {
        return ResponseEntity.ok().body(
                ResponseDto.builder()
                        .status(1)
                        .errors(errors)
                        .build());
    }
}
