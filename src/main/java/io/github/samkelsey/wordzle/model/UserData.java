package io.github.samkelsey.wordzle.model;

import io.github.samkelsey.wordzle.dto.ResponseDto;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import static io.github.samkelsey.wordzle.model.GameStatus.PLAYING;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class UserData implements Serializable {

    private GameStatus gameStatus = PLAYING;
    private List<Guess> guesses = new ArrayList<>();
    private Integer lives = 5;

    public static UserData fromResponseDto(ResponseDto responseDto) {
        return new UserData(
                responseDto.getGameStatus(),
                responseDto.getGuesses(),
                responseDto.getLives()
        );
    }

}
