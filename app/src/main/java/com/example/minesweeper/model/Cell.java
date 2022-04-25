package com.example.minesweeper.model;

import static com.example.minesweeper.model.Board.arrayOfAllCells;

import androidx.annotation.Nullable;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

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

    private static List<Coordinates> initCoordinates() {
        List<Coordinates> coordinates = new ArrayList<>();
        coordinates.add(new Coordinates(-1, -1));
        coordinates.add(new Coordinates(-1, 1));
        coordinates.add(new Coordinates(1, -1));
        coordinates.add(new Coordinates(1, 1));
        coordinates.add(new Coordinates(1, 0));
        coordinates.add(new Coordinates(-1, 0));
        coordinates.add(new Coordinates(0, 1));
        coordinates.add(new Coordinates(0, -1));
        return coordinates;
    }

    public static List<Cell> getNeighbours(Cell cell, int fieldLength, int fieldHeight) {
        List<Coordinates> coordinates = initCoordinates();
        List<Cell> neighbours = new ArrayList<>();
        for (Coordinates coordinate : coordinates) {
            Cell neighbour = getCell(cell.getY() + coordinate.getY(), cell.getX() + coordinate.getX(),
                    fieldLength, fieldHeight);
            if (neighbour != null) neighbours.add(neighbour);
        }
        return neighbours;
    }

    public static Cell getCell(int posY, int posX, int fieldLength, int fieldHeight) {
        if (cellExist(posY, posX, fieldLength, fieldHeight))
            return arrayOfAllCells.get(posY).get(posX);
        else return null;
    }

    private static boolean cellExist(int posY, int posX, int fieldLength, int fieldHeight) {
        return posY < fieldHeight && posY >= 0 && posX < fieldLength && posX >= 0;
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

    @Override
    public int hashCode() {
        return Objects.hash(x, y, marked, mined, open, nearbyMines);
    }
}
