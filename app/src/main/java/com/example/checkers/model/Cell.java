package com.example.checkers.model;

import androidx.annotation.Nullable;

public class Cell {
    private final int x;
    private final int y;
    private Checker checker;

    public Cell(int y, int x, Checker checker) {
        this.y = y;
        this.x = x;
        this.checker = checker;
    }

    public int getX() {
        return x;
    }

    public int getY() {
        return y;
    }

    public Checker getChecker() {
        return checker;
    }

    public void setChecker(Checker checker) {
        this.checker = checker;
    }

    public boolean cellExist(int posY, int posX) {
        return posY <= 7 && posY >= 0 && posX <= 7 && posX >= 0;
    }

    @Override
    public boolean equals(@Nullable Object other) {
        if (!(other instanceof Cell)) return false;
        if (this.getX() != ((Cell) other).getX()) return false;
        if (this.getY() != ((Cell) other).getY()) return false;
        if (this.getChecker() == null && ((Cell) other).getChecker() == null) return true;
        if ((this.getChecker() != null && ((Cell) other).getChecker() == null) || (this.getChecker() == null && ((Cell) other).getChecker() != null))
            return false;
        if (this.getChecker().getCondition() != ((Cell) other).getChecker().getCondition())
            return false;
        return this.getChecker().getColor() == ((Cell) other).getChecker().getColor();
    }
}

