package fr.tia.projet;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class Main {
    public static void main(String[] args) {
        Grid grid_start = new Grid(5, 5);

        grid_start.setAgent(0, 2, new Agent('A')); // croix-encerclée
        grid_start.setAgent(0, 3, new Agent('B')); // soleil
        grid_start.setAgent(1, 2, new Agent('C')); // sablier
        grid_start.setAgent(2, 3, new Agent('D')); // étoile

        Grid grid_end = new Grid(5, 5);

        grid_end.setAgent(0, 0, new Agent('A')); // croix-encerclée
        grid_end.setAgent(1, 1, new Agent('B')); // soleil
        grid_end.setAgent(1, 2, new Agent('C')); // sablier
        grid_end.setAgent(2, 3, new Agent('D')); // étoile

        System.out.println("Hello, world! " + search_path(new Agent('B'), grid_start, grid_end));
    }

    private static Cell search_case(Agent agent, Grid grid) {
        for (Cell cell : grid.getCells()) {
            if (grid.hasAgent(cell) && grid.getAgent(cell).equals(agent)) {
                return cell;
            }
        }

        return null;
    }

    private static ArrayList<Cell> search_path(Agent agent, Grid grid_start, Grid grid_end) {
        Cell start_case = search_case(agent, grid_start);
        Cell end_case = search_case(agent, grid_end);

        GridSearch gridSearch = grid_start.toGridSearch(agent);

        ArrayList<Cell> Q = new ArrayList<>();
        Map<Cell, Integer> d = new HashMap<>();
        Map<Cell, Cell> pred = new HashMap<>();
        for (Cell cell : gridSearch.getFreeCells()) {
            d.put(cell, Integer.MAX_VALUE);
            pred.put(cell, null);
            Q.add(cell);
        }
        d.put(start_case, 0);

        while (!Q.isEmpty()) {
            Cell s1 = trouve_min(Q, d);
            Q.remove(s1);

            Cell botCell = gridSearch.getCell(s1.getRow() + 1, s1.getCol());
            Cell topCell = gridSearch.getCell(s1.getRow() - 1, s1.getCol());
            Cell rightCell = gridSearch.getCell(s1.getRow(), s1.getCol() + 1);
            Cell leftCell = gridSearch.getCell(s1.getRow(), s1.getCol() - 1);

            if (botCell != null)
                maj_distances(s1, botCell, d, pred);
            if (topCell != null)
                maj_distances(s1, topCell, d, pred);
            if (rightCell != null)
                maj_distances(s1, rightCell, d, pred);
            if (leftCell != null)
                maj_distances(s1, leftCell, d, pred);
        }

        ArrayList<Cell> path = new ArrayList();
        Cell s = end_case;
        while (s != start_case) {
            path.add(s);
            s = pred.get(s);
        }

        path.add(start_case);

        return path;
    }

    private static Cell trouve_min(ArrayList<Cell> Q, Map<Cell, Integer> d) {
        int mini = Integer.MAX_VALUE;
        Cell sommet = null;
        for (Cell cell : Q) {
            if (d.get(cell) < mini) {
                mini = d.get(cell);
                sommet = cell;
            }
        }

        return sommet;
    }

    private static void maj_distances(Cell s1, Cell s2, Map<Cell, Integer> d, Map<Cell, Cell> pred) {
        if (d.get(s2) > d.get(s1) + 1) {
            d.put(s2, d.get(s1) + 1);
            pred.put(s2, s1);
        }
    }
}
