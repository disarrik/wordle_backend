package io.github.samkelsey.wordzle.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotNull;

@Getter
@AllArgsConstructor
@NoArgsConstructor
public class RequestDto {

    @NotNull(message = "guess is a mandatory field.")
    @JsonProperty("guess")
    private String guess;

}
