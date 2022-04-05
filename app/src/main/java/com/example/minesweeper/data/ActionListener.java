package com.example.minesweeper.data;

import com.example.minesweeper.model.Cell;

public interface ActionListener {
    void mineAdded(Cell cell);

    void numOfNearbyMinesAdded(Cell cell, Integer digit);

    void openCell(Cell cell);

    void markCell(Cell cell);

    void unMarkCell(Cell cell);
}
