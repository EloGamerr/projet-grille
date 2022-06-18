package fr.tia.projet;

import java.util.Set;
import java.util.stream.Collectors;

public class GridSearch {
    private final Grid grid;
    private final Agent agent;
    private final Set<Agent> blockedAgents;

    public GridSearch(Grid grid, Agent agent, Set<Agent> blockedAgents) {
        this.grid = grid;
        this.agent = agent;
        this.blockedAgents = blockedAgents;
    }

    public Grid getGrid() {
        return grid;
    }

    public Set<Agent> getBlockedAgents() {
        return blockedAgents;
    }

    public Agent getAgent() {
        return agent;
    }

    public Set<Cell> getFreeCells() {
        return grid.getCells().stream().filter(cell -> !grid.hasAgent(cell) || ((agent == null || grid.getAgent(cell).equals(agent)) && !blockedAgents.contains(grid.getAgent(cell)))).collect(Collectors.toSet());
    }

    public Cell getCell(int row, int col) {
        if (row < 0 || row >= grid.getRowAmount() || col < 0 || col >= grid.getColAmount())
            return null;

        Cell cell = grid.getCells().get(row * grid.getColAmount() + col);

        if (cell == null || (agent != null && grid.hasAgent(cell) && !grid.getAgent(cell).equals(agent)) || blockedAgents.contains(grid.getAgent(cell)))
            return null;

        return cell;
    }
}
