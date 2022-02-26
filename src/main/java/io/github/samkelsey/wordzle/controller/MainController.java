package io.github.samkelsey.wordzle.controller;

import io.github.samkelsey.wordzle.dto.RequestDto;
import io.github.samkelsey.wordzle.dto.ResponseDto;
import io.github.samkelsey.wordzle.model.UserData;
import io.github.samkelsey.wordzle.schedule.ResetTargetWordTask;
import io.github.samkelsey.wordzle.service.GuessService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
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
    private final String SESSION_ATTRIBUTE = "USER_DATA";

    public MainController(GuessService guessService, ResetTargetWordTask resetTargetWordTask) {
        this.guessService = guessService;
        this.resetTargetWordTask = resetTargetWordTask;
    }

    @GetMapping("/getStats")
    public ResponseEntity<ResponseDto> getStats(HttpServletRequest request) {
        UserData userData = getUserData(request);
        return ResponseEntity.ok().body(new ResponseDto(userData));
    }

    @PostMapping(value = "/submitGuess")
    public ResponseEntity<ResponseDto> submitGuess(@RequestBody @Valid RequestDto dto, HttpServletRequest request) {
        LOGGER.info("Processing request for a guess of: {}", dto.getGuess());
        UserData userData = getUserData(request);
        ResponseDto response = guessService.makeGuess(userData, dto);
        setUserData(request.getSession(), UserData.fromResponseDto(response));
        return ResponseEntity.ok(response);
    }

    private UserData getUserData(HttpServletRequest request) {
        HttpSession session = getSession(request);
        UserData userData = (UserData) session.getAttribute(SESSION_ATTRIBUTE);

        if (userData == null) {
            userData = new UserData();
        }

        return userData;
    }

    private void setUserData(HttpSession session, UserData userData) {
        session.setAttribute(SESSION_ATTRIBUTE, userData);
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
