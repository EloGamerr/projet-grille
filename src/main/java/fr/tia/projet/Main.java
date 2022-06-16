package fr.tia.projet;

import java.util.ArrayList;
import java.util.List;

public class Main {
    private static final long MEGABYTE = 1024L * 1024L;
    public static long bytesToMegabytes(long bytes) {
        return bytes / MEGABYTE;
    }

    public static void main(String[] args) throws InterruptedException {
        Grid grid_start = new Grid(5, 5);

        Agent agentA = new Agent('A', grid_start, new Cell(0, 4));
        Agent agentB = new Agent('B', grid_start, null);
        Agent agentC = new Agent('C', grid_start, null);
        Agent agentD = new Agent('D', grid_start, null);
        Agent agentE = new Agent('E', grid_start, null);
        Agent agentF = new Agent('F', grid_start, null);

        grid_start.setAgent(0, 0, agentA); // croix-encerclée
        grid_start.setAgent(0, 1, agentB); // soleil
        grid_start.setAgent(1, 1, agentC); // sablier
        grid_start.setAgent(2, 1, agentD); // étoile
        grid_start.setAgent(3, 1, agentE); // étoile
        grid_start.setAgent(4, 1, agentF); // étoile

        agentA.move();
        agentB.move();
        agentC.move();
        agentD.move();
        agentE.move();
        agentF.move();

        List<Agent> agents = new ArrayList<>();
        agents.add(agentA);
        agents.add(agentB);
        agents.add(agentC);
        agents.add(agentD);
        agents.add(agentE);
        agents.add(agentF);

        while(!moveDone(agents)) {
            grid_start.display();

            Runtime runtime = Runtime.getRuntime();
            long memory = runtime.totalMemory() - runtime.freeMemory();
            System.out.println("Used memory is bytes: " + memory);
            System.out.println("Used memory is megabytes: "
                    + bytesToMegabytes(memory));

            Thread.sleep(1000);
        }


        /*SwingView view = new SwingView(800, 600, grid_start);

        Agent agentRecherche = new Agent('B');
        ArrayList<Cell> searchPath = search_path(agentRecherche, grid_start);

        Grid grid_clone = grid_start.clone();

        System.out.println("Hello, world! " + searchPath);

        System.out.println(grid_clone);


        Cell cellAgent = grid_clone.getCellFromAgent(agentRecherche);
        int lastRow = cellAgent.getRow();
        int lastCol = cellAgent.getCol();
        for (int i=searchPath.size()-1;i>=0;i--) {
            Cell pred = searchPath.get(i);
            grid_clone.moveAgent(lastRow, lastCol, pred.getRow(), pred.getCol());
            // view.createGrid(grid_clone);
        }*/
        // x x A B x
        // x o c x x
        // x x x D x
        // x x x x x
    }

    private static boolean moveDone(List<Agent> agents) {
        for (Agent agent : agents) {
            if (!agent.isMoveDone()) {
                return false;
            }
        }

        return true;
    }
}
