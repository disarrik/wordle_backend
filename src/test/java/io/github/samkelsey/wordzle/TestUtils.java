package io.github.samkelsey.wordzle;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.flextrade.jfixture.JFixture;
import io.github.samkelsey.wordzle.entity.Guess;
import io.github.samkelsey.wordzle.entity.UserData;
import io.github.samkelsey.wordzle.dto.RequestDto;

import java.util.ArrayList;
import java.util.List;

import static io.github.samkelsey.wordzle.entity.GameStatus.PLAYING;

public class TestUtils {

    public static final JFixture jFixture = new JFixture();

    public static UserData createSampleUserData() {
        List<Guess> guesses = new ArrayList<>();
        guesses.add(jFixture.create(Guess.class));
        guesses.add(jFixture.create(Guess.class));
        guesses.add(jFixture.create(Guess.class));

        UserData userData = new UserData();
        userData.setGameStatus(PLAYING);
        userData.setLives(5);
        userData.setGuesses(guesses);

        return userData;
    }

    public static RequestDto createSampleRequestDto() {
        return new RequestDto("parrot");
    }

    public static String asJsonString(final Object obj) {
        try {
            ObjectMapper mapper = new ObjectMapper();
            return mapper.writeValueAsString(obj);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }
}
