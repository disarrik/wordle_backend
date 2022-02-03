package io.github.samkelsey.wordzle;

import io.github.samkelsey.wordzle.schedule.ResetTargetWordTask;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

@RestController
public class MainController {

    private final String SESSION_ATTRIBUTE = "USER_DATA";
    private final ResetTargetWordTask resetTargetWordTask;

    public MainController(ResetTargetWordTask resetTargetWordTask) {
        this.resetTargetWordTask = resetTargetWordTask;
    }

    @GetMapping("/getSessionData")
    public ResponseEntity<SessionModel> getSessionData(HttpSession session) {
        return ResponseEntity.ok().body((SessionModel) session.getAttribute(SESSION_ATTRIBUTE));
    }

    @PostMapping("/submitGuess")
    public ResponseEntity<SessionModel> submitGuess(@RequestBody RequestDto dto, HttpServletRequest request) {
        SessionModel session = (SessionModel) request.getSession().getAttribute(SESSION_ATTRIBUTE);

        if (session == null) {
            session = new SessionModel();
        }

        String guess = dto.getGuess();
        session.getGuesses().add(guess);

        if (guess.equals(resetTargetWordTask.getTargetWord())) {
            session.setHasAnsweredCorrectly(true);
        }

        request.getSession().setAttribute(SESSION_ATTRIBUTE, session);

        return ResponseEntity.ok().body(session);
    }
}
