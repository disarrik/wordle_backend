package io.github.samkelsey.wordzle.controller;

import io.github.samkelsey.wordzle.dto.RequestDto;
import io.github.samkelsey.wordzle.dto.ResponseDto;
import io.github.samkelsey.wordzle.entity.UserData;
import io.github.samkelsey.wordzle.entity.UserDataPK;
import io.github.samkelsey.wordzle.repository.UserDataRepository;
import io.github.samkelsey.wordzle.schedule.ResetTargetWordTask;
import io.github.samkelsey.wordzle.service.GuessService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;

@RestController
public class MainController {

    private final GuessService guessService;

    private final ResetTargetWordTask resetTargetWordTask;
    private final Logger LOGGER = LoggerFactory.getLogger(MainController.class);

    public MainController(GuessService guessService, ResetTargetWordTask resetTargetWordTask) {
        this.guessService = guessService;
        this.resetTargetWordTask = resetTargetWordTask;
    }


    @GetMapping("/getStats")
    public ResponseEntity<ResponseDto> getStats(@RequestParam("user_id") String userId,
                                                @RequestParam("chat_id") String chatId) {
        UserData userData = guessService.getStats(userId, chatId);
        return ResponseEntity.ok().body(new ResponseDto(userData));
    }

    @PostMapping(value = "/submitGuess")
    public ResponseEntity<ResponseDto> submitGuess(@RequestParam("user_id") String userId,
                                                   @RequestParam("chat_id") String chatId,
                                                   @RequestBody @Valid RequestDto dto) {
        LOGGER.info("Processing request for a guess of: {}", dto.getGuess());
        ResponseDto response = guessService.makeGuess(userId, chatId, dto);
        return ResponseEntity.ok(response);
    }
}
