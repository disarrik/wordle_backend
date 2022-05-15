package io.github.samkelsey.wordzle.exception;


import io.github.samkelsey.wordzle.dto.ApiErrorResponseDto;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

import java.util.Set;
import java.util.TreeSet;

import static org.springframework.http.HttpStatus.BAD_REQUEST;

/**
 * A {@link ControllerAdvice} annotated class that contains all game's exception handlers
 */
@ControllerAdvice
public class RequestHandler {

    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<ApiErrorResponseDto> handleValidationExceptions(MethodArgumentNotValidException ex) {
        Set<String> errors = new TreeSet<>();
        ex.getBindingResult().getAllErrors().forEach((error) -> errors.add(error.getDefaultMessage()));

        return ResponseEntity.badRequest().body(
                new ApiErrorResponseDto(
                        BAD_REQUEST,
                        "Bad request",
                        errors
                )
        );
    }
}
