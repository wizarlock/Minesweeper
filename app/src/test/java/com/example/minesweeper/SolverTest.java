package com.example.minesweeper;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

import com.example.minesweeper.model.Board;
import com.example.minesweeper.model.Cell;
import com.example.minesweeper.model.Solver;

import org.junit.Test;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class SolverTest {

    private final int fieldLengthForBoard8 = 8;
    private final int fieldHeightForBoard8 = 8;
    Board board8 = new Board(fieldLengthForBoard8, fieldHeightForBoard8, 10);

    @Test
    public void solve() {
        Solver gameSolver = new Solver(fieldLengthForBoard8, fieldLengthForBoard8);
        board8.board();

        Map.Entry<Cell, Integer> solution = gameSolver.solve(Board.arrayOfAllCells);
        assertEquals(solution.getKey(), Board.arrayOfAllCells.get(0).get(0));
        assertEquals(Integer.toUnsignedLong(solution.getValue()), 0);

        Board.arrayOfAllCells.get(0).get(0).setOpen();
        Board.arrayOfAllCells.get(0).get(0).setNearbyMines(1);
        Board.arrayOfAllCells.get(1).get(1).setOpen();
        Board.arrayOfAllCells.get(1).get(1).setNearbyMines(3);
        Board.arrayOfAllCells.get(2).get(1).setOpen();
        Board.arrayOfAllCells.get(2).get(1).setNearbyMines(2);
        solution = gameSolver.solve(Board.arrayOfAllCells);
        List<Cell> possibleCell = new ArrayList<>();
        possibleCell.add(Board.arrayOfAllCells.get(1).get(0));
        possibleCell.add(Board.arrayOfAllCells.get(0).get(1));

        assertTrue(possibleCell.contains(solution.getKey()));
        assertEquals(Integer.toUnsignedLong(solution.getValue()), 0);

        board8.board();
        Board.arrayOfAllCells.get(0).get(0).setOpen();
        Board.arrayOfAllCells.get(0).get(0).setNearbyMines(3);
        solution = gameSolver.solve(Board.arrayOfAllCells);
        possibleCell.clear();
        possibleCell.add(Board.arrayOfAllCells.get(1).get(0));
        possibleCell.add(Board.arrayOfAllCells.get(0).get(1));
        possibleCell.add(Board.arrayOfAllCells.get(1).get(1));

        assertTrue(possibleCell.contains(solution.getKey()));
        assertEquals(Integer.toUnsignedLong(solution.getValue()), 1);

        board8.board();
        Board.arrayOfAllCells.get(0).get(0).setOpen();
        Board.arrayOfAllCells.get(0).get(0).setNearbyMines(1);
        Board.arrayOfAllCells.get(1).get(0).setOpen();
        Board.arrayOfAllCells.get(1).get(0).setNearbyMines(1);
        solution = gameSolver.solve(Board.arrayOfAllCells);
        possibleCell.clear();
        possibleCell.add(Board.arrayOfAllCells.get(2).get(0));
        possibleCell.add(Board.arrayOfAllCells.get(2).get(1));

        assertTrue(possibleCell.contains(solution.getKey()));
        assertEquals(Integer.toUnsignedLong(solution.getValue()), 0);

        board8.board();
        Board.arrayOfAllCells.get(0).get(0).setOpen();
        Board.arrayOfAllCells.get(0).get(0).setNearbyMines(0);
        Board.arrayOfAllCells.get(0).get(1).setOpen();
        Board.arrayOfAllCells.get(0).get(1).setNearbyMines(1);
        Board.arrayOfAllCells.get(1).get(0).setOpen();
        Board.arrayOfAllCells.get(1).get(0).setNearbyMines(2);
        Board.arrayOfAllCells.get(1).get(1).setOpen();
        Board.arrayOfAllCells.get(1).get(1).setNearbyMines(3);
        solution = gameSolver.solve(Board.arrayOfAllCells);
        possibleCell.clear();
        possibleCell.add(Board.arrayOfAllCells.get(2).get(0));
        possibleCell.add(Board.arrayOfAllCells.get(2).get(1));

        assertTrue(possibleCell.contains(solution.getKey()));
        assertEquals(Integer.toUnsignedLong(solution.getValue()), 1);

        board8.board();
        Board.arrayOfAllCells.get(0).get(0).setOpen();
        Board.arrayOfAllCells.get(0).get(0).setNearbyMines(0);
        Board.arrayOfAllCells.get(0).get(1).setOpen();
        Board.arrayOfAllCells.get(0).get(1).setNearbyMines(1);
        Board.arrayOfAllCells.get(1).get(0).setOpen();
        Board.arrayOfAllCells.get(1).get(0).setNearbyMines(1);
        Board.arrayOfAllCells.get(1).get(1).setOpen();
        Board.arrayOfAllCells.get(1).get(1).setNearbyMines(3);
        solution = gameSolver.solve(Board.arrayOfAllCells);

        assertEquals(solution.getKey(), Board.arrayOfAllCells.get(2).get(2));
        assertEquals(Integer.toUnsignedLong(solution.getValue()), 1);

        board8.board();
        Board.arrayOfAllCells.get(0).get(0).setOpen();
        Board.arrayOfAllCells.get(0).get(0).setNearbyMines(1);
        Board.arrayOfAllCells.get(0).get(1).setOpen();
        Board.arrayOfAllCells.get(0).get(1).setNearbyMines(1);
        Board.arrayOfAllCells.get(1).get(0).setOpen();
        Board.arrayOfAllCells.get(1).get(0).setNearbyMines(1);
        solution = gameSolver.solve(Board.arrayOfAllCells);

        assertEquals(solution.getKey(), Board.arrayOfAllCells.get(1).get(1));
        assertEquals(Integer.toUnsignedLong(solution.getValue()), 1);
    }
}
