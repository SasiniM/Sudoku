package org.sudoku.constant;

import java.util.Arrays;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
@Getter
public enum Action {
    MOVE_NUM("move"), CLEAR_NUM("clear"), HINT("hint"), CHECK("check"), QUIT("quit");

    final String code;

    public static Action of(String code) {
        return Arrays
                .stream(Action.values())
                .filter(action -> action.getCode().equals(code))
                .findFirst()
                .orElseThrow(IllegalArgumentException::new);
    }

}
