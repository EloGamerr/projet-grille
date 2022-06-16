package fr.tia.projet;

import java.util.HashMap;
import java.util.Map;

public class Grid {
    private final int rowAmount;
    private final int colAmount;
    private final Map<Integer, Cell> cells;

    public Grid(int rowAmount, int colAmount) {
        this.rowAmount = rowAmount;
        this.colAmount = colAmount;
        this.cells = new HashMap<>();

        for (int row = 0; row < rowAmount; ++row) {
            for (int col = 0; col < colAmount; ++col) {
                this.cells.put(row * colAmount + col, new Cell(row, col));
            }
        }
    }

    public Cell getCell(int row, int col) {
        return cells.get(row * colAmount + col);
    }

    public Map<Integer, Cell> getCells() {
        return cells;
    }

    public GridSearch toGridSearch(Cell startCell) {
        return new GridSearch(this, startCell);
    }

    public int getRowAmount() {
        return rowAmount;
    }

    public int getColAmount() {
        return colAmount;
    }
}
