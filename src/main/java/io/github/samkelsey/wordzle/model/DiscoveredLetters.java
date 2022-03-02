package io.github.samkelsey.wordzle.model;

import lombok.Getter;
import lombok.NoArgsConstructor;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

@Getter
@NoArgsConstructor
public class DiscoveredLetters implements Serializable {

    private final List<Character> incorrect = new ArrayList<>();
    private final List<Character> correct = new ArrayList<>();
    private final List<Character> exists = new ArrayList<>();

}
