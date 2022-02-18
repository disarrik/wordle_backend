package io.github.samkelsey.wordzle.model;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.io.Serializable;
import java.util.List;

@Getter
@Setter
@AllArgsConstructor
public class Guess implements Serializable {

    private String guess;
    private List<Integer> correct;
    private List<Integer> exists;

}
