package com.example.checkers.data;

import android.view.View;

import com.example.checkers.model.Cell;
import com.example.checkers.model.CellsForEating;

import java.util.List;

public interface OnCheckerActionListener {
    void onCheckerAdded(Cell cell);

    void onQueenAdded(Cell cell);

    void onCheckerMoved(Cell from, Cell to);

    void onCheckerRemoved(Cell cell);

    void colorForPick(View view);

    void colorForMoves(List<Cell> cells);

    void colorForEat(List<CellsForEating> cells, Cell requiredCell);

    void boardClear();

    void finish(boolean blackMoves);

    void scoreChange(Integer newScoreForBlack, Integer newScoreForWhite, boolean blackMoves);
}
