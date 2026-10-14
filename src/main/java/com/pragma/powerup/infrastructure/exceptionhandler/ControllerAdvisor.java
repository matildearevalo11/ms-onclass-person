package com.pragma.powerup.infrastructure.exceptionhandler;

import com.pragma.powerup.domain.exception.AuthenticationException;
import com.pragma.powerup.domain.exception.ExceptionMessages;
import lombok.extern.slf4j.Slf4j;
import org.springframework.dao.DataAccessException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.server.ServerWebInputException;
import org.springframework.web.bind.support.WebExchangeBindException;

@Slf4j
@RestControllerAdvice
public class ControllerAdvisor {

    @ExceptionHandler(AuthenticationException.class)
    public ResponseEntity<ErrorResponse> unauthorized(AuthenticationException exception) {
        return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(ErrorResponse.from(exception.getMessage()));
    }

    @ExceptionHandler(WebExchangeBindException.class)
    public ResponseEntity<ErrorResponse> invalidCredentialsRequest() {
        return ResponseEntity.badRequest().body(ErrorResponse.from(ExceptionMessages.CREDENTIALS_REQUIRED.getMessage()));
    }

    @ExceptionHandler(ServerWebInputException.class)
    public ResponseEntity<ErrorResponse> invalidRequest() {
        return ResponseEntity.badRequest().body(ErrorResponse.from(ExceptionMessages.INVALID_REQUEST.getMessage()));
    }

    @ExceptionHandler(DataAccessException.class)
    public ResponseEntity<ErrorResponse> persistenceFailure(DataAccessException exception) {
        log.error("Persistence operation failed", exception);
        return ResponseEntity.status(HttpStatus.SERVICE_UNAVAILABLE)
                .body(ErrorResponse.from(ExceptionMessages.DATABASE_UNAVAILABLE.getMessage()));
    }

    @ExceptionHandler(Exception.class)
    public ResponseEntity<ErrorResponse> unexpectedFailure(Exception exception) {
        log.error("Unexpected request failure", exception);
        return ResponseEntity.internalServerError().body(ErrorResponse.from(ExceptionMessages.INTERNAL_ERROR.getMessage()));
    }
}
