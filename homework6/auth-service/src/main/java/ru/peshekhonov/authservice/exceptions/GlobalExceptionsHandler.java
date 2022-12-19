package ru.peshekhonov.authservice.exceptions;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import ru.peshekhonov.api.exceptions.AppError;
import ru.peshekhonov.api.exceptions.ResourceNotFoundException;

@ControllerAdvice
public class GlobalExceptionsHandler {

    @ExceptionHandler
    public ResponseEntity<AppError> handleResourceNotFoundException(ResourceNotFoundException e) {
        return new ResponseEntity<>(
                AppError.builder()
                        .code("RESOURCE_NOT_FOUND")
                        .error(e.getMessage())
                        .build(),
                HttpStatus.NOT_FOUND);
    }
}
