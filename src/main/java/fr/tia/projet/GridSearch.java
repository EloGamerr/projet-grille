package fr.tia.projet;

import java.util.Set;
import java.util.stream.Collectors;

public class GridSearch {
    private final Grid grid;
    private final Agent agent;

    public GridSearch(Grid grid, Agent agent) {
        this.grid = grid;
        this.agent = agent;
    }

    public Grid getGrid() {
        return grid;
    }

    public Agent getAgent() {
        return agent;
    }

    public Set<Cell> getFreeCells() {
        return grid.getCells().stream().filter(cell -> !grid.hasAgent(cell) || grid.getAgent(cell).equals(agent)).collect(Collectors.toSet());
    }

    public Cell getCell(int row, int col) {
        if (row < 0 || row >= grid.getRowAmount() || col < 0 || col >= grid.getColAmount())
            return null;

        Cell cell = grid.getCells().get(row * grid.getColAmount() + col);

        if (cell == null || (grid.hasAgent(cell) && !grid.getAgent(cell).equals(agent)))
            return null;

        return cell;
    }
}
