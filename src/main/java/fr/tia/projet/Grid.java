package fr.tia.projet;

import java.util.*;

public class Grid {
    private final int rowAmount;
    private final int colAmount;
    private final List<Cell> cells;
    private Map<Cell, Agent> agents;

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

    public void setAgents(Map<Cell, Agent> agents) {
        this.agents = agents;
    }

    public Cell getCell(int row, int col) {
        if (row < 0 || row >= getRowAmount() || col < 0 || col >= getColAmount())
            return null;

        return cells.get(row * getColAmount() + col);
    }

    public List<Cell> getCells() {
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

    public Agent moveAgent(int fromRow, int fromCol, int toRow, int toCol) {
        Cell fromCell = this.getCell(fromRow, fromCol);
        Cell toCell = this.getCell(toRow, toCol);

        if (fromCell == null || toCell == null)
            return null;

        synchronized (agents) {
            if (this.agents.containsKey(toCell))
                return this.agents.get(toCell);

            Agent agent = this.agents.remove(fromCell);

            if (agent != null)
                this.agents.put(toCell, agent);

            return null;
        }
    }

    public void removeAgent(int row, int col) {
        Cell cell = this.getCell(row, col);
        if (cell == null)
            return;

        synchronized (agents) {
            System.out.println("valeurs : " + row + " et " + col);
            System.out.println(this.agents);
            if (!this.agents.containsKey(cell))
                return;

            Agent agent = this.agents.remove(cell);
            System.out.println("agent removed!!!");
        }
    }

    public boolean hasAgent(Cell cell) {
        return this.agents.containsKey(cell);
    }

    public Agent getAgent(Cell cell) {
        return this.agents.get(cell);
    }

    public Cell getCellFromAgent(Agent agent) {
        for (Cell cell : getCells()) {
            if (hasAgent(cell) && getAgent(cell).equals(agent)) {
                return cell;
            }
        }

        return null;
    }

    public void display() {
        for (Cell cell : getCells()) {
            Character c;
            Agent agent;
            synchronized (agents) {
                agent = getAgent(cell);
            }

            if (agent != null) {
                c = agent.getC();
            } else {
                c = '0';
            }

            System.out.print(c + " ");

            if (cell.getCol() == getColAmount()-1) {
                System.out.println();
            }
        }
        System.out.println();
    }

    @Override
    public Grid clone() {
        Grid grid = new Grid(getRowAmount(), getColAmount());

        for (Map.Entry<Cell, Agent> entry : agents.entrySet()) {
            grid.setAgent(entry.getKey().getRow(), entry.getKey().getCol(), entry.getValue());
        }

        return grid;
    }

    public void copyAgent(Grid toGrid) {
        synchronized (agents) {
            toGrid.setAgents(new HashMap<>(agents));
        }
    }
}
