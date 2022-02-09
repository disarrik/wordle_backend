package io.github.samkelsey.wordzle.schedule;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestClientException;
import org.springframework.web.client.RestTemplate;

import java.time.Instant;
import java.time.LocalDateTime;

@Component
public class ResetTargetWordTask {

    Logger LOGGER = LoggerFactory.getLogger(ResetTargetWordTask.class);
    private String targetWord = "owl";
    private long targetWordCreationTime;

    @Value("${reset-word-task.url}")
    private String url;

    @Scheduled(fixedDelayString ="${reset-word-task.delay}")
    public void resetWord() {
        try {
            LOGGER.info("Resetting word");
//            RestTemplate restTemplate = new RestTemplate();
//            String result = restTemplate.getForObject(url, String.class);
//            targetWord = result.substring(2, result.length() - 2);
            targetWordCreationTime = Instant.now().toEpochMilli();
            LOGGER.info("Target word reset to \"{}\"", targetWord);

        } catch (RestClientException ex) {
            LOGGER.info("Failed to reset target word. Couldn't reach word api, will try again soon.");
        }
    }

    public String getTargetWord() {
        return targetWord;
    }

    public long getTargetWordCreationTime() {
        return targetWordCreationTime;
    }
}
