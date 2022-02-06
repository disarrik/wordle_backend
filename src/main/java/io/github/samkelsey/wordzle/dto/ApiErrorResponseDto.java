package io.github.samkelsey.wordzle.dto;


import lombok.AllArgsConstructor;
import lombok.Getter;
import org.springframework.http.HttpStatus;

import java.util.Set;

@AllArgsConstructor
public class ApiErrorResponseDto {

    @Getter
    private HttpStatus status;

    @Getter
    private String message;

    @Getter
    private Set<String> errors;
}
