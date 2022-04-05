package com.example.minesweeper.model;

public class Cell {
    private final int x;
    private final int y;
    private boolean marked;
    private boolean mined;
    private boolean open;
    private int nearbyMines;

    public Cell(int x, int y, boolean marked, boolean open, boolean mined, int nearbyMines) {
        this.x = x;
        this.y = y;
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

}
