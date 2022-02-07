package io.github.samkelsey.wordzle.config;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.test.context.TestConfiguration;
import redis.embedded.RedisServer;

import javax.annotation.PostConstruct;
import javax.annotation.PreDestroy;
import java.io.IOException;

@TestConfiguration
public class EmbeddedRedisTestConfiguration {

    private final RedisServer redisServer;

    public EmbeddedRedisTestConfiguration(@Value("${spring.redis.port}") final int redisPort)  {
        this.redisServer = new RedisServer(redisPort);
    }

    @PostConstruct
    public void startRedis() {
        this.redisServer.start();
    }

    @PreDestroy
    public void stopRedis() {
        this.redisServer.stop();
    }

}
