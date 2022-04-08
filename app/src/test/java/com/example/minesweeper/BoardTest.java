package com.example.minesweeper;

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

public class BoardTest {
    Board board8 = new Board(8, 8, 10);
    Board board10 = new Board(10, 10, 99);

    private List<List<Cell>> newCells(Integer length, Integer height) {
        List<List<Cell>> newCells = new ArrayList<>(height);
        for (int i = 0; i < height; i++) {
            newCells.add(i, new ArrayList<>(length));
            for (int j = 0; j < length; j++)
                newCells.get(i).add(j, new Cell(i, j, false, false, false, 0));
        }
        return newCells;
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
    public void getCell() {
        List<List<Cell>> cells8 = newCells(8, 8);
        board8.board();
        assertEquals(board8.getCell(5, 5), cells8.get(5).get(5));
        assertNotEquals(board8.getCell(5, 5), cells8.get(1).get(1));
        assertEquals(board8.getCell(0, 0), cells8.get(0).get(0));
        assertNotEquals(board8.getCell(5, 5), null);
        assertNull(board8.getCell(-1, 4));
        assertNull(board8.getCell(4, -1));
        assertNull(board8.getCell(-1, -1));
        assertNull(board8.getCell(8, 8));
        assertNull(board8.getCell(20, 5));
        assertNull(board8.getCell(5, 20));

        List<List<Cell>> cells10 = newCells(10, 10);
        board10.board();
        assertEquals(board10.getCell(7, 3), cells10.get(7).get(3));
        assertNotEquals(board10.getCell(7, 3), cells10.get(5).get(4));
        assertEquals(board10.getCell(9, 9), cells10.get(9).get(9));
        assertNotEquals(board10.getCell(6, 1), null);
        assertNull(board10.getCell(-1, 5));
        assertNull(board10.getCell(5, -1));
        assertNull(board10.getCell(-1, -1));
        assertNull(board10.getCell(10, 10));
        assertNull(board10.getCell(20, 5));
        assertNull(board10.getCell(5, 20));
    }
}


