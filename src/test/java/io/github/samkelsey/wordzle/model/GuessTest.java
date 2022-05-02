package io.github.samkelsey.wordzle.model;

import io.github.samkelsey.wordzle.entity.Guess;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class GuessTest {

    @Test
    void whenGetCorrectAsChars_shouldReturn() {
        Guess guess = new Guess(
                "grand",
                new ArrayList<>(Arrays.asList(0, 1)),
                new ArrayList<>(Arrays.asList(4)),
                new ArrayList<>(Arrays.asList(2, 3))
        );

        List<Character> result = guess.getCorrectAsChars();

        List<Character> expected = new ArrayList<>(Arrays.asList('g', 'r'));
        assertEquals(expected, result);
    }
}
