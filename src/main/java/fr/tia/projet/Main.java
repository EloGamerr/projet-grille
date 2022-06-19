package fr.tia.projet;

import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

public class Main {
    public static Map<Agent, List<Message>> messages;

    public static void main(String[] args) {
        // "ConcurrentHashMap" permet de rendre la map thread-safe
        messages = new ConcurrentHashMap<>();

        Simulation.instance().defaultGrid();
        Grid grid_start = Simulation.instance().getGridStart();
        Grid grid_end = Simulation.instance().getGridEnd();

        new SwingView(800, 620, grid_start, grid_end);
    }
}
