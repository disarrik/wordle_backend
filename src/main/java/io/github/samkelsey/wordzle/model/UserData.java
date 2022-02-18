package io.github.samkelsey.wordzle.model;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import static io.github.samkelsey.wordzle.model.GameStatus.PLAYING;

@Getter
@Setter
@NoArgsConstructor
public class UserData implements Serializable {

    private GameStatus gameStatus = PLAYING;
    private List<Guess> guesses = new ArrayList<>();
    private Integer lives = 5;

}
