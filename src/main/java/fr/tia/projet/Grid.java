package fr.tia.projet;

import java.util.*;

public class Grid {
    private final int rowAmount;
    private final int colAmount;

    // A noter que le contenu de "cells" ne va jamais être modifié après l'instanciation de la classe
    // Ceci implique donc que l'accès à "cells" est thread-safe, contrairement à "agents", d'où l'utilisation
    // du mot clé "synchronized" sur cette dernière variable
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

    /**
     * Création d'une grille de recherche pour l'algorithme de Dijkstra
      */
    public GridSearch toGridSearch(Agent agent, Set<Cell> blockedCells) {
        return new GridSearch(this, agent, blockedCells);
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

    public boolean canMove(int toRow, int toCol) {
        return this.getCell(toRow, toCol) != null;
    }

    public Agent moveAgent(int fromRow, int fromCol, int toRow, int toCol) {
        Cell fromCell = this.getCell(fromRow, fromCol);
        Cell toCell = this.getCell(toRow, toCol);

        if (fromCell == null || toCell == null)
            return null;

        // On rend l'accès à "agents" thread-safe
        synchronized (agents) {
            // Impossible de faire le déplacement car un agent occupe déjà la cellule de destination
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

        // On rend l'accès à "agents" thread-safe
        synchronized (agents) {
            if (!this.agents.containsKey(cell))
                return;

            this.agents.remove(cell);
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

    /**
     * Cette méthode n'est pas thread-safe ! Elle doit être appelée par un seul thread à la fois
     * Nous n'avons pas rendu la méthode thread-safe car il était inutile de le faire étant donné que cette méthode
     * n'est jamais utilisée dans plusieurs threads à la fois
     */
    @Override
    public Grid clone() {
        Grid grid = new Grid(getRowAmount(), getColAmount());

        for (Map.Entry<Cell, Agent> entry : agents.entrySet()) {
            grid.setAgent(entry.getKey().getRow(), entry.getKey().getCol(), entry.getValue());
        }

        return grid;
    }

    public void copyAgent(Grid toGrid) {
        // On rend l'accès à "agents" thread-safe
        synchronized (agents) {
            toGrid.setAgents(new HashMap<>(agents));
        }
    }
}
