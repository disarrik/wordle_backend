package io.github.samkelsey.wordzle;

import io.github.samkelsey.wordzle.dto.RequestDto;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import redis.embedded.RedisServer;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;

@SpringBootTest
@AutoConfigureMockMvc
public class MainControllerLayerTest {

    @Autowired
    private MockMvc mockMvc;

    private static final RedisServer redisServer = new RedisServer();

    @BeforeAll
    public static void before() {
        redisServer.start();
    }

    // 200 RESPONSES
    @Test
    void whenValidRequest_shouldReturn200() throws Exception {
        MvcResult response = mockMvc.perform(
                post("/submitGuess")
                        .content(TestUtils.asJsonString(new RequestDto("testGuess")))
                        .contentType(MediaType.APPLICATION_JSON)
        ).andReturn();

        assertEquals(200, response.getResponse().getStatus());
    }

    // 400 RESPONSES
    @Test
    void whenInvalidRequest_shouldReturn400() {

    }

    // 500 RESPONSES

    @AfterAll
    public static void after() {
        redisServer.stop();
    }
}
