package io.github.samkelsey.wordzle.schedule;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

@Component
public class ResetTargetWordTask {

    Logger logger = LoggerFactory.getLogger(ResetTargetWordTask.class);

    @Scheduled(fixedDelay = 5 * 1000)
    public void resetWord() {
        logger.info("Resetting word");
    }
}
