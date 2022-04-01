package com.example.checkers.model;

import android.view.View;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

import static com.example.checkers.model.Checker.Colors.BLACK;
import static com.example.checkers.model.Checker.Colors.WHITE;

import com.example.checkers.data.OnCheckerActionListener;
import com.example.checkers.ui.Game;

public class CheckersDesk {
    private Boolean blacksMoves = false;
    private int numberOfClicks;
    private Cell selectedCell;
    private Cell eatingCell;
    private List<CellsForEating> requiredMovesForChecker;
    private List<Cell> ordinaryMovesForChecker;
    private int scoreForBlack = 0;
    private int scoreForWhite = 0;
    private static final int ROWS = 8;
    private final int COLUMNS = 8;

    public void setOnCheckerActionListener(OnCheckerActionListener onCheckerActionListener) {
        this.onCheckerActionListener = onCheckerActionListener;
    }

    private OnCheckerActionListener onCheckerActionListener;

    public static List<List<Cell>> arrayOfAllCells;

    public void checkersDesk() {
        arrayOfAllCells = new ArrayList<>(ROWS);

        for (int i = 0; i < ROWS; i++) {
            arrayOfAllCells.add(i, new ArrayList<>(COLUMNS));
            for (int j = 0; j < COLUMNS; j++) {
                arrayOfAllCells.get(i).add(j, new Cell(i, j, null));
            }
        }
    }

    public void initDesk() {
        for (int i = 0; i < ROWS; i++)
            for (int j = 0; j < COLUMNS; j++) {
                if ((i + j) % 2 != 0) {
                    Checker checker = null;
                    if (i < 3) checker = new Checker(BLACK, false);
                    else if (i > ROWS - 4) checker = new Checker(WHITE, false);
                    if (checker != null) {
                        Objects.requireNonNull(getCell(i, j)).setChecker(checker);
                        if (onCheckerActionListener != null)
                            onCheckerActionListener.onCheckerAdded(new Cell(i, j, checker));
                    }
                }
            }
    }

    private Cell getCell(int posY, int posX) {
        Cell cell = new Cell(posY, posX, null);
        if (cell.cellExist(cell.getY(), cell.getX())) return arrayOfAllCells.get(posY).get(posX);
        else return null;
    }

    private boolean becomingQueen(Cell cell) {
        if (cell.getChecker() != null)
            if ((cell.getChecker().getColor() == BLACK && cell.getY() == 7) || (cell.getChecker().getColor() == WHITE && cell.getY() == 0)) {
                cell.getChecker().setCondition(true);
                return true;
            }
        return false;
    }

    private List<Cell> ordinaryMoves(Cell cell) {
        if (cell.getChecker().getCondition()) return ordinaryMovesForQueen(cell);
        else return ordinaryMovesForOrdinaryChecker(cell);
    }

    private List<Cell> ordinaryMovesForQueen(Cell cell) {
        List<Cell> moveOpportunitiesForQueen = new ArrayList<>();
        List<Coordinates> coordinates = initCoordinates();
        int coefficient = 1;
        for (Coordinates coordinate : coordinates) {
            while (getCell(cell.getY() + coefficient * coordinate.getY(), cell.getX() + coefficient * coordinate.getX()) != null) {
                Cell potentialMove = getCell(cell.getY() + coefficient * coordinate.getY(), cell.getX() + coefficient * coordinate.getX());
                if (Objects.requireNonNull(potentialMove).getChecker() == null)
                    moveOpportunitiesForQueen.add(potentialMove);
                else break;
                coefficient++;
            }
            coefficient = 1;
        }
        return moveOpportunitiesForQueen;
    }

    private List<Cell> ordinaryMovesForOrdinaryChecker(Cell cell) {
        List<Cell> moveOpportunitiesForDefaultChecker = new ArrayList<>();
        List<Coordinates> coordinates = initCoordinatesForBlackOrWhite(blacksMoves);
        for (Coordinates coordinate : coordinates) {
            Cell potentialMove = getCell(cell.getY() + coordinate.getY(), cell.getX() + coordinate.getX());
            if (potentialMove != null && potentialMove.getChecker() == null)
                moveOpportunitiesForDefaultChecker.add(potentialMove);
        }
        return moveOpportunitiesForDefaultChecker;
    }

    private List<CellsForEating> requiredMoves() {
        List<CellsForEating> requiredMoves = new ArrayList<>();
        for (int i = 0; i < ROWS; i++) {
            for (int j = 0; j < COLUMNS; j++) {
                Cell cell = getCell(i, j);
                if (Objects.requireNonNull(cell).getChecker() != null && checkCorrectnessColor(cell))
                    if (cell.getChecker().getCondition())
                        requiredMoves.addAll(requiredMovesForQueen(cell));
                    else requiredMoves.addAll(requiredMovesForOrdinaryChecker(cell));
            }
        }
        return requiredMoves;
    }

    private List<CellsForEating> requiredMovesForQueen(Cell cell) {
        List<Coordinates> coordinates = initCoordinates();
        List<CellsForEating> requiredMoves = new ArrayList<>();
        int coefficient = 1;
        for (Coordinates coordinate : coordinates) {
            while (getCell(cell.getY() + (coefficient + 1) * coordinate.getY(), cell.getX() + (coefficient + 1) * coordinate.getX()) != null) {
                Cell potentialEaten = getCell(cell.getY() + coefficient * coordinate.getY(), cell.getX() + coefficient * coordinate.getX());
                if (Objects.requireNonNull(potentialEaten).getChecker() != null) {
                    if (potentialEaten.getChecker().getColor() != cell.getChecker().getColor()) {
                        while (getCell(cell.getY() + (coefficient + 1) * coordinate.getY(), cell.getX() + (coefficient + 1) * coordinate.getX()) != null) {
                            Cell potentialMove = getCell(cell.getY() + (coefficient + 1) * coordinate.getY(), cell.getX() + (coefficient + 1) * coordinate.getX());
                            if (Objects.requireNonNull(potentialMove).getChecker() == null) {
                                requiredMoves.add(new CellsForEating(cell, potentialEaten, potentialMove));
                                coefficient++;
                            } else break;
                        }
                    }
                    break;
                } else coefficient++;
            }
            coefficient = 1;
        }
        return requiredMoves;
    }

    private List<CellsForEating> requiredMovesForOrdinaryChecker(Cell cell) {
        List<Coordinates> coordinates = initCoordinates();
        List<CellsForEating> requiredMoves = new ArrayList<>();
        for (Coordinates coordinate : coordinates) {
            Cell potentialMove = getCell(cell.getY() + 2 * coordinate.getY(), cell.getX() + 2 * coordinate.getX());
            Cell potentialEaten = getCell(cell.getY() + coordinate.getY(), cell.getX() + coordinate.getX());
            if (potentialMove != null && potentialMove.getChecker() == null)
                if (potentialEaten.getChecker() != null && potentialEaten.getChecker().getColor() != cell.getChecker().getColor())
                    requiredMoves.add(new CellsForEating(cell, potentialEaten, potentialMove));
        }
        return requiredMoves;
    }

    private List<Coordinates> initCoordinates() {
        List<Coordinates> coordinates = new ArrayList<>();
        coordinates.add(new Coordinates(-1, -1));
        coordinates.add(new Coordinates(-1, 1));
        coordinates.add(new Coordinates(1, -1));
        coordinates.add(new Coordinates(1, 1));
        return coordinates;
    }

    private List<Coordinates> initCoordinatesForBlackOrWhite(boolean blacksMoves) {
        List<Coordinates> coordinates = new ArrayList<>();
        if (blacksMoves) {
            coordinates.add(new Coordinates(1, -1));
            coordinates.add(new Coordinates(1, 1));
        } else {
            coordinates.add(new Coordinates(-1, -1));
            coordinates.add(new Coordinates(-1, 1));
        }
        return coordinates;
    }

    private boolean checkCorrectnessColor(Cell cell) {
        Checker checker = cell.getChecker();
        return ((blacksMoves && checker.getColor() == BLACK) || (!blacksMoves && checker.getColor() == WHITE));
    }

    private void initMoves(Cell cell) {
        List<CellsForEating> requiredMoves = requiredMoves();
        List<Cell> ordinaryMoves = ordinaryMoves(cell);
        if (requiredMoves.size() != 0) {
            ordinaryMovesForChecker = null;
            requiredMovesForChecker = requiredMoves;
            onCheckerActionListener.colorForEat(requiredMovesForChecker, selectedCell);
        } else {
            requiredMovesForChecker = null;
            ordinaryMovesForChecker = ordinaryMoves;
            onCheckerActionListener.colorForMoves(ordinaryMovesForChecker);
        }
    }

    private List<CellsForEating> canEatMore(Cell cell) {
        if (cell.getChecker().getCondition()) return requiredMovesForQueen(cell);
        else return requiredMovesForOrdinaryChecker(cell);
    }

    private void updateScore () {
        if (blacksMoves) scoreForBlack++;
        else scoreForWhite++;
        onCheckerActionListener.scoreChange(scoreForBlack, scoreForWhite, blacksMoves);
    }

    private void eatStart(Cell cell) {
        for (CellsForEating list : requiredMovesForChecker)
            if (selectedCell.equals(list.getRequiredCell()) && cell.equals(list.getMoving())) {
                moving(cell);
                if (onCheckerActionListener != null)
                    onCheckerActionListener.onCheckerRemoved(list.getEaten());
                list.getEaten().setChecker(null);
                List<CellsForEating> eatMore = canEatMore(list.getMoving());
                updateScore();
                if (eatMore.size() != 0) {
                    Cell newCell = list.getMoving();
                    selectedCell = newCell;
                    eatingCell = selectedCell;
                    requiredMovesForChecker = eatMore;
                    onCheckerActionListener.colorForEat(requiredMovesForChecker, newCell);
                    onCheckerActionListener.colorForPick(Game.getCheckerLayout(newCell));
                } else {
                    eatingCell = null;
                    blacksMoves = !blacksMoves;
                    numberOfClicks = 0;
                }
            }
    }

    private void moving(Cell cell) {
        cell.setChecker(selectedCell.getChecker());
        selectedCell.setChecker(null);
        if (onCheckerActionListener != null)
            onCheckerActionListener.onCheckerMoved(selectedCell, cell);
        if (becomingQueen(cell)) {
            if (onCheckerActionListener != null) {
                onCheckerActionListener.onCheckerRemoved(cell);
                onCheckerActionListener.onQueenAdded(cell);
            }
        }
    }

    private boolean checkBlocked() {
        for (int i = 0; i < ROWS; i++)
            for (int j = 0; j < COLUMNS; j++) {
                Cell cell = getCell(i, j);
                if (cell.getChecker() != null)
                    if (checkCorrectnessColor(cell))
                        if (ordinaryMoves(cell).size() != 0) return false;
            }
        return true;
    }

    private boolean finishGame() {
        int counterForBlack = 0;
        int counterForWhite = 0;
        if (requiredMoves().size() == 0 && checkBlocked()) return true;
        for (int i = 0; i < ROWS; i++)
            for (int j = 0; j < COLUMNS; j++) {
                Cell cell = getCell(i, j);
                if (cell.getChecker() != null)
                    if (cell.getChecker().getColor() == WHITE) counterForWhite++;
                    else counterForBlack++;
            }
        return counterForBlack == 0 || counterForWhite == 0;
    }

    public void startGame(View view) {
        if (numberOfClicks == 1) {
            Cell cellForSecondClick = Objects.requireNonNull(getCell(Integer.parseInt(((View) view.getParent()).getTag().toString()), Integer.parseInt(view.getTag().toString())));
            onCheckerActionListener.boardClear();
            if (cellForSecondClick.getChecker() != null) numberOfClicks = 0;
            if (requiredMovesForChecker != null) eatStart(cellForSecondClick);
            else if (ordinaryMovesForChecker.contains(cellForSecondClick)) {
                moving(cellForSecondClick);
                blacksMoves = !blacksMoves;
                numberOfClicks = 0;
            } else numberOfClicks = 0;

            if (finishGame()) onCheckerActionListener.finish(blacksMoves);
        }
        if (numberOfClicks == 0) {
            Cell cellForFirstClick = getCell(Integer.parseInt(((View) view.getParent()).getTag().toString()),
                    Integer.parseInt(view.getTag().toString()));
            if (Objects.requireNonNull(cellForFirstClick).getChecker() != null)
                if (checkCorrectnessColor(cellForFirstClick))
                    if (eatingCell == null || eatingCell.equals(cellForFirstClick)) {
                        onCheckerActionListener.colorForPick(view);
                        selectedCell = cellForFirstClick;
                        initMoves(cellForFirstClick);
                        numberOfClicks = 1;
                    }
        }
    }
}







