package com.example.minesweeper.model;

import static com.example.minesweeper.model.Board.arrayOfAllCells;
import static com.example.minesweeper.model.Cell.getNeighbours;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;


public class Solver {

    private final int rows;
    private final int columns;
    private final int mines;
    private boolean firstMove = true;


    public Solver(int fieldLength, int fieldHeight, int numOfMines) {
        this.rows = fieldHeight;
        this.columns = fieldLength;
        this.mines = numOfMines;
    }

    public Map<Cell, Integer> solve(List<List<Cell>> allCells) {
        Map<Cell, Integer> solution;
        List<GroupOfCells> groups = createGroups(allCells);
        solution = getSolution(groups);
        if (firstMove) solution.put(allCells.get(0).get(0), 0);
        firstMove = false;
        if (solution.isEmpty()) solution = getSolutionUsingProbability(groups);;
        return solution;
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
        do {
            removeAllDuplicates(groups);
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
                    } else if (isOverlaps(parent, child)) {
                        if (groupI.getNumOfMines() > groupJ.getNumOfMines()) {
                            parent = groupI;
                            child = groupJ;
                        } else {
                            parent = groupJ;
                            child = groupI;
                        }
                        List<GroupOfCells> newGroups = newGroupsDueToCrossing(parent, child);
                        if (newGroups.size() == 3) {
                            groups.addAll(newGroups);
                            groups.remove(j);
                            groups.remove(i);
                            i = 0;
                            j = 0;
                            removeAllDuplicates(groups);
                        }
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

    private boolean isOverlaps(GroupOfCells parent, GroupOfCells child) {
        for (int i = 0; i < child.getGroup().size(); i++)
            if (parent.getGroup().contains(child.getGroup().get(i)))
                return true;

        return false;
    }

    private List<GroupOfCells> newGroupsDueToCrossing(GroupOfCells parent, GroupOfCells child) {
        List<GroupOfCells> newGroups = new ArrayList<>();
        List<Cell> newCellsForGroup = new ArrayList<>();
        for (int i = 0; i < child.getGroup().size(); i++)
            if (parent.getGroup().contains(child.getGroup().get(i)))
                newCellsForGroup.add(child.getGroup().get(i));

        GroupOfCells newGroup = new GroupOfCells(newCellsForGroup,
                parent.getNumOfMines() - (child.getGroup().size() - newCellsForGroup.size()));
        newGroups.add(newGroup);
        if (newGroup.getNumOfMines().equals(child.getNumOfMines())) {
            newGroups.add(subtraction(parent, newGroup));
            newGroups.add(subtraction(child, newGroup));
        }
        return newGroups;
    }

    private void removeAllDuplicates(List<GroupOfCells> groups) {
        Set<Integer> helperSet = new HashSet<>();
        for (int i = 0; i < groups.size() - 1; i++)
            for (int j = i + 1; j < groups.size(); j++)
                if (groups.get(i).equals(groups.get(j)))
                    helperSet.add(j);

        List<Integer> helperList = new ArrayList<>(helperSet);
        for (int i = helperList.size() - 1; i >= 0; i--)
            groups.remove((int) helperList.get(i));
    }

    private Map<Cell, Integer> checkForClick(List<GroupOfCells> groups) {
        Map<Cell, Integer> solution = new HashMap<>();
        for (GroupOfCells group : groups) {
            if (group.getGroup().size() != 0)
                if (group.getGroup().size() == group.getNumOfMines())
                    solution.put(group.getGroup().get(0), 1);
                else if (group.getNumOfMines() == 0)
                    solution.put(group.getGroup().get(0), 0);
        }
        return solution;
    }

    private Map<Cell, Integer> getSolutionUsingProbability(List<GroupOfCells> groups) {
        Map<Cell, Double> cells = new HashMap<>();
        for (GroupOfCells group : groups)
            for (Cell cell : group.getGroup()) {
                Double value;
                if ((value = cells.get(cell)) == null)
                    cells.put(cell, (double) group.getNumOfMines() / group.getGroup().size());
                else
                    cells.put(cell, 1 - (1 - value) * (1 - group.getNumOfMines() / group.getGroup().size()));
            }
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

