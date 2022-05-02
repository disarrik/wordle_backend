package io.github.samkelsey.wordzle.entity;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.ElementCollection;
import javax.persistence.EmbeddedId;
import javax.persistence.Entity;
import javax.persistence.Lob;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import static io.github.samkelsey.wordzle.entity.GameStatus.PLAYING;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Entity
public class UserData implements Serializable {

    @EmbeddedId
    private UserDataPK userDataPK;
    private GameStatus gameStatus = PLAYING;
    @ElementCollection
    private List<Guess> guesses = new ArrayList<>();
    private Integer lives = 5;
    private DiscoveredLetters discoveredLetters = new DiscoveredLetters();

/*    public static UserData fromResponseDto(ResponseDto responseDto) {
        return new UserData(
                responseDto.getGameStatus(),
                responseDto.getGuesses(),
                responseDto.getLives(),
                responseDto.getDiscoveredLetters()
        );
    }*/

}
