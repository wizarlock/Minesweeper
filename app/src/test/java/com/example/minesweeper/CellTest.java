package com.example.minesweeper;

import static com.example.minesweeper.model.Cell.getCell;
import static com.example.minesweeper.model.Cell.getNeighbours;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotEquals;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;

import com.example.minesweeper.model.Board;
import com.example.minesweeper.model.Cell;

import org.junit.Test;

import java.util.ArrayList;
import java.util.List;

public class CellTest {

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
}

