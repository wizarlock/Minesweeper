package com.example.minesweeper.model;

import com.example.minesweeper.data.ActionListener;

import java.util.ArrayList;
import java.util.List;

public class Board {
    private ActionListener actionListener;
    private final int rows;
    private final int columns;
    private final int mines;

    public Board(int fieldLength, int fieldWidth, int numOfMines) {
        this.rows = fieldLength;
        this.columns = fieldWidth;
        this.mines = numOfMines;
    }

    public void setActionListener(ActionListener actionListener) {
        this.actionListener = actionListener;
    }

    public static List<List<Cell>> arrayOfAllCells;

    public void board() {
        arrayOfAllCells = new ArrayList<>(rows);

        for (int i = 0; i < rows; i++) {
            arrayOfAllCells.add(i, new ArrayList<>(columns));
            for (int j = 0; j < columns; j++) {
                arrayOfAllCells.get(i).add(j, new Cell(i, j, false, false, false, 0));
            }
        }
    }
}
