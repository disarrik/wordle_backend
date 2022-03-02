package io.github.samkelsey.wordzle.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

@Getter
@Setter
@AllArgsConstructor
public class Guess implements Serializable {

    private String guess;
    private List<Integer> correct;
    private List<Integer> incorrect;
    private List<Integer> exists;

    @JsonIgnore
    public List<Character> getCorrectAsChars() {
        return getIndexesAsChars(correct, guess);
    }

    @JsonIgnore
    public List<Character> getIncorrectAsChars() {
        return getIndexesAsChars(incorrect, guess);
    }

    @JsonIgnore
    public List<Character> getExistsAsChars() {
        return getIndexesAsChars(exists, guess);
    }

    private List<Character> getIndexesAsChars(List<Integer> indexes, String guess) {
        List<Character> result = new ArrayList<>();
        char[] guessArr = guess.toCharArray();

        indexes.forEach(num -> result.add(guessArr[num]));

        return result;
    }

}
