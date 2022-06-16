package fr.tia.projet;

import java.util.Set;
import java.util.stream.Collectors;

public class GridSearch {
    private final Grid grid;
    private final Cell startCell;

    public GridSearch(Grid grid, Cell startCell) {
        this.grid = grid;
        this.startCell = startCell;
    }

    public Grid getGrid() {
        return grid;
    }

    public Cell getStartCell() {
        return startCell;
    }

    public Set<Cell> getFreeCells() {
        return grid.getCells().stream().filter(c -> c.getC() == null || c.equals(startCell)).collect(Collectors.toSet());
    }

    public Cell getCell(int row, int col) {
        if (row < 0 || row >= grid.getRowAmount() || col < 0 || col >= grid.getColAmount())
            return null;

        Cell cell = grid.getCells().get(row * grid.getColAmount() + col);

        if (cell == null || (cell.getC() != null && !cell.getC().equals(startCell)))
            return null;

        return cell;
    }
}
