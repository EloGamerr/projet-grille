package fr.tia.projet;

import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

public class Main {
    public static Map<Agent, List<Message>> messages;

    private static final long MEGABYTE = 1024L * 1024L;
    public static long bytesToMegabytes(long bytes) {
        return bytes / MEGABYTE;
    }

    public static void main(String[] args) throws InterruptedException {

        messages = new ConcurrentHashMap<>();

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
