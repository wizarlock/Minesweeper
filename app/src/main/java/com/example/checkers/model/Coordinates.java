package com.example.checkers.model;


public class Coordinates {
    private final int y;
    private final int x;

    public Coordinates(int y, int x) {
        this.y = y;
        this.x = x;
    }

    public int getX() {
        return x;
    }

    public int getY() {
        return y;
    }
}

