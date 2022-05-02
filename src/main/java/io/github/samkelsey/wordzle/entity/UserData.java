package io.github.samkelsey.wordzle.entity;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.ElementCollection;
import javax.persistence.EmbeddedId;
import javax.persistence.Entity;
import javax.persistence.FetchType;
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
    @ElementCollection(fetch = FetchType.EAGER)
    private List<Guess> guesses = new ArrayList<>();
    private Integer lives = 5;
    private DiscoveredLetters discoveredLetters = new DiscoveredLetters();

    public UserData(UserDataPK userDataPK) {
        this.userDataPK = userDataPK;
    }
}
