package com.example.minesweeper.model;

import android.view.View;

import com.example.minesweeper.data.ActionListener;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.Random;

public class Board {
    private ActionListener actionListener;
    private final int rows;
    private final int columns;
    private final int mines;
    private boolean firstMove = true;

    public Board(int fieldLength, int fieldHeight, int numOfMines) {
        this.rows = fieldHeight;
        this.columns = fieldLength;
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
            for (int j = 0; j < columns; j++)
                arrayOfAllCells.get(i).add(j, new Cell(i, j, false, false, false, 0));
        }
    }

    public void fieldRandomFilling(Cell cell) {
        int counterMines = 0;
        List<Cell> possibleCellsForMines = new ArrayList<>();

        for (int i = 0; i < rows; i++)
            for (int j = 0; j < columns; j++)
                if (!(i == cell.getY() && j == cell.getX()))
                    possibleCellsForMines.add(new Cell(i, j, false, false, false, 0));
        //Filling with mines
        while (counterMines != mines) {
            Random rand = new Random();
            Cell cellWithMine = possibleCellsForMines.get(rand.nextInt(possibleCellsForMines.size()));
            arrayOfAllCells.get(cellWithMine.getY()).get(cellWithMine.getX()).setMined();
            possibleCellsForMines.remove(cellWithMine);
            counterMines++;
        }
        //Filling with digits
        possibleCellsForMines.add(cell);
        for (int i = 0; i < possibleCellsForMines.size(); i++) {
            Cell cellWithDigit = possibleCellsForMines.get(i);
            List<Cell> neighbours = getNeighbours(cellWithDigit);
            int counterNearbyMines = 0;
            for (int j = 0; j < neighbours.size(); j++)
                if (neighbours.get(j).isMined()) counterNearbyMines++;
            arrayOfAllCells.get(cellWithDigit.getY()).get(cellWithDigit.getX()).setNearbyMines(counterNearbyMines);
        }
    }

    public void fieldOpening(Cell cell) {
        if (!cell.isOpen()) {
            actionListener.openCell(cell);
            cell.setOpen();
            if (cell.getNearbyMines() != 0)
                actionListener.numOfNearbyMinesAdded(cell, cell.getNearbyMines());
            else {
                List<Cell> neighbours = getNeighbours(cell);
                for (int i = 0; i < neighbours.size(); i++) fieldOpening(neighbours.get(i));
            }
        }
    }

    public List<Cell> getNeighbours(Cell cell) {
        List<Coordinates> coordinates = initCoordinates();
        List<Cell> neighbours = new ArrayList<>();
        for (Coordinates coordinate : coordinates) {
            Cell neighbour = getCell(cell.getY() + coordinate.getY(), cell.getX() + coordinate.getX());
            if (neighbour != null) neighbours.add(neighbour);
        }
        return neighbours;
    }


    public Cell getCell(int posY, int posX) {
        if (cellExist(posY, posX)) return arrayOfAllCells.get(posY).get(posX);
        else return null;
    }

    private boolean cellExist(int posY, int posX) {
        return posY < rows && posY >= 0 && posX < columns && posX >= 0;
    }

    private List<Coordinates> initCoordinates() {
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

    public boolean winResult() {
        for (int i = 0; i < rows; i++)
            for (int j = 0; j < columns; j++)
                if (!arrayOfAllCells.get(i).get(j).isOpen() && !arrayOfAllCells.get(i).get(j).isMined())
                    return false;
        return true;
    }

    public void startGame(View view) {
        Cell cellForClick = getCell((int) ((View) view.getParent()).getTag(), (int) view.getTag());
        if (!Objects.requireNonNull(cellForClick).isMarked()) {
            if (firstMove) {
                fieldRandomFilling(cellForClick);
                fieldOpening(cellForClick);
                firstMove = !firstMove;
            } else {
                if (cellForClick.isMined()) {
                    actionListener.mineAdded(cellForClick);
                    actionListener.defeat();
                } else fieldOpening(cellForClick);
            }
            if (winResult()) actionListener.winResult();
        }
    }

    public boolean placeFlag(View view) {
        Cell cellForClick = getCell((int) ((View) view.getParent()).getTag(), (int) view.getTag());
        if (!Objects.requireNonNull(cellForClick).isOpen()) {
            cellForClick.setMarked();
            if (cellForClick.isMarked()) actionListener.markCell(cellForClick);
            else actionListener.unMarkCell(cellForClick);
        }
        return true;
    }
}
