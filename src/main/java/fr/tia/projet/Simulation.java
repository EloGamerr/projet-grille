package fr.tia.projet;

import java.beans.PropertyChangeListener;
import java.beans.PropertyChangeSupport;
import java.util.ArrayList;
import java.util.List;

public class Simulation {
    private static Simulation simulation;
    private final PropertyChangeSupport support;
    private Grid grid_start;
    private Grid grid_end;
    private List<Agent> agents;

    private Simulation() {
        support = new PropertyChangeSupport(this);
        grid_start = new Grid(Config.GRID_SIZE, Config.GRID_SIZE);
        grid_end = new Grid(Config.GRID_SIZE, Config.GRID_SIZE);
        agents = new ArrayList<>();
        // defaultGrid();
    }

    /**
     * @return singleton.
     */
    public static Simulation instance() {
        if (simulation == null) {
            simulation = new Simulation();
        }

        return simulation;
    }

    public void defaultGrid() {
        Agent agentA = new Agent('A', grid_start, new Cell(0, 4));
        Agent agentB = new Agent('B', grid_start, null);
        Agent agentC = new Agent('C', grid_start, null);
        Agent agentD = new Agent('D', grid_start, null);
        Agent agentE = new Agent('E', grid_start, null);
        Agent agentF = new Agent('F', grid_start, null);

        agents.add(agentA);
        agents.add(agentB);
        agents.add(agentC);
        agents.add(agentD);
        agents.add(agentE);
        agents.add(agentF);

        grid_start.setAgent(0, 0, agentA); // croix-encerclée
        grid_start.setAgent(0, 3, agentB); // soleil
        grid_start.setAgent(1, 1, agentC); // sablier
        grid_start.setAgent(2, 1, agentD); // étoile
        grid_start.setAgent(3, 1, agentE); // étoile
        grid_start.setAgent(4, 1, agentF); // étoile
    }

    public void addAgentStart(Character c, int rowStart, int colStart) {
        if (c == '\0') {
            System.out.println("on supprime ici");
            agents.remove(grid_start.getAgent(grid_start.getCell(rowStart, colStart)));
            grid_start.removeAgent(rowStart, colStart);
        } else {
            Agent agent = new Agent(c, grid_start, null);
            grid_start.setAgent(rowStart, colStart, agent); // croix-encerclée
            agents.add(agent);
        }

        support.firePropertyChange("gridStartUpdate", null, grid_start);
    }

    public void addAgentEnd(Character c, int rowEnd, int colEnd) {
        Agent agent = null; // setEndCase
        for (Agent a : agents) {
            if (a.getC() == c) {
                agent = a;
                break;
            }
        }
        if (agent == null) {
            System.out.println("Il n y a pas d'agent de fin");
            return;
        }
        agent.setEndCase(new Cell(rowEnd, colEnd));

        updateGridEnd();
        support.firePropertyChange("gridEndUpdate", null, grid_end);
    }

    /**
     * Retourne une grille de fin (basée sur les positions end_case des agents)
     */
    private void updateGridEnd() {
        grid_end = new Grid(Config.GRID_SIZE, Config.GRID_SIZE);
        for (Agent a : agents) {
            Cell end_case = a.getEndCase();
            if (end_case != null) {
                grid_end.setAgent(end_case.getRow(), end_case.getCol(), a);
            }
        }
    }

    public Grid getGridStart() {
        return grid_start;
    }

    public Grid getGridEnd() {
        updateGridEnd();
        return grid_end;
    }
    public void launchSimulation() throws InterruptedException {
        System.out.println("Simulation en cours");

        for (Agent a : agents) {
            a.move();
        }

        while(!moveDone(agents)) {
            grid_start.display();
            GridController.instance().updateGrid(grid_start);

            Thread.sleep(1000);
        }

        grid_start.display();
        GridController.instance().updateGrid(grid_start);
    }

    private static boolean moveDone(List<Agent> agents) {
        for (Agent agent : agents) {
            if (!agent.isMoveDone()) {
                return false;
            }
        }

        return true;
    }


    public static Character nextChar(Character c) {
        int nextCharVal = 0;
        for (int i=0;i<Config.charList.size();i++) {
            if (Config.charList.get(i) == c) {
                nextCharVal = i + 1;
                break;
            }
        }
        nextCharVal = nextCharVal % Config.charList.size();

        return Config.charList.get(nextCharVal);
    }

    public void addPropertyChangeListener(PropertyChangeListener pcl) {
        this.support.addPropertyChangeListener(pcl);
    }
}
