package io.github.samkelsey.wordzle.controller;

import io.github.samkelsey.wordzle.dto.ResponseDto;
import io.github.samkelsey.wordzle.schedule.ResetTargetWordTask;
import io.github.samkelsey.wordzle.service.GuessService;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

import static io.github.samkelsey.wordzle.model.GameStatus.PLAYING;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.atMostOnce;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
public class MainControllerTest {

    @Mock
    private ResetTargetWordTask resetTargetWordTask;

    @Mock
    private GuessService guessService;

    @InjectMocks
    private MainController mainController;

    @Test
    void whenTargetWordResets_sessionShouldBeInvalidated() {
        HttpServletRequest requestMock = mock(HttpServletRequest.class);
        HttpSession sessionMock = mock(HttpSession.class);
        when(requestMock.getSession()).thenReturn(sessionMock);
        when(sessionMock.getCreationTime()).thenReturn(10L);
        when(resetTargetWordTask.getTargetWordCreationTime()).thenReturn(20L);

        mainController.getStats(requestMock);

        verify(sessionMock, times(1)).invalidate();
    }

    @Test
    void whenSessionIsNull_newUserDataCreated() {
        HttpServletRequest requestMock = mock(HttpServletRequest.class);
        HttpSession sessionMock = mock(HttpSession.class);
        when(requestMock.getSession()).thenReturn(sessionMock);
        when(sessionMock.getCreationTime()).thenReturn(20L);
        when(resetTargetWordTask.getTargetWordCreationTime()).thenReturn(10L);

        ResponseDto res = mainController.getStats(requestMock).getBody();

        assertNotNull(res);
        assertEquals(PLAYING, res.getGameStatus());
        assertEquals(5, res.getLives());
    }

    @Test
    void whenMakeGuess_shouldUpdateSession() {
        HttpServletRequest requestMock = mock(HttpServletRequest.class);
        HttpSession sessionMock = mock(HttpSession.class);
        when(requestMock.getSession()).thenReturn(sessionMock);
        when(sessionMock.getCreationTime()).thenReturn(20L);
        when(resetTargetWordTask.getTargetWordCreationTime()).thenReturn(10L);

        mainController.getStats(requestMock);

        verify(sessionMock, atMostOnce()).setAttribute(anyString(), any());
    }
}
