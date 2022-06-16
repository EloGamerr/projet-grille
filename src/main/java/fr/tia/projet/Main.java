package fr.tia.projet;

import java.util.ArrayList;

public class Main {
    public static void main(String[] args) {
        ArrayList<Cell> grid_start = new ArrayList<>();

        for (int i = 0; i < 25; ++i) {
            grid_start.add(new Cell(i));
        }

        grid_start.get(0*5 + 2).setC('A'); // croix-encerclée
        grid_start.get(0*5 + 3).setC('B'); // soleil
        grid_start.get(1*5 + 2).setC('C'); // sablier
        grid_start.get(2*5 + 3).setC('D'); // étoile

        ArrayList<Cell> grid_end = new ArrayList<>();

        for (int i = 0; i < 25; ++i) {
            grid_end.add(new Cell(i));
        }

        grid_end.get(0*5 + 0).setC('A'); // croix-encerclée
        grid_end.get(1*5 + 1).setC('B'); // soleil
        grid_end.get(1*5 + 2).setC('C'); // sablier
        grid_end.get(2*5 + 3).setC('D'); // étoile

        System.out.println("Hello, world! " + search_path('B', grid_start, grid_end));
        // x o A o x
        // x o x x x
        // x x x x x
    }

    private static Cell search_case(char c, ArrayList<Cell> grid) {
        for (int i = 0; i < grid.size(); ++i) {
            if (grid.get(i).getC() != null && grid.get(i).getC() == c) {
                return grid.get(i);
            }
        }

        return null;
    }

    private static ArrayList<Cell> search_path(char c, ArrayList<Cell> grid_start, ArrayList<Cell> grid_end) {
        Cell start_case = search_case(c, grid_start);
        Cell end_case = search_case(c, grid_end);

        ArrayList<Integer> Q = new ArrayList<>();
        ArrayList<Integer> d = new ArrayList<>();
        ArrayList<Integer> pred = new ArrayList<>();
        for (int i = 0; i < grid_start.size(); ++i) {
            d.add(Integer.MAX_VALUE);
            pred.add(null);
            if (grid_start.get(i).getC() == null)
                Q.add(i);
        }
        d.set(start_case.getId(), 0);

        while (!Q.isEmpty()) {
            int s1 = trouve_min(Q, d);
            Q.remove(s1);
            if (s1 + 1 < 25 && grid_start.get(s1 + 1).getC() == null)
                maj_distances(s1, s1+1, d, pred);
            if (s1 - 1 >= 0 && grid_start.get(s1 - 1).getC() == null)
                maj_distances(s1, s1-1, d, pred);
            if (s1 + 1*5 < 25 && grid_start.get(s1 + 1*5).getC() == null)
                maj_distances(s1, s1+1*5, d, pred);
            if (s1 - 1*5 >= 0 && grid_start.get(s1 - 1*5).getC() == null)
                maj_distances(s1, s1-1*5, d, pred);
        }

        ArrayList<Cell> path = new ArrayList();
        Cell s = end_case;
        while (s != start_case) {
            path.add(s);
            s = grid_start.get(pred.get(s.getId()));
        }

        path.add(start_case);

        return path;
    }

    private static int trouve_min(ArrayList<Integer> Q, ArrayList<Integer> d) {
        int mini = Integer.MAX_VALUE;
        int sommet = -1;
        for (int i = 0; i < Q.size(); ++i) {
            if (d.get(i) < mini) {
                mini = d.get(i);
                sommet = i;
            }
        }

        return sommet;
    }

    private static void maj_distances(int s1, int s2, ArrayList<Integer> d, ArrayList<Integer> pred) {
        if (d.get(s2) > d.get(s1) + 1) {
            d.set(s2, d.get(s1) + 1);
            pred.set(s2, s1);
        }
    }
}
