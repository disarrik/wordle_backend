package io.github.samkelsey.wordzle.service;

import io.github.samkelsey.wordzle.model.Guess;
import io.github.samkelsey.wordzle.model.UserData;
import io.github.samkelsey.wordzle.dto.RequestDto;
import io.github.samkelsey.wordzle.dto.ResponseDto;
import io.github.samkelsey.wordzle.schedule.ResetTargetWordTask;
import org.springframework.stereotype.Service;

import javax.servlet.http.HttpSession;

import java.util.ArrayList;
import java.util.List;

import static io.github.samkelsey.wordzle.model.GameStatus.LOST;
import static io.github.samkelsey.wordzle.model.GameStatus.WON;

@Service
public class GuessService {

    private final ResetTargetWordTask resetTargetWordTask;

    public GuessService(ResetTargetWordTask resetTargetWordTask) {
        this.resetTargetWordTask = resetTargetWordTask;
    }

    public ResponseDto makeGuess(UserData userData, RequestDto dto) {

        if (isGameOver(userData)) {
            return new ResponseDto(userData);
        }

        Guess guessResult = evaluateGuess(dto.getGuess());

        userData.setLives(userData.getLives() - 1);
        userData.getGuesses().add(guessResult);
        updateDiscoveredLetters(userData, guessResult);

        if (dto.getGuess().equals(resetTargetWordTask.getTargetWord())) {
            userData.setGameStatus(WON);
        } else if (userData.getLives() <= 0) {
            userData.setGameStatus(LOST);
        }

        return new ResponseDto(
                dto.getGuess().equals(resetTargetWordTask.getTargetWord()),
                userData
        );
    }

    private void updateDiscoveredLetters(UserData userData, Guess guessResult) {
        appendUniqueLetters(
                userData.getDiscoveredLetters().getCorrect(),
                guessResult.getCorrectAsChars()
        );
        appendUniqueLetters(
                userData.getDiscoveredLetters().getExists(),
                guessResult.getExistsAsChars()
        );
        appendUniqueLetters(
                userData.getDiscoveredLetters().getIncorrect(),
                guessResult.getIncorrectAsChars()
        );
    }

    private void appendUniqueLetters(List<Character> initialLetters, List<Character> newLetters) {
        for (char c : newLetters) {
            if (!initialLetters.contains(c)) {
                initialLetters.add(c);
            }
        }
    }

    private boolean isGameOver(UserData userData) {
        return userData.getGameStatus() == LOST || userData.getGameStatus() == WON;
    }

    private Guess evaluateGuess(String s) {
        char[] target = resetTargetWordTask.getTargetWord().toCharArray();
        char[] guess = s.toCharArray();

        List<Integer> correct = new ArrayList<>();
        List<Integer> incorrect = new ArrayList<>();
        List<Integer> exists = new ArrayList<>();

        for (int i = 0; i < target.length; i++) {
            if (target[i] == guess[i]) {
                correct.add(i);
            } else {
                for (char c : target) {
                    if (guess[i] == c && !exists.contains(i)) {
                        exists.add(i);
                    }
                }
            }

            if (!correct.contains(i) && !exists.contains(i)) {
                incorrect.add(i);
            }
        }

        return new Guess(s, correct, incorrect, exists);
    }
}
