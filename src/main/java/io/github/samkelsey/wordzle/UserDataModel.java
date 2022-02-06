package io.github.samkelsey.wordzle;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import static io.github.samkelsey.wordzle.GameStatus.PLAYING;

@Getter
@Setter
@NoArgsConstructor
public class UserDataModel implements Serializable {

    private GameStatus gameStatus = PLAYING;
    private List<String> guesses = new ArrayList<>();
    private Integer lives = 5;

}
