package io.github.samkelsey.wordzle.service;

import io.github.samkelsey.wordzle.GameStatus;
import io.github.samkelsey.wordzle.TestUtils;
import io.github.samkelsey.wordzle.UserDataModel;
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
import java.util.List;

import static io.github.samkelsey.wordzle.GameStatus.LOST;
import static io.github.samkelsey.wordzle.GameStatus.PLAYING;
import static io.github.samkelsey.wordzle.GameStatus.WON;
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
        UserDataModel userDataMock = TestUtils.createSampleUserData();
        HttpSession sessionMock = mock(HttpSession.class);
        when(sessionMock.getAttribute(anyString())).thenReturn(userDataMock);
        int initialLives = userDataMock.getLives();

        int lives = guessService.makeGuess(sessionMock, dto).getLives();

        assertEquals(initialLives, lives + 1);
    }

    @Test
    void whenMakeGuess_shouldAddGuess() {
        RequestDto dto = TestUtils.createSampleRequestDto();
        HttpSession sessionMock = mock(HttpSession.class);
        UserDataModel userDataMock = TestUtils.createSampleUserData();
        when(sessionMock.getAttribute(anyString())).thenReturn(userDataMock);
        when(resetTargetWordTask.getTargetWord()).thenReturn(dto.getGuess());
        List<String> initialGuesses = new ArrayList<>(userDataMock.getGuesses());

        List<String> guesses = guessService.makeGuess(sessionMock, dto).getGuesses();

        assertEquals(initialGuesses.size() + 1, guesses.size());
        assertEquals(dto.getGuess(), guesses.get(guesses.size() - 1));
    }

    @Test
    void whenMakeGuess_shouldUpdateSession() {
        RequestDto dto = TestUtils.createSampleRequestDto();
        HttpSession sessionMock = mock(HttpSession.class);
        UserDataModel userDataMock = TestUtils.createSampleUserData();
        when(sessionMock.getAttribute(anyString())).thenReturn(userDataMock);

        guessService.makeGuess(sessionMock, dto);

        verify(sessionMock, times(1)).setAttribute(anyString(), any(UserDataModel.class));
    }

    @Test
    void whenOutOfLives_shouldGameOver() {
        RequestDto dto = TestUtils.createSampleRequestDto();
        UserDataModel userDataMock = TestUtils.createSampleUserData();
        userDataMock.setLives(0);
        HttpSession sessionMock = mock(HttpSession.class);
        when(sessionMock.getAttribute(anyString())).thenReturn(userDataMock);

        ResponseDto response = guessService.makeGuess(sessionMock, dto);

        assertEquals(LOST, response.getGameStatus());
    }

    @ParameterizedTest
    @EnumSource(value = GameStatus.class, names = {"WON", "LOST"})
    void whenGameOver_shouldReturnOnlyUserData(GameStatus gameStatus) {
        RequestDto dto = TestUtils.createSampleRequestDto();
        UserDataModel userDataMock = TestUtils.createSampleUserData();
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

        ResponseDto response = guessService.makeGuess(sessionMock, dto);

        assertNotNull(response.getGuesses());
        assertEquals(PLAYING, response.getGameStatus());
    }
}
