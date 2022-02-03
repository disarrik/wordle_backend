package io.github.samkelsey.wordzle;

import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

@Data
@NoArgsConstructor
public class SessionModel implements Serializable {

    private List<String> guesses = new ArrayList<>();
    private boolean hasAnsweredCorrectly = false;

}
