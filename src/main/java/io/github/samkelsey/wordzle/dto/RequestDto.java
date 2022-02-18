package io.github.samkelsey.wordzle.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.validation.constraints.Max;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

@Getter
@AllArgsConstructor
@NoArgsConstructor
public class RequestDto {

    @NotNull(message = "guess is a mandatory field.")
    @Size(min = 5, max = 5)
    @JsonProperty("guess")
    private String guess;

}
