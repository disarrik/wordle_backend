package io.github.samkelsey.wordzle.entity;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.persistence.Embeddable;
import java.io.Serializable;

@Getter
@AllArgsConstructor
@NoArgsConstructor
@Embeddable
public class UserDataPK implements Serializable {
    private String userID;
    private String chatId;
}
