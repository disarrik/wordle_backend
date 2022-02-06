package io.github.samkelsey.wordzle.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;

import javax.validation.constraints.NotNull;

@Getter
@AllArgsConstructor
public class RequestDto {

    @NotNull(message = "guess is a mandatory field.")
    private String guess;

}
