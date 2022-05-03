package io.github.samkelsey.wordzle.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.telegram.telegrambots.meta.api.objects.games.GameHighScore;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class GameScoreDTO {
    private boolean ok;
    private GameHighScore[] result;
}
