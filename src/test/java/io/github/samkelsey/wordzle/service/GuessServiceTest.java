package io.github.samkelsey.wordzle.service;

import io.github.samkelsey.wordzle.TestUtils;
import io.github.samkelsey.wordzle.UserDataModel;
import io.github.samkelsey.wordzle.dto.RequestDto;
import io.github.samkelsey.wordzle.dto.ResponseDto;
import io.github.samkelsey.wordzle.schedule.ResetTargetWordTask;
import org.apache.catalina.User;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import javax.servlet.http.HttpSession;

import java.util.ArrayList;
import java.util.List;

import static io.github.samkelsey.wordzle.GameStatus.WON;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.mock;
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
        // TODO
    }

    @Test
    void whenOutOfLives_shouldGameOver() {
        // TODO
    }

    @Test
    void whenGameOver_shouldReturnOnlyUserData() {
        // TODO
    }


}
