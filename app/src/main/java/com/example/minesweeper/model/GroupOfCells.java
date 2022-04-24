package com.example.minesweeper.model;

import androidx.annotation.Nullable;

import java.util.List;

public class GroupOfCells {
    private final List<Cell> cells;
    private final Integer numOfMines;

    public GroupOfCells(List<Cell> cells, Integer numOfMines) {
        this.cells = cells;
        this.numOfMines = numOfMines;
    }

    public List<Cell> getGroup() {
        return cells;
    }

    public Integer getNumOfMines() {
        return numOfMines;
    }

    @Override
    public boolean equals(@Nullable Object other) {
        if (!(other instanceof GroupOfCells)) return false;
        if (this.getGroup().size() != ((GroupOfCells) other).getGroup().size()) return false;
        if (!this.getNumOfMines().equals(((GroupOfCells) other).getNumOfMines())) return false;
        return this.getGroup().containsAll(((GroupOfCells) other).getGroup());
    }
}
