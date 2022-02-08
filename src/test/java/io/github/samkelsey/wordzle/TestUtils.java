package io.github.samkelsey.wordzle;

import com.fasterxml.jackson.databind.ObjectMapper;
import io.github.samkelsey.wordzle.UserDataModel;
import io.github.samkelsey.wordzle.dto.RequestDto;

import java.util.ArrayList;
import java.util.List;

import static io.github.samkelsey.wordzle.GameStatus.PLAYING;

public class TestUtils {

    public static UserDataModel createSampleUserData() {
        List<String> guesses = new ArrayList<>();
        guesses.add("cat");
        guesses.add("dog");
        guesses.add("snake");

        UserDataModel userData = new UserDataModel();
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
