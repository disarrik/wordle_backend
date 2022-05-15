package io.github.samkelsey.wordzle.schedule;

import io.github.samkelsey.wordzle.repository.UserDataRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Isolation;
import org.springframework.transaction.annotation.Transactional;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.time.Instant;
import java.util.Random;

/**
 * A class with scheduled method
 */
@Component
public class ResetTargetWordTask {

    private final Logger LOGGER = LoggerFactory.getLogger(ResetTargetWordTask.class);
    private String targetWord;
    private long targetWordCreationTime;
    private final UserDataRepository userDataRepository;
    @Value("${reset-word-task.fileLocation}")
    private String fileLocation;

    public ResetTargetWordTask(UserDataRepository userDataRepository) {
        this.userDataRepository = userDataRepository;
    }

    /**
     * Scheduled method that is invoked to change the word in tha game
     */
    @Scheduled(fixedDelayString ="${reset-word-task.delay}")
    @Transactional(isolation = Isolation.SERIALIZABLE)
    public void resetWord() {
        LOGGER.info("Resetting word");
        targetWord = selectWord();
        targetWordCreationTime = Instant.now().toEpochMilli();
        userDataRepository.deleteAll();
        LOGGER.info("Target word reset to \"{}\"", targetWord);
    }

    private String selectWord() {
        String word = "grand";
        try {
            InputStream resourceStream = getClass().getResourceAsStream(fileLocation);
            InputStreamReader isr = new InputStreamReader(resourceStream);
            BufferedReader reader = new BufferedReader(isr);

            Random rand = new Random();
            int randInt = rand.nextInt(1000);

            while (randInt > 0) {
                reader.readLine();
                randInt -= 1;
            }

            word = reader.readLine();
            return word;
        } catch (IOException e) {
            LOGGER.error("Could not read new word. Setting word to default value.");
        }

        return word;
    }

    public String getTargetWord() {
        return targetWord;
    }

    public long getTargetWordCreationTime() {
        return targetWordCreationTime;
    }
}
