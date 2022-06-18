package fr.tia.projet;

import java.util.*;

public class Agent {
    private final Character c;
    private final Grid grid;
    private final Grid grid_clone;
    private Cell end_case; // on enlève final
    private boolean isAlive;
    private boolean stunned;
    public Set<Message> messagesHistoric;
    public Set<Agent> agents;

    public Agent(Character c, Grid grid, Cell end_case) {
        this.c = c;
        this.grid = grid;
        this.grid_clone = grid.clone();
        this.end_case = end_case;
        this.messagesHistoric = new HashSet<>();
        this.agents = new HashSet<>();
    }

    public void setAlive(boolean alive) {
        isAlive = alive;
    }

    public Character getC() {
        return c;
    }

    public Cell getEndCase() {
        return end_case;
    }

    public void setEndCase(Cell cell) {
        end_case = cell;
    }

    public void move() {
        this.isAlive = true;

        if (end_case == null) {
            end_case = grid.getCellFromAgent(this);
        }

        new Thread(() -> {
            while (isAlive) {
                if (!stunned) {
                    if (!grid.getCellFromAgent(this).equals(end_case)) {
                        grid.copyAgent(grid_clone);
                        ArrayList<Cell> path = search_path(grid_clone.toGridSearch(this, new HashSet<>()), end_case);
                        if (path.size() >= 2) {
                            Cell fromCell = path.get(path.size()-1);
                            Cell toCell = path.get(path.size()-2);

                            Agent agent = grid.moveAgent(fromCell.getRow(), fromCell.getCol(), toCell.getRow(), toCell.getCol());

                            if (agent != null) {
                                List<Message> messages = Main.messages.getOrDefault(agent, new ArrayList<>());

                                synchronized (messages) {
                                    Message message = new Message(this, fromCell, toCell);
                                    if (messagesHistoric.contains(message)) {
                                        agents.add(agent);
                                    }
                                    messages.add(message);
                                    Main.messages.put(agent, messages);
                                    messagesHistoric.add(message);

                                }
                            } else {
                                Main.messages.getOrDefault(this, new ArrayList<>()).clear();
                            }
                        }
                    }
                    for (Message message : Main.messages.getOrDefault(this, new ArrayList<>())) {
                        if (message.getTo().equals(grid.getCellFromAgent(this))) {
                            for (int i = -1; i <= 1; ++i) {
                                for (int j = -1; j <= 1; ++j) {
                                    if (i != 0 && j != 0)
                                        continue;

                                    if (i == 0 && j == 0)
                                        continue;

                                    if (message.getFrom().getRow() == message.getTo().getRow()+i && message.getFrom().getCol() == message.getTo().getCol()+j)
                                        continue;

                                    if (!grid.canMove(message.getTo().getRow()+i, message.getTo().getCol()+j))
                                        continue;

                                    Agent agent = grid.moveAgent(message.getTo().getRow(), message.getTo().getCol(), message.getTo().getRow()+i, message.getTo().getCol()+j);

                                    if (agent == null) {
                                        i = 2;
                                        j = 2;
                                        stunned = true;
                                    }
                                }
                            }
                        }
                    }
                    Main.messages.getOrDefault(this, new ArrayList<>()).clear();
                } else {
                    stunned = false;
                }

                try {
                    Thread.sleep(2000);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        }).start();
    }

    public boolean isMoveDone() {
        return end_case == null || grid.getCellFromAgent(this).equals(end_case);
    }

    private ArrayList<Cell> search_path(GridSearch gridSearch, Cell end_case) {
        Cell start_case = gridSearch.getGrid().getCellFromAgent(this);

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

            if (s1 == null) {
                break;
            }

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
            // Aucun chemin possible vers la destination
            if (s == null) {
                if (gridSearch.getAgent() != null) {
                    // On recalcule avec djikstra mais en considérant
                    // que les autres agents ne sont pas des obstacles
                    return search_path(grid_clone.toGridSearch(null, agents), end_case);
                } else {
                    if (!gridSearch.getBlockedAgents().isEmpty()) {
                        return search_path(grid_clone.toGridSearch(null, new HashSet<>()), end_case);
                    } else {
                        return new ArrayList<>();
                    }
                }

            }
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

    @Override
    public String toString() {
        return c.toString();
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Agent agent = (Agent) o;
        return Objects.equals(c, agent.c);
    }

    @Override
    public int hashCode() {
        return Objects.hash(c);
    }
}

