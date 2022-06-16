package fr.tia.projet;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class Grid {
    private final int rowAmount;
    private final int colAmount;
    private final ArrayList<Cell> cells;
    private final Map<Cell, Agent> agents;

    public Grid(int rowAmount, int colAmount) {
        this.rowAmount = rowAmount;
        this.colAmount = colAmount;
        this.cells = new ArrayList<>();
        this.agents = new HashMap<>();

        for (int row = 0; row < rowAmount; ++row) {
            for (int col = 0; col < colAmount; ++col) {
                this.cells.add(new Cell(row, col));
            }
        }
    }

    public Cell getCell(int row, int col) {
        if (row < 0 || row >= getRowAmount() || col < 0 || col >= getColAmount())
            return null;

        return cells.get(row * getColAmount() + col);
    }

    public ArrayList<Cell> getCells() {
        return cells;
    }

    public GridSearch toGridSearch(Agent agent) {
        return new GridSearch(this, agent);
    }

    public int getRowAmount() {
        return rowAmount;
    }

    public int getColAmount() {
        return colAmount;
    }

    public void setAgent(int row, int col, Agent agent) {
        Cell cell = this.getCell(row, col);

        if (cell == null || agent == null)
            return;

        this.agents.put(cell, agent);
    }

    public void moveAgent(int fromRow, int fromCol, int toRow, int toCol) {
        Cell fromCell = this.getCell(fromRow, fromCol);
        Cell toCell = this.getCell(toRow, toCol);

        if (fromCell == null || toCell == null)
            return;

        Agent agent = this.agents.remove(fromCell);

        if (agent != null)
            this.agents.put(toCell, agent);
    }

    public boolean hasAgent(Cell cell) {
        return this.agents.containsKey(cell);
    }

    public Agent getAgent(Cell cell) {
        return this.agents.get(cell);
    }
}
