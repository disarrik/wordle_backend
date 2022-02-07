package io.github.samkelsey.wordzle;

import io.github.samkelsey.wordzle.config.EmbeddedRedisTestConfiguration;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.annotation.Import;

@SpringBootTest
@Import(EmbeddedRedisTestConfiguration.class)
class WordzleApplicationTests {

	@Test
	void contextLoads() {
	}

}
