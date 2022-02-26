package io.github.samkelsey.wordzle.controller;

import io.github.samkelsey.wordzle.TestUtils;
import io.github.samkelsey.wordzle.dto.RequestDto;
import io.github.samkelsey.wordzle.dto.ResponseDto;
import io.github.samkelsey.wordzle.schedule.ResetTargetWordTask;
import io.github.samkelsey.wordzle.service.GuessService;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;

@ExtendWith(SpringExtension.class)
@WebMvcTest(MainController.class)
public class MainControllerLayerTest {

    @MockBean
    private ResetTargetWordTask resetTargetWordTask;

    @MockBean
    private GuessService guessService;

    @Autowired
    private MockMvc mockMvc;

    // 200 RESPONSES
    @Test
    void whenValidRequest_shouldReturn200() throws Exception {
        when(guessService.makeGuess(any(), any())).thenReturn(TestUtils.jFixture.create(ResponseDto.class));
        MvcResult response = mockMvc.perform(
                post("/submitGuess")
                        .content(TestUtils.asJsonString(new RequestDto("testG")))
                        .contentType(MediaType.APPLICATION_JSON)
        ).andReturn();

        assertEquals(200, response.getResponse().getStatus());
    }

    // 400 RESPONSES
    @Test
    void whenInvalidRequest_shouldReturn400() throws Exception {
        MvcResult response = mockMvc.perform(
                post("/submitGuess")
                        .content("{\"obj\":\"fail\"}")
                        .contentType(MediaType.APPLICATION_JSON)
        ).andReturn();

        assertEquals(400, response.getResponse().getStatus());
    }
}
