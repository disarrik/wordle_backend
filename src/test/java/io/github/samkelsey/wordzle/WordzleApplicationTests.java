package io.github.samkelsey.wordzle;

import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import redis.embedded.RedisServer;

@SpringBootTest
class WordzleApplicationTests {

	private static final RedisServer redisServer = new RedisServer();

	@BeforeAll
	public static void before() {
		redisServer.start();
	}

	@Test
	void contextLoads() {
	}

	@AfterAll
	public static void after() {
		redisServer.stop();
	}

}
