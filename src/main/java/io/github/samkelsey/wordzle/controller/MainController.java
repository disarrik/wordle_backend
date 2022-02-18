package io.github.samkelsey.wordzle.controller;

import io.github.samkelsey.wordzle.dto.RequestDto;
import io.github.samkelsey.wordzle.dto.ResponseDto;
import io.github.samkelsey.wordzle.schedule.ResetTargetWordTask;
import io.github.samkelsey.wordzle.service.GuessService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import javax.validation.Valid;

@RestController
public class MainController {

    private final GuessService guessService;
    private final ResetTargetWordTask resetTargetWordTask;
    private final Logger LOGGER = LoggerFactory.getLogger(MainController.class);

    public MainController(GuessService guessService, ResetTargetWordTask resetTargetWordTask) {
        this.guessService = guessService;
        this.resetTargetWordTask = resetTargetWordTask;
    }

    @PostMapping(value = "/submitGuess")
    public ResponseEntity<ResponseDto> submitGuess(@RequestBody @Valid RequestDto dto, HttpServletRequest request) {
        LOGGER.info("Processing request for a guess of: {}", dto.getGuess());
        HttpSession session = getSession(request);
        ResponseDto response = guessService.makeGuess(session, dto);

        return ResponseEntity.ok(response);
    }

    private HttpSession getSession(HttpServletRequest request) {
        HttpSession session = request.getSession();

        if (session.getCreationTime() < resetTargetWordTask.getTargetWordCreationTime()) {
            session.invalidate();
            session = request.getSession();
        }

        return session;
    }
}
