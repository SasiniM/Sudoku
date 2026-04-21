package org.sudoku.model;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import org.sudoku.constant.Action;

@Getter
@Setter
@ToString
@Builder
public class Command {
    private String rawCommand;
    private Action action;
    private String cellRef;
    private int row;
    private int column;
    private int value;
}
