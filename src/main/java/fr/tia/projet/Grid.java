package fr.tia.projet;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class Grid {
    private final int rowAmount;
    private final int colAmount;
    private final ArrayList<Cell> cells;

    public Grid(int rowAmount, int colAmount) {
        this.rowAmount = rowAmount;
        this.colAmount = colAmount;
        this.cells = new ArrayList<>();

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
