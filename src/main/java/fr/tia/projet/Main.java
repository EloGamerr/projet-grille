package fr.tia.projet;

import java.beans.PropertyChangeSupport;
import java.util.ArrayList;
import java.util.List;

public class Main {
    private static final long MEGABYTE = 1024L * 1024L;
    public static long bytesToMegabytes(long bytes) {
        return bytes / MEGABYTE;
    }

    public static void main(String[] args) throws InterruptedException {

        Simulation.instance().defaultGrid();
        Grid grid_start = Simulation.instance().getGridStart();
        Grid grid_end = Simulation.instance().getGridEnd();

        SwingView view = new SwingView(800, 620, grid_start, grid_end);

        // x x A B x
        // x o c x x
        // x x x D x
        // x x x x x
    }
}
