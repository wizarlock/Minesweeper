package com.example.checkers.model;

public class CellsForEating {
    private final Cell requiredCell;
    private final Cell eaten;
    private final Cell moving;

    public CellsForEating(Cell cell, Cell eaten, Cell moving) {
        this.requiredCell = cell;
        this.eaten = eaten;
        this.moving = moving;
    }

    public Cell getRequiredCell() {
        return requiredCell;
    }

    public Cell getEaten() {
        return eaten;
    }

    public Cell getMoving() {
        return moving;
    }
}
