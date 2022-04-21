package com.example.minesweeper;

import static com.example.minesweeper.model.Cell.getCell;
import static com.example.minesweeper.model.Cell.getNeighbours;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotEquals;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;

import com.example.minesweeper.data.ActionListener;
import com.example.minesweeper.model.Board;
import com.example.minesweeper.model.Cell;

import org.junit.Test;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class BoardTest implements ActionListener {
    private final int fieldLengthForBoard8 = 8;
    private final int fieldHeightForBoard8 = 8;
    private final int fieldLengthForBoard10 = 10;
    private final int fieldHeightForBoard10 = 10;
    Board board8 = new Board(fieldLengthForBoard8, fieldHeightForBoard8, 10);
    Board board10 = new Board(fieldLengthForBoard10, fieldHeightForBoard10, 99);

    private List<List<Cell>> newCells(Integer length, Integer height) {
        List<List<Cell>> newCells = new ArrayList<>(height);
        for (int i = 0; i < height; i++) {
            newCells.add(i, new ArrayList<>(length));
            for (int j = 0; j < length; j++)
                newCells.get(i).add(j, new Cell(i, j, false, false, false, 0));
        }
        return newCells;
    }

    private boolean assertListEquals(List<Cell> list1, List<Cell> list2) {
        if (list1.size() != list2.size()) return false;
        return list1.containsAll(list2);
    }

    private boolean assertContentEquals(List<List<Cell>> list1, List<List<Cell>> list2) {
        if (list1.size() != list2.size()) return false;
        for (int i = 0; i < list1.size(); i++)
            for (int j = 0; j < list2.size(); j++)
                if (!list1.get(i).get(j).equals(list2.get(i).get(j))) return false;
        return true;
    }


    @Test
    public void board() {
        List<List<Cell>> cells8 = newCells(8, 8);
        List<List<Cell>> cells10 = newCells(10, 10);
        board8.board();
        assertTrue(assertContentEquals(cells8, Board.arrayOfAllCells));
        assertFalse(assertContentEquals(cells10, Board.arrayOfAllCells));
        board10.board();
        assertTrue(assertContentEquals(cells10, Board.arrayOfAllCells));
        assertFalse(assertContentEquals(cells8, Board.arrayOfAllCells));
    }

    @Test
    public void getterCell() {
        List<List<Cell>> cells8 = newCells(8, 8);
        board8.board();
        assertEquals(getCell(5, 5, fieldLengthForBoard8, fieldHeightForBoard8), cells8.get(5).get(5));
        assertNotEquals(getCell(5, 5, fieldLengthForBoard8, fieldHeightForBoard8), cells8.get(1).get(1));
        assertEquals(getCell(0, 0, fieldLengthForBoard8, fieldHeightForBoard8), cells8.get(0).get(0));
        assertNotEquals(getCell(5, 5, fieldLengthForBoard8, fieldHeightForBoard8), null);
        assertNull(getCell(-1, 4, fieldLengthForBoard8, fieldHeightForBoard8));
        assertNull(getCell(4, -1, fieldLengthForBoard8, fieldHeightForBoard8));
        assertNull(getCell(-1, -1, fieldLengthForBoard8, fieldHeightForBoard8));
        assertNull(getCell(8, 8, fieldLengthForBoard8, fieldHeightForBoard8));
        assertNull(getCell(20, 5, fieldLengthForBoard8, fieldHeightForBoard8));
        assertNull(getCell(5, 20, fieldLengthForBoard8, fieldHeightForBoard8));

        List<List<Cell>> cells10 = newCells(10, 10);
        board10.board();
        assertEquals(getCell(7, 3, fieldLengthForBoard10, fieldHeightForBoard10), cells10.get(7).get(3));
        assertNotEquals(getCell(7, 3, fieldLengthForBoard10, fieldHeightForBoard10), cells10.get(5).get(4));
        assertEquals(getCell(9, 9, fieldLengthForBoard10, fieldHeightForBoard10), cells10.get(9).get(9));
        assertNotEquals(getCell(6, 1, fieldLengthForBoard10, fieldHeightForBoard10), null);
        assertNull(getCell(-1, 5, fieldLengthForBoard10, fieldHeightForBoard10));
        assertNull(getCell(5, -1, fieldLengthForBoard10, fieldHeightForBoard10));
        assertNull(getCell(-1, -1, fieldLengthForBoard10, fieldHeightForBoard10));
        assertNull(getCell(10, 10, fieldLengthForBoard10, fieldHeightForBoard10));
        assertNull(getCell(20, 5, fieldLengthForBoard10, fieldHeightForBoard10));
        assertNull(getCell(5, 20, fieldLengthForBoard10, fieldHeightForBoard10));
    }

    @Test
    public void getterNeighbours() {
        List<List<Cell>> cells8 = newCells(8, 8);
        board8.board();
        List<Cell> neighbours = new ArrayList<>();
        neighbours.add(cells8.get(0).get(1));
        neighbours.add(cells8.get(1).get(0));
        neighbours.add(cells8.get(1).get(2));
        neighbours.add(cells8.get(2).get(1));
        assertFalse(assertListEquals(neighbours, getNeighbours(Board.arrayOfAllCells.get(1).get(1), fieldLengthForBoard8, fieldHeightForBoard8)));

        neighbours.add(cells8.get(0).get(0));
        neighbours.add(cells8.get(0).get(2));
        neighbours.add(cells8.get(2).get(0));
        neighbours.add(cells8.get(2).get(2));
        assertTrue(assertListEquals(neighbours, getNeighbours(Board.arrayOfAllCells.get(1).get(1), fieldLengthForBoard8, fieldHeightForBoard8)));

        neighbours.clear();
        neighbours.add(cells8.get(0).get(1));
        neighbours.add(cells8.get(1).get(0));
        assertFalse(assertListEquals(neighbours, getNeighbours(Board.arrayOfAllCells.get(0).get(0), fieldLengthForBoard8, fieldHeightForBoard8)));

        neighbours.add(cells8.get(1).get(1));
        assertTrue(assertListEquals(neighbours, getNeighbours(Board.arrayOfAllCells.get(0).get(0), fieldLengthForBoard8, fieldHeightForBoard8)));
    }

    @Test
    public void fieldRandomFilling() {
        for (int i = 0; i <= 299; i++) {
            int minesCounter = 0;
            Random rand = new Random();
            board8.board();
            board8.fieldRandomFilling(Board.arrayOfAllCells.get(rand.nextInt(8)).get(rand.nextInt(8)));
            for (int j = 0; j < 8; j++)
                for (int k = 0; k < 8; k++)
                    if (Board.arrayOfAllCells.get(j).get(k).isMined()) minesCounter++;
                    else {
                        long nearbyMinesCounter = 0;
                        List<Cell> neighbours = getNeighbours(Board.arrayOfAllCells.get(j).get(k), fieldLengthForBoard8, fieldHeightForBoard8);
                        for (int l = 0; l < neighbours.size(); l++)
                            if (neighbours.get(l).isMined()) nearbyMinesCounter++;
                        assertEquals(Integer.toUnsignedLong(Board.arrayOfAllCells.get(j).get(k).getNearbyMines()), nearbyMinesCounter);
                    }
            assertEquals(10, minesCounter);

            minesCounter = 0;
            board10.board();
            board10.fieldRandomFilling(Board.arrayOfAllCells.get(rand.nextInt(10)).get(rand.nextInt(10)));
            for (int j = 0; j < 10; j++)
                for (int k = 0; k < 10; k++)
                    if (Board.arrayOfAllCells.get(j).get(k).isMined()) minesCounter++;
                    else {
                        long nearbyMinesCounter = 0;
                        List<Cell> neighbours = getNeighbours(Board.arrayOfAllCells.get(j).get(k), fieldLengthForBoard10, fieldHeightForBoard10);
                        for (int l = 0; l < neighbours.size(); l++)
                            if (neighbours.get(l).isMined()) nearbyMinesCounter++;
                        assertEquals(Integer.toUnsignedLong(Board.arrayOfAllCells.get(j).get(k).getNearbyMines()), nearbyMinesCounter);
                    }
            assertEquals(99, minesCounter);
        }
    }

    @Test
    public void fieldOpening() {
        board8.board();
        board8.setActionListener(BoardTest.this);
        Board.arrayOfAllCells.get(5).get(0).setNearbyMines(1);
        Board.arrayOfAllCells.get(5).get(1).setNearbyMines(2);
        Board.arrayOfAllCells.get(6).get(2).setNearbyMines(2);
        Board.arrayOfAllCells.get(7).get(2).setNearbyMines(1);
        Board.arrayOfAllCells.get(6).get(0).setNearbyMines(2);
        Board.arrayOfAllCells.get(6).get(1).setNearbyMines(3);
        Board.arrayOfAllCells.get(7).get(1).setNearbyMines(1);
        board8.fieldOpening(Board.arrayOfAllCells.get(7).get(0));

        List<Cell> openCellsManually = new ArrayList<>();
        openCellsManually.add(Board.arrayOfAllCells.get(6).get(0));
        openCellsManually.add(Board.arrayOfAllCells.get(6).get(1));
        openCellsManually.add(Board.arrayOfAllCells.get(7).get(1));

        List<Cell> openCellsProgrammatically = new ArrayList<>();
        for (int i = 0; i < 8; i++)
            for (int j = 0; j < 8; j++)
                if (Board.arrayOfAllCells.get(i).get(j).isOpen())
                    openCellsProgrammatically.add(Board.arrayOfAllCells.get(i).get(j));
        assertFalse(assertListEquals(openCellsManually, openCellsProgrammatically));

        openCellsManually.add(Board.arrayOfAllCells.get(7).get(0));
        assertTrue(assertListEquals(openCellsManually, openCellsProgrammatically));

        board8.board();
        Board.arrayOfAllCells.get(5).get(0).setNearbyMines(1);
        Board.arrayOfAllCells.get(5).get(1).setNearbyMines(2);
        Board.arrayOfAllCells.get(6).get(1).setNearbyMines(1);
        Board.arrayOfAllCells.get(6).get(2).setNearbyMines(2);
        Board.arrayOfAllCells.get(7).get(2).setNearbyMines(1);
        Board.arrayOfAllCells.get(4).get(0).setNearbyMines(1);
        Board.arrayOfAllCells.get(4).get(1).setNearbyMines(1);
        Board.arrayOfAllCells.get(5).get(2).setNearbyMines(1);
        board8.fieldOpening(Board.arrayOfAllCells.get(7).get(0));

        openCellsManually.clear();
        openCellsManually.add(Board.arrayOfAllCells.get(6).get(0));
        openCellsManually.add(Board.arrayOfAllCells.get(7).get(0));
        openCellsManually.add(Board.arrayOfAllCells.get(5).get(0));
        openCellsManually.add(Board.arrayOfAllCells.get(5).get(1));
        openCellsManually.add(Board.arrayOfAllCells.get(6).get(1));
        openCellsManually.add(Board.arrayOfAllCells.get(7).get(1));
        openCellsManually.add(Board.arrayOfAllCells.get(6).get(2));

        openCellsProgrammatically.clear();
        for (int i = 0; i < 8; i++)
            for (int j = 0; j < 8; j++)
                if (Board.arrayOfAllCells.get(i).get(j).isOpen())
                    openCellsProgrammatically.add(Board.arrayOfAllCells.get(i).get(j));
        assertFalse(assertListEquals(openCellsManually, openCellsProgrammatically));

        openCellsManually.add(Board.arrayOfAllCells.get(7).get(2));
        assertTrue(assertListEquals(openCellsManually, openCellsProgrammatically));
    }

    @Test
    public void winResultTest() {
        board10.board();
        board10.setActionListener(BoardTest.this);
        board10.fieldRandomFilling(Board.arrayOfAllCells.get(0).get(0));
        board10.fieldOpening(Board.arrayOfAllCells.get(0).get(0));
        assertTrue(board10.winResult());

        board8.board();
        board8.setActionListener(BoardTest.this);
        board8.fieldRandomFilling(Board.arrayOfAllCells.get(0).get(0));
        board8.fieldOpening(Board.arrayOfAllCells.get(0).get(0));
        assertFalse(board8.winResult());
    }

    @Override
    public void mineAdded(Cell cell) {

    }

    @Override
    public void numOfNearbyMinesAdded(Cell cell, Integer digit) {

    }

    @Override
    public void openCell(Cell cell) {

    }

    @Override
    public void markCell(Cell cell) {

    }

    @Override
    public void unMarkCell(Cell cell) {

    }

    @Override
    public void defeat() {

    }

    @Override
    public void winResult() {

    }
}


