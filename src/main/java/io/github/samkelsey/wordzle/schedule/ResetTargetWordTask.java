package io.github.samkelsey.wordzle.schedule;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestClientException;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.time.Instant;
import java.util.Random;

@Component
public class ResetTargetWordTask {

    Logger LOGGER = LoggerFactory.getLogger(ResetTargetWordTask.class);
    private String targetWord;
    private long targetWordCreationTime;

    @Value("${reset-word-task.fileLocation}")
    private String fileLocation;

    @Scheduled(fixedDelayString ="${reset-word-task.delay}")
    public void resetWord() {
        try {
            LOGGER.info("Resetting word");
            targetWord = selectWord();
            targetWordCreationTime = Instant.now().toEpochMilli();
            LOGGER.info("Target word reset to \"{}\"", targetWord);

        } catch (RestClientException ex) {
            LOGGER.info("Failed to reset target word. Couldn't reach word api, will try again soon.");
        }
    }

    private String selectWord() {
        String word = "grand";
        try {
            BufferedReader reader = new BufferedReader(new FileReader(fileLocation));
            Random rand = new Random();
            int randInt = rand.nextInt(1000);

            while (randInt > 0) {
                reader.readLine();
                randInt -= 1;
            }

            word = reader.readLine();
            return word;
        } catch (IOException e) {
            LOGGER.error(e.getMessage());
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
