package io.github.samkelsey.wordzle.service;

import io.github.samkelsey.wordzle.UserDataModel;
import io.github.samkelsey.wordzle.dto.RequestDto;
import io.github.samkelsey.wordzle.dto.ResponseDto;
import io.github.samkelsey.wordzle.schedule.ResetTargetWordTask;
import org.springframework.stereotype.Service;

import javax.servlet.http.HttpSession;

import static io.github.samkelsey.wordzle.GameStatus.LOST;
import static io.github.samkelsey.wordzle.GameStatus.WON;

@Service
public class GuessService {

    private final String SESSION_ATTRIBUTE = "USER_DATA";
    private final ResetTargetWordTask resetTargetWordTask;

    public GuessService(ResetTargetWordTask resetTargetWordTask) {
        this.resetTargetWordTask = resetTargetWordTask;
    }

    public ResponseDto makeGuess(HttpSession session, RequestDto dto) {
        UserDataModel userData = getUserData(session);

        if (isGameOver(userData)) {
            return new ResponseDto(userData);
        }

        userData.setLives(userData.getLives() - 1);
        userData.getGuesses().add(dto.getGuess());

        if (dto.getGuess().equals(resetTargetWordTask.getTargetWord())) {
            userData.setGameStatus(WON);
        } else if (userData.getLives() <= 0) {
            userData.setGameStatus(LOST);
        }

        session.setAttribute(SESSION_ATTRIBUTE, userData);
        return new ResponseDto(
                dto.getGuess().equals(resetTargetWordTask.getTargetWord()),
                userData
        );
    }

    private UserDataModel getUserData(HttpSession session) {
        UserDataModel userData = (UserDataModel) session.getAttribute(SESSION_ATTRIBUTE);

        if (userData == null) {
            userData = new UserDataModel();
        }

        return userData;
    }

    private boolean isGameOver(UserDataModel userData) {
        return userData.getGameStatus() == LOST || userData.getGameStatus() == WON;
    }
}
