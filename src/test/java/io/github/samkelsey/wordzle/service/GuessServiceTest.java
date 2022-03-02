package io.github.samkelsey.wordzle.service;

import io.github.samkelsey.wordzle.model.GameStatus;
import io.github.samkelsey.wordzle.TestUtils;
import io.github.samkelsey.wordzle.model.Guess;
import io.github.samkelsey.wordzle.model.UserData;
import io.github.samkelsey.wordzle.dto.RequestDto;
import io.github.samkelsey.wordzle.dto.ResponseDto;
import io.github.samkelsey.wordzle.schedule.ResetTargetWordTask;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.EnumSource;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import javax.servlet.http.HttpSession;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import static io.github.samkelsey.wordzle.model.GameStatus.LOST;
import static io.github.samkelsey.wordzle.model.GameStatus.PLAYING;
import static io.github.samkelsey.wordzle.model.GameStatus.WON;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
public class GuessServiceTest {

    @Mock
    private ResetTargetWordTask resetTargetWordTask;

    @InjectMocks
    private GuessService guessService;

    private RequestDto dto;
    private UserData userData;

    @BeforeEach
    public void init() {
        dto = TestUtils.createSampleRequestDto();
        userData = TestUtils.createSampleUserData();
    }

    @Test
    void whenCorrectGuess_shouldGameOver() {
        when(resetTargetWordTask.getTargetWord()).thenReturn(dto.getGuess());

        ResponseDto responseDto = guessService.makeGuess(userData, dto);

        assertEquals(WON, responseDto.getGameStatus());
    }

    @Test
    void whenMakeGuess_shouldDeductLife() {
        when(resetTargetWordTask.getTargetWord()).thenReturn("foo");
        int initialLives = userData.getLives();

        int lives = guessService.makeGuess(userData, dto).getLives();

        assertEquals(initialLives, lives + 1);
    }

    @Test
    void whenMakeGuess_shouldEvaluateGuess() {
        RequestDto dto = new RequestDto("motfs");
        when(resetTargetWordTask.getTargetWord()).thenReturn("forks");

        List<Guess> guesses = guessService.makeGuess(userData, dto).getGuesses();

        Guess result = guesses.get(guesses.size() - 1);
        Guess expected = new Guess(
                "motfs",
                Arrays.asList(1, 4),
                Arrays.asList(0, 2),
                Arrays.asList(3)
        );
        assertEquals(expected.getGuess(), result.getGuess());
        assertEquals(expected.getExists(), result.getExists());
        assertEquals(expected.getCorrect(), result.getCorrect());
    }

    @Test
    void whenMakeGuess_shouldAddGuess() {
        when(resetTargetWordTask.getTargetWord()).thenReturn(dto.getGuess());
        List<Guess> initialGuesses = new ArrayList<>(userData.getGuesses());

        List<Guess> guesses = guessService.makeGuess(userData, dto).getGuesses();

        assertEquals(initialGuesses.size() + 1, guesses.size());
        assertEquals(dto.getGuess(), guesses.get(guesses.size() - 1).getGuess());
    }

    @Test
    void whenOutOfLives_shouldGameOver() {
        userData.setLives(0);
        when(resetTargetWordTask.getTargetWord()).thenReturn("foo");

        ResponseDto response = guessService.makeGuess(userData, dto);

        assertEquals(LOST, response.getGameStatus());
    }

    @ParameterizedTest
    @EnumSource(value = GameStatus.class, names = {"WON", "LOST"})
    void whenGameOver_shouldReturnOnlyUserData(GameStatus gameStatus) {
        userData.setGameStatus(gameStatus);

        ResponseDto response = guessService.makeGuess(userData, dto);

        assertNull(response.getGuessIsCorrect());
    }
}
