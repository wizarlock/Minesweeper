package com.example.minesweeper.model;

import static com.example.minesweeper.model.Cell.getNeighbours;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Random;
import java.util.Set;


public class Solver {
    private final int rows;
    private final int columns;
    private boolean firstMove = true;


    public Solver(int fieldLength, int fieldHeight) {
        this.rows = fieldHeight;
        this.columns = fieldLength;
    }

    public Map.Entry<Cell, Integer> solve(List<List<Cell>> allCells) {
        Map.Entry<Cell, Integer> answer = null;
        Map<Cell, Integer> solution = new HashMap<>();
        if (firstMove) {
            solution.put(allCells.get(0).get(0), 0);
            firstMove = false;
        } else {
            List<GroupOfCells> groups = createGroups(allCells);
            solution = getSolution(groups);
            if (solution.isEmpty()) solution = getSolutionUsingProbability(groups);
            if (solution.isEmpty()) {
                Cell randomCell = allCells.get(new Random().nextInt(rows)).get(new Random().nextInt(columns));
                while (randomCell.isOpen() || randomCell.isMarked())
                    randomCell = allCells.get(new Random().nextInt(rows)).get(new Random().nextInt(columns));
                solution.put(randomCell, 0);
            }
        }
        for (Map.Entry<Cell, Integer> entry : solution.entrySet())
            answer = entry;
        return answer;
    }

    //Создает список групп клеток, связанных одним значением открытого поля
    private List<GroupOfCells> createGroups(List<List<Cell>> allCells) {
        List<GroupOfCells> groups = new ArrayList<>();
        for (int i = 0; i < rows; i++)
            for (int j = 0; j < columns; j++)
                if (allCells.get(i).get(j).isOpen() && allCells.get(i).get(j).getNearbyMines() != 0) {
                    List<Cell> possibleGroup = new ArrayList<>();
                    List<Cell> neighbours = getNeighbours(allCells.get(i).get(j), columns, rows);
                    Integer counterOfFlags = 0;
                    for (int l = 0; l < neighbours.size(); l++) {
                        if (!neighbours.get(l).isOpen())
                            if (!neighbours.get(l).isMarked())
                                possibleGroup.add(neighbours.get(l));
                            else counterOfFlags++;
                    }
                    if (possibleGroup.size() != 0) groups.add(new GroupOfCells(possibleGroup,
                            allCells.get(i).get(j).getNearbyMines() - counterOfFlags));
                }
        return groups;
    }

    //Разбивает группы на более мелкие и удаляет повторяющиеся
    private Map<Cell, Integer> getSolution(List<GroupOfCells> groups) {
        Map<Cell, Integer> solution;
        boolean repeat;
        removeAllDuplicates(groups);
        do {
            repeat = false;
            for (int i = 0; i < groups.size() - 1; i++)
                for (int j = i + 1; j < groups.size(); j++) {
                    GroupOfCells groupI = groups.get(i);
                    GroupOfCells groupJ = groups.get(j);
                    GroupOfCells parent;
                    GroupOfCells child;
                    int indexOfParent = i;
                    if (groupI.getGroup().size() > groupJ.getGroup().size()) {
                        parent = groupI;
                        child = groupJ;
                    } else {
                        parent = groupJ;
                        indexOfParent = j;
                        child = groupI;
                    }
                    if (parent.getGroup().containsAll(child.getGroup())) {
                        groups.set(indexOfParent, subtraction(parent, child));
                        repeat = true;
                        removeAllDuplicates(groups);
                    }
                    solution = checkForClick(groups);
                    if (!solution.isEmpty()) return solution;
                }
        }
        while (repeat);
        solution = checkForClick(groups);
        return solution;
    }

    private GroupOfCells subtraction(GroupOfCells parent, GroupOfCells child) {
        List<Cell> newCellsForGroup = new ArrayList<>();
        for (int i = 0; i < parent.getGroup().size(); i++)
            if (!child.getGroup().contains(parent.getGroup().get(i)))
                newCellsForGroup.add(parent.getGroup().get(i));

        return new GroupOfCells(newCellsForGroup, parent.getNumOfMines() - child.getNumOfMines());
    }

    private void removeAllDuplicates(List<GroupOfCells> groups) {
        Set<GroupOfCells> helperSet = new HashSet<>(groups);
        groups.clear();
        groups.addAll(helperSet);
    }

    private Map<Cell, Integer> checkForClick(List<GroupOfCells> groups) {
        Map<Cell, Integer> solution = new HashMap<>();
        for (GroupOfCells group : groups) {
            if (group.getGroup().size() != 0)
                if (group.getGroup().size() == group.getNumOfMines()) {
                    solution.put(group.getGroup().get(0), 1);
                    break;
                } else if (group.getNumOfMines() == 0) {
                    solution.put(group.getGroup().get(0), 0);
                    break;
                }
        }
        return solution;
    }

    private Map<Cell, Integer> getSolutionUsingProbability(List<GroupOfCells> groups) {
        Map<Cell, Double> cells = new HashMap<>();
        for (GroupOfCells group : groups)
            for (Cell cell : group.getGroup()) {
                double curVal = (double) group.getNumOfMines() / group.getGroup().size();
                if ((cells.get(cell)) == null)
                    cells.put(cell, curVal);
                else {
                    cells.put(cell, 1 - (1 - cells.get(cell)) * (1 - curVal));
                }
            }
        boolean repeat;
        do {
            repeat = false;
            for (GroupOfCells group : groups) {
                List<Cell> cellsInGroup = group.getGroup();
                double sum = 0;
                for (Map.Entry<Cell, Double> entry : cells.entrySet())
                    if (cellsInGroup.contains(entry.getKey())) sum += entry.getValue();
                if (Math.abs(sum - group.getNumOfMines()) > 1) {
                    for (Cell cell : cellsInGroup)
                        cells.put(cell, cells.get(cell) * (group.getNumOfMines() / sum));
                    repeat = true;
                }
            }
        } while (repeat);

        Cell minCell = null;
        Double minValue = 1.0;
        for (Map.Entry<Cell, Double> entry : cells.entrySet())
            if (minValue > entry.getValue()) {
                minValue = entry.getValue();
                minCell = entry.getKey();
            }
        Map<Cell, Integer> solution = new HashMap<>();
        if (minCell != null) solution.put(minCell, 0);
        return solution;
    }
}

