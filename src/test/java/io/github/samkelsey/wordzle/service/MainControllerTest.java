package io.github.samkelsey.wordzle.service;

import io.github.samkelsey.wordzle.MainController;
import io.github.samkelsey.wordzle.TestUtils;
import io.github.samkelsey.wordzle.dto.RequestDto;
import io.github.samkelsey.wordzle.schedule.ResetTargetWordTask;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

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
        RequestDto dto = TestUtils.createSampleRequestDto();

        mainController.submitGuess(dto, requestMock);

        verify(sessionMock, times(1)).invalidate();
    }
}
