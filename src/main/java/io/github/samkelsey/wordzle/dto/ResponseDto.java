package io.github.samkelsey.wordzle.dto;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonInclude.Include;
import com.fasterxml.jackson.annotation.JsonProperty;
import io.github.samkelsey.wordzle.GameStatus;
import io.github.samkelsey.wordzle.UserDataModel;
import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
@JsonInclude(Include.NON_NULL)
public class ResponseDto {

    @JsonProperty("game-status")
    private final GameStatus gameStatus;

    @JsonProperty("guess-is-correct")
    private Boolean guessIsCorrect;

    private List<String> guesses;

    private int lives;

    public ResponseDto(UserDataModel userData) {
        this.gameStatus = userData.getGameStatus();
        this.guessIsCorrect = null;
        this.guesses = userData.getGuesses();
        this.lives = userData.getLives();
    }

    public ResponseDto(boolean guessIsCorrect, UserDataModel userData) {
        this.gameStatus = userData.getGameStatus();
        this.guessIsCorrect = guessIsCorrect;
        this.guesses = userData.getGuesses();
        this.lives = userData.getLives();
    }
}
