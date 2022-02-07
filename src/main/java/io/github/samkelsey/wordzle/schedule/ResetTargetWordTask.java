package io.github.samkelsey.wordzle.schedule;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestClientException;
import org.springframework.web.client.RestTemplate;

@Component
public class ResetTargetWordTask {

    Logger LOGGER = LoggerFactory.getLogger(ResetTargetWordTask.class);
    private String targetWord;

    @Value("${reset-word-task.url}")
    private String url;

    @Scheduled(fixedDelayString ="${reset-word-task.delay}")
    public void resetWord() {
        try {
            LOGGER.info("Resetting word");
            RestTemplate restTemplate = new RestTemplate();
            String result = restTemplate.getForObject(url, String.class);
            targetWord = result.substring(2, result.length() - 2);
            LOGGER.info("Target word reset to \"{}\"", targetWord);
            // TODO: Wipe all session data so users are back to the start.
        } catch (RestClientException ex) {
            targetWord = "owl";
            LOGGER.info("Couldn't reach word api. Setting target word to \"{}\"", targetWord);
        }
    }

    public String getTargetWord() {
        return targetWord;
    }
}
