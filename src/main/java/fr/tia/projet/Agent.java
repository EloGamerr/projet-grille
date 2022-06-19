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
    public Set<Cell> blockedCells;

    public Agent(Character c, Grid grid, Cell end_case) {
        this.c = c;
        this.grid = grid;
        this.grid_clone = grid.clone();
        this.end_case = end_case;
        this.messagesHistoric = new HashSet<>();
        this.blockedCells = new HashSet<>();
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

    /**
     * Méthode qui déplace l'agent
     */
    public void move() {
        this.isAlive = true;

        // Si aucune case de fin n'a été mise, alors la case de fin est la case de début (c'est important car il
        // est possible que l'agent bouge suite à un message d'un autre agent, même s'il était déjà sur sa case de fin)
        if (end_case == null) {
            end_case = grid.getCellFromAgent(this);
        }

        new Thread(() -> {
            // Tant que l'agent est en vie (i.e. tant que la grille n'est pas celle finale)
            while (isAlive) {
                // On cherche un chemin seulement si l'agent n'est pas immobilisé
                // Un agent est immobilisé lorsqu'il s'est déplacé suite à un message d'un autre agent
                if (!stunned) {
                    // Inutile de recharher un chemin si on a déjà atteint la case finale
                    if (!grid.getCellFromAgent(this).equals(end_case)) {
                        // On copy les agents dans la grille clonée
                        grid.copyAgent(grid_clone);
                        // On cherche un chemin en considérant que chaque agent est un obstacle
                        ArrayList<Cell> path = search_path(grid_clone.toGridSearch(this, new HashSet<>()), end_case);
                        // Si on a trouvé un chemin
                        if (path.size() >= 2) {
                            Cell fromCell = path.get(path.size()-1);
                            Cell toCell = path.get(path.size()-2);

                            // On déplace l'agent. Si un agent est renvoyé, alors le déplacement n'a pas pu avoir lieu
                            // car un agent occupe déjà la case. Il faut regarder la méthode "search_path" pour
                            // comprendre pourquoi ce cas est possible.
                            Agent agent = grid.moveAgent(fromCell.getRow(), fromCell.getCol(), toCell.getRow(), toCell.getCol());

                            if (agent != null) {
                                // Ici, on va demander à l'agent qui nous bloque de se déplacer
                                List<Message> messages = Main.messages.getOrDefault(agent, new ArrayList<>());

                                // On synchronise la liste des messages car elle est utilisée par plusieurs threads
                                synchronized (messages) {
                                    Message message = new Message(this, fromCell, toCell);
                                    // Si c'est la deuxième fois qu'on envoie le même message, alors on
                                    // considère qu'on ne peut plus se déplacer vers la cellule pour éviter les cycles
                                    if (messagesHistoric.contains(message)) {
                                        blockedCells.add(toCell);
                                    }
                                    messages.add(message);
                                    Main.messages.put(agent, messages);
                                    messagesHistoric.add(message);
                                }
                            } else {
                                // On peut clear les messages car on vient de se déplacer
                                Main.messages.getOrDefault(this, new ArrayList<>()).clear();
                            }
                        }
                    }
                    List<Message> messages = Main.messages.getOrDefault(this, new ArrayList<>());
                    // On synchronise la liste des messages car elle est utilisée par plusieurs threads
                    synchronized (messages) {
                        // Lecture des messages reçus
                        for (Message message : messages) {
                            // On vérifie que l'agent se trouve toujours sur la cellule qui doit être libérée
                            if (message.getTo().equals(grid.getCellFromAgent(this))) {
                                // On boucle sur les 4 cellules adjacentes
                                for (int i = -1; i <= 1; ++i) {
                                    for (int j = -1; j <= 1; ++j) {
                                        if (i != 0 && j != 0)
                                            continue;

                                        if (i == 0 && j == 0)
                                            continue;

                                        // On ne se déplace pas sur la case où il y a déjà l'agent qui envoie le message
                                        if (message.getFrom().getRow() == message.getTo().getRow()+i && message.getFrom().getCol() == message.getTo().getCol()+j)
                                            continue;

                                        // On vérifie qu'on peut se déplacer vers la destination (est-ce que la cellule
                                        // se trouve dans la grille ?)
                                        if (!grid.canMove(message.getTo().getRow()+i, message.getTo().getCol()+j))
                                            continue;

                                        // Déplacement de l'agent
                                        Agent agent = grid.moveAgent(message.getTo().getRow(), message.getTo().getCol(), message.getTo().getRow()+i, message.getTo().getCol()+j);

                                        // Le déplacement s'est bien passé
                                        if (agent == null) {
                                            // On termine les 2 boucles
                                            i = 2;
                                            j = 2;
                                            // On met l'agent dans l'état "stunned" (le prochain tick de déplacement
                                            // sera ignoré)
                                            stunned = true;
                                        }
                                    }
                                }
                            }
                        }
                        // Les messages ont été lus, on peut les clear
                        messages.clear();
                    }
                } else {
                    // On sort de l'état "stunned"
                    stunned = false;
                }

                try {
                    // Un tick correspond à 2 secondes (cette valeur est arbitraire mais permet de bien comprendre
                    // comment se déroule la simulation quand on la lance)
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

    /**
     * Fonction permettant de rechercher un chemin en utilisant l'algorithme de Dijkstra
     * Cette méthode va calculer 3 fois au maximum un chemin
     * Une première fois en considérant tous les obstacles possibles
     * Si on n'a pas trouvé de chemin :
     * Une deuxième fois en bloquant certaines cellules mais en considérant qu'on peut aller sur toutes les autres cases
     * (même si elles sont occupés par un agent)
     * Si on a toujours pas trouvé de chemin :
     * Une troisième fois en considérant qu'on peut se déplacer sur n'importe quelle case
     * @return Le chemin vers la case finale
     */
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
                // Il n'y a plus de minimum, on peut arrêter la boucle
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
        // Création du chemin
        while (s != start_case) {
            path.add(s);
            s = pred.get(s);
            // Aucun chemin possible vers la destination
            if (s == null) {
                // Si c'est le premier appel à la méthode "search_path"
                if (gridSearch.getAgent() != null) {
                    // On recalcule avec Dijkstra mais en considérant
                    // que certaines cellules ne sont plus valides et en considérant que les autres agents ne sont
                    // plus des obstacles
                    return search_path(grid_clone.toGridSearch(null, blockedCells), end_case);
                } else {
                    // S'il y avait des cellules non valides
                    if (!gridSearch.getBlockedCells().isEmpty()) {
                        // On recalcule avec Dijkstra mais en considérant
                        // qu'il n'y a plus aucun obstacle
                        return search_path(grid_clone.toGridSearch(null, new HashSet<>()), end_case);
                    } else {
                        // Aucun chemin trouvé
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

