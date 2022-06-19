package fr.tia.projet;

import java.util.Set;
import java.util.stream.Collectors;

public class GridSearch {
    private final Grid grid;
    private final Agent agent;
    private final Set<Cell> blockedCells;

    public GridSearch(Grid grid, Agent agent, Set<Cell> blockedCells) {
        this.grid = grid;
        this.agent = agent;
        this.blockedCells = blockedCells;
    }

    public Grid getGrid() {
        return grid;
    }

    public Set<Cell> getBlockedCells() {
        return blockedCells;
    }

    public Agent getAgent() {
        return agent;
    }

    /**
     * @return Toutes les cellules accessibles
     */
    public Set<Cell> getFreeCells() {
        return grid.getCells().stream().filter(cell -> !blockedCells.contains(cell) && (!grid.hasAgent(cell) || agent == null || grid.getAgent(cell).equals(agent))).collect(Collectors.toSet());
    }

    public Cell getCell(int row, int col) {
        if (row < 0 || row >= grid.getRowAmount() || col < 0 || col >= grid.getColAmount())
            return null;

        Cell cell = grid.getCells().get(row * grid.getColAmount() + col);

        if (cell == null || (agent != null && grid.hasAgent(cell) && !grid.getAgent(cell).equals(agent)) || blockedCells.contains(cell))
            return null;

        return cell;
    }
}
