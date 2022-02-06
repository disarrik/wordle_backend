package io.github.samkelsey.wordzle.dto;

import lombok.Data;

import javax.validation.constraints.NotNull;

@Data
public class RequestDto {

    @NotNull(message = "guess is a mandatory field.")
    private String guess;

}
