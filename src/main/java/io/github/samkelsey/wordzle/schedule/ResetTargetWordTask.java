package io.github.samkelsey.wordzle.schedule;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;

@Component
public class ResetTargetWordTask {

    Logger logger = LoggerFactory.getLogger(ResetTargetWordTask.class);
    private String targetWord;

    @Scheduled(fixedDelay = 5 * 1000)
    public void resetWord() {
        logger.info("Resetting word");

        RestTemplate restTemplate = new RestTemplate();
        final String uri = "https://random-word-api.herokuapp.com/word";
        String result = restTemplate.getForObject(uri, String.class);
//        targetWord = result.substring(2, result.length() - 2);
        targetWord = "owl";
        logger.info("Target word reset to \"{}\"", targetWord);
    }

    public String getTargetWord() {
        return targetWord;
    }
}
