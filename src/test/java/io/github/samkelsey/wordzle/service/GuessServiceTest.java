package io.github.samkelsey.wordzle.service;

import io.github.samkelsey.wordzle.model.GameStatus;
import io.github.samkelsey.wordzle.TestUtils;
import io.github.samkelsey.wordzle.model.Guess;
import io.github.samkelsey.wordzle.model.UserData;
import io.github.samkelsey.wordzle.dto.RequestDto;
import io.github.samkelsey.wordzle.dto.ResponseDto;
import io.github.samkelsey.wordzle.schedule.ResetTargetWordTask;
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

    @Test
    void whenCorrectGuess_shouldGameOver() {
        RequestDto dto = TestUtils.createSampleRequestDto();
        HttpSession sessionMock = mock(HttpSession.class);
        when(sessionMock.getAttribute(anyString())).thenReturn(TestUtils.createSampleUserData());
        when(resetTargetWordTask.getTargetWord()).thenReturn(dto.getGuess());

        ResponseDto responseDto = guessService.makeGuess(sessionMock, dto);

        assertEquals(WON, responseDto.getGameStatus());
    }

    @Test
    void whenMakeGuess_shouldDeductLife() {
        RequestDto dto = TestUtils.createSampleRequestDto();
        UserData userDataMock = TestUtils.createSampleUserData();
        HttpSession sessionMock = mock(HttpSession.class);
        when(sessionMock.getAttribute(anyString())).thenReturn(userDataMock);
        when(resetTargetWordTask.getTargetWord()).thenReturn("foo");
        int initialLives = userDataMock.getLives();

        int lives = guessService.makeGuess(sessionMock, dto).getLives();

        assertEquals(initialLives, lives + 1);
    }

    @Test
    void whenMakeGuess_shouldEvaluateGuess() {
        RequestDto dto = new RequestDto("motfs");
        HttpSession sessionMock = mock(HttpSession.class);
        when(resetTargetWordTask.getTargetWord()).thenReturn("forks");

        Guess result = guessService.makeGuess(sessionMock, dto).getGuesses().get(0);

        Guess expected = new Guess(
                "motfs",
                Arrays.asList(1, 4),
                Arrays.asList(3)
        );
        assertEquals(expected.getGuess(), result.getGuess());
        assertEquals(expected.getExists(), result.getExists());
        assertEquals(expected.getCorrect(), result.getCorrect());
    }

    @Test
    void whenMakeGuess_shouldAddGuess() {
        RequestDto dto = TestUtils.createSampleRequestDto();
        HttpSession sessionMock = mock(HttpSession.class);
        UserData userDataMock = TestUtils.createSampleUserData();
        when(sessionMock.getAttribute(anyString())).thenReturn(userDataMock);
        when(resetTargetWordTask.getTargetWord()).thenReturn(dto.getGuess());
        List<Guess> initialGuesses = new ArrayList<>(userDataMock.getGuesses());

        List<Guess> guesses = guessService.makeGuess(sessionMock, dto).getGuesses();

        assertEquals(initialGuesses.size() + 1, guesses.size());
        assertEquals(dto.getGuess(), guesses.get(guesses.size() - 1).getGuess());
    }

    @Test
    void whenMakeGuess_shouldUpdateSession() {
        RequestDto dto = TestUtils.createSampleRequestDto();
        HttpSession sessionMock = mock(HttpSession.class);
        UserData userDataMock = TestUtils.createSampleUserData();
        when(sessionMock.getAttribute(anyString())).thenReturn(userDataMock);
        when(resetTargetWordTask.getTargetWord()).thenReturn("foo");

        guessService.makeGuess(sessionMock, dto);

        verify(sessionMock, times(1)).setAttribute(anyString(), any(UserData.class));
    }

    @Test
    void whenOutOfLives_shouldGameOver() {
        RequestDto dto = TestUtils.createSampleRequestDto();
        UserData userDataMock = TestUtils.createSampleUserData();
        userDataMock.setLives(0);
        HttpSession sessionMock = mock(HttpSession.class);
        when(sessionMock.getAttribute(anyString())).thenReturn(userDataMock);
        when(resetTargetWordTask.getTargetWord()).thenReturn("foo");

        ResponseDto response = guessService.makeGuess(sessionMock, dto);

        assertEquals(LOST, response.getGameStatus());
    }

    @ParameterizedTest
    @EnumSource(value = GameStatus.class, names = {"WON", "LOST"})
    void whenGameOver_shouldReturnOnlyUserData(GameStatus gameStatus) {
        RequestDto dto = TestUtils.createSampleRequestDto();
        UserData userDataMock = TestUtils.createSampleUserData();
        userDataMock.setGameStatus(gameStatus);
        HttpSession sessionMock = mock(HttpSession.class);
        when(sessionMock.getAttribute(anyString())).thenReturn(userDataMock);

        ResponseDto response = guessService.makeGuess(sessionMock, dto);

        assertNull(response.getGuessIsCorrect());
    }

    @Test
    void whenSessionIsNull_newSessionCreated() {
        RequestDto dto = TestUtils.createSampleRequestDto();
        HttpSession sessionMock = mock(HttpSession.class);
        when(sessionMock.getAttribute(anyString())).thenReturn(null);
        when(resetTargetWordTask.getTargetWord()).thenReturn("foo");

        ResponseDto response = guessService.makeGuess(sessionMock, dto);

        assertNotNull(response.getGuesses());
        assertEquals(PLAYING, response.getGameStatus());
    }
}
