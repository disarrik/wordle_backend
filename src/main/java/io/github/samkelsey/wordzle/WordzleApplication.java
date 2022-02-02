package io.github.samkelsey.wordzle;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.scheduling.annotation.EnableScheduling;

@SpringBootApplication
@EnableScheduling
public class WordzleApplication {

	public static void main(String[] args) {
		SpringApplication.run(WordzleApplication.class, args);
	}

}
