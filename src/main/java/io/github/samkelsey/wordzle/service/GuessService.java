package io.github.samkelsey.wordzle.service;

import io.github.samkelsey.wordzle.dto.GameScoreDTO;
import io.github.samkelsey.wordzle.dto.RequestDto;
import io.github.samkelsey.wordzle.dto.ResponseDto;
import io.github.samkelsey.wordzle.entity.Guess;
import io.github.samkelsey.wordzle.entity.UserData;
import io.github.samkelsey.wordzle.entity.UserDataPK;
import io.github.samkelsey.wordzle.repository.UserDataRepository;
import io.github.samkelsey.wordzle.schedule.ResetTargetWordTask;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.util.UriComponentsBuilder;
import org.telegram.telegrambots.meta.api.objects.Message;
import org.telegram.telegrambots.meta.api.objects.games.GameHighScore;

import java.util.*;

import static io.github.samkelsey.wordzle.entity.GameStatus.LOST;
import static io.github.samkelsey.wordzle.entity.GameStatus.WON;

/**
 * A Service that contains all methods with business logic
 */
@Service
public class GuessService {

    private final ResetTargetWordTask resetTargetWordTask;
    private final UserDataRepository userDataRepository;
    private final String token;

    public GuessService(ResetTargetWordTask resetTargetWordTask, UserDataRepository userDataRepository, @Value("${bot.token}") String token) {
        this.resetTargetWordTask = resetTargetWordTask;
        this.userDataRepository = userDataRepository;
        this.token = token;
    }

    /**
     * Return {@link UserData} that contains current stats of the user
     */
    @Transactional
    public UserData getStats(String userId, String chatId) {
        Optional<UserData> optionalUserData = userDataRepository.findById(new UserDataPK(userId, chatId));
        if (optionalUserData.isEmpty()) {
            return userDataRepository.save(new UserData(new UserDataPK(userId, chatId)));
        }
        return optionalUserData.get();
    }

    /**
     * Make a guess and return the changed stats of the user
     * @param dto {@link RequestDto} the request from the user that is making the current guess
     */
    @Transactional
    public ResponseDto makeGuess(String userId, String chatId, RequestDto dto) {
        UserData userData;
        Optional<UserData> optionalUserData = userDataRepository.findById(new UserDataPK(userId, chatId));
        if (optionalUserData.isEmpty()) {
            userData = userDataRepository.save(new UserData(new UserDataPK(userId, chatId)));
        } else {
            userData = optionalUserData.get();
        }

        if (isGameOver(userData)) {
            return new ResponseDto(userData);
        }

        Guess guessResult = evaluateGuess(dto.getGuess());

        userData.setLives(userData.getLives() - 1);
        userData.getGuesses().add(guessResult);
        updateDiscoveredLetters(userData, guessResult);

        if (dto.getGuess().equals(resetTargetWordTask.getTargetWord())) {
            userData.setGameStatus(WON);
            RestTemplate restTemplate = new RestTemplate();
            String botUrlGetScore = "https://api.telegram.org/bot" + token + "/getGameHighScores";
            String botUrlSetScore = "https://api.telegram.org/bot" + token + "/setGameScore";
            System.out.println(userId);
            System.out.println(chatId);


            UriComponentsBuilder uriGetScore = UriComponentsBuilder.fromHttpUrl(botUrlGetScore)
                    .queryParam("user_id", userId)
                    .queryParam("inline_message_id", chatId);
            ResponseEntity<GameScoreDTO> gameHighScores = restTemplate.getForEntity(uriGetScore.toUriString(), GameScoreDTO.class);

            for(GameHighScore gameHighScore:gameHighScores.getBody().getResult()){
                if(Long.parseLong(userId) == gameHighScore.getUser().getId()){
                    int score=gameHighScore.getScore() + userData.getLives();
                    UriComponentsBuilder uriSetScore = UriComponentsBuilder.fromHttpUrl(botUrlSetScore)
                            .queryParam("user_id", userId)
                            .queryParam("inline_message_id", chatId)
                            .queryParam("score", score);
                    restTemplate.getForEntity(uriSetScore.toUriString(), String.class);
                }
            }


        } else if (userData.getLives() <= 0) {
            userData.setGameStatus(LOST);
        }

        return new ResponseDto(
                dto.getGuess().equals(resetTargetWordTask.getTargetWord()),
                userData
        );
    }

    /**
     * A private method that changes {@link UserData} according to the current {@link Guess}
     */
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

    /**
     * Private method that forms the {@link Guess} according to the sent word
     * @param s sent word
     * @return formed {@link Guess} according to the sent word
     */
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
