package com.example.minesweeper.model;

import androidx.annotation.Nullable;

public class Cell {
    private final int x;
    private final int y;
    private boolean marked;
    private boolean mined;
    private boolean open;
    private int nearbyMines;

    public Cell(int y, int x, boolean marked, boolean open, boolean mined, int nearbyMines) {
        this.y = y;
        this.x = x;
        this.marked = marked;
        this.open = open;
        this.mined = mined;
        this.nearbyMines = nearbyMines;
    }

    public int getX() {
        return x;
    }

    public int getY() {
        return y;
    }

    public boolean isMarked() {
        return marked;
    }

    public void setMarked() {
        this.marked = !this.marked;
    }

    public boolean isOpen() {
        return open;
    }

    public void setOpen() {
        this.open = !this.open;
    }

    public boolean isMined() {
        return mined;
    }

    public void setMined() {
        this.mined = !this.mined;
    }

    public Integer getNearbyMines() {
        return nearbyMines;
    }

    public void setNearbyMines(int digit) {
        this.nearbyMines = digit;
    }

    @Override
    public boolean equals(@Nullable Object other) {
        if (!(other instanceof Cell)) return false;
        if (this.getX() != ((Cell) other).getX()) return false;
        if (this.getY() != ((Cell) other).getY()) return false;
        if (this.isMarked() != ((Cell) other).isMarked()) return false;
        if (this.isMined() != ((Cell) other).isMined()) return false;
        if (this.isOpen() != ((Cell) other).isOpen()) return false;
        return this.getNearbyMines().equals(((Cell) other).getNearbyMines());
    }

}
