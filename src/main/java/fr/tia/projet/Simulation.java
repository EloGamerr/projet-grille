package fr.tia.projet;

import java.beans.PropertyChangeListener;
import java.beans.PropertyChangeSupport;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

public class Simulation {
    private static Simulation simulation;
    private final PropertyChangeSupport support;
    private boolean simulationProgress;
    private Grid grid_start;
    private Grid grid_end;
    private List<Agent> agents;
    private Set<Character> charStart;
    private Set<Character> charEnd;

    private Simulation() {
        support = new PropertyChangeSupport(this);
        grid_start = new Grid(Config.GRID_SIZE, Config.GRID_SIZE);
        grid_end = new Grid(Config.GRID_SIZE, Config.GRID_SIZE);
        agents = new ArrayList<>();
        charStart = new HashSet<>();
        charEnd = new HashSet<>();
        simulationProgress = false;
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

    /**
     * Méthode remettant les grilles à leur état par défaut (appelée par un bouton "Réinitialiser")4
     * Cette méthode va notifier la vue avec les nouvelles grilles
     */
    public void resetToDefault() {
        if (simulationProgress) {
            if (Config.DEBUG)
                System.out.println("Impossible de réinitialiser alors que la simulation est en cours");
            return;
        }
        grid_start = new Grid(Config.GRID_SIZE, Config.GRID_SIZE);
        grid_end = new Grid(Config.GRID_SIZE, Config.GRID_SIZE);
        agents = new ArrayList<>();
        charStart = new HashSet<>();
        charEnd = new HashSet<>();
        defaultGrid();

        // On envoie à la vue les nouvelles informations
        updateGridEnd();

        support.firePropertyChange("gridStartUpdate", null, grid_start);
        support.firePropertyChange("gridEndUpdate", null, grid_end);
    }

    /**
     * Génération d'une grille par défaut dans l'intérêt de ne pas avoir des grilles "vides" et d'avoir une grille intéressante
     * dès qu'on lance l'exécutable
     */
    public void defaultGrid() {
        Agent agentA = new Agent('A', grid_start, new Cell(0, 4));
        // Théoriquement, end_case peut être nul: mais dans le cadre de l'uniformisation avec le design Swing
        // On renseigne cette case. Si l'agent ne doit pas bouger, son end_case = sa position dans la grille
        Agent agentB = new Agent('B', grid_start, new Cell(0, 3));
        Agent agentC = new Agent('C', grid_start, new Cell(1, 1));
        Agent agentD = new Agent('D', grid_start, new Cell(2, 1));
        Agent agentE = new Agent('E', grid_start, new Cell(3, 1));
        Agent agentF = new Agent('F', grid_start, new Cell(4, 1));

        agents.add(agentA);
        agents.add(agentB);
        agents.add(agentC);
        agents.add(agentD);
        agents.add(agentE);
        agents.add(agentF);

        grid_start.setAgent(0, 0, agentA); // croix-encerclée
        grid_start.setAgent(0, 1, agentB); // soleil
        grid_start.setAgent(1, 1, agentC); // sablier
        grid_start.setAgent(2, 1, agentD); // étoile
        grid_start.setAgent(3, 1, agentE); // étoile
        grid_start.setAgent(4, 1, agentF); // étoile

        charStart.add('A');
        charStart.add('B');
        charStart.add('C');
        charStart.add('D');
        charStart.add('E');
        charStart.add('F');

        charEnd.add('A');
        charEnd.add('B');
        charEnd.add('C');
        charEnd.add('D');
        charEnd.add('E');
        charEnd.add('F');

    }

    /**
     * Ajoute un agent sur la grille de début
     * @param c le caractère qui va être ajouté ('\0' pour libérer une case ou sinon un caractère c)
     * @param rowStart la ligne de la grille de début sur laquelle on interagit
     * @param colStart la colonne de la grille de début sur laquelle on interagit
     */
    public void addAgentStart(Character c, int rowStart, int colStart) {
        boolean updateGridEnd = false;
        if (c == '\0') {
            // Suppression de l'agent qui était positionné en rowStart;colStart
            Agent agentToRemove = grid_start.getAgent(grid_start.getCell(rowStart, colStart));
            agents.remove(agentToRemove);
            Character cToRemove = agentToRemove.getC();
            charStart.remove(cToRemove);
            if (charEnd.contains(cToRemove)) {
                charEnd.remove(cToRemove);
                updateGridEnd = true;
            }
            grid_start.removeAgent(rowStart, colStart);
        } else {
            // Création d'un agent
            Agent agent = new Agent(c, grid_start, null);
            grid_start.setAgent(rowStart, colStart, agent); // croix-encerclée
            agents.add(agent);
            charStart.add(c);
        }

        // On appelle la vue avec la grille de début pour la notifier du changement
        support.firePropertyChange("gridStartUpdate", null, grid_start);
        // Si la suppression d'un agent de gauche a entraîné la suppression de celle de droite, alors on met également à jour la grille de fin sur la vue
        if (updateGridEnd) {
            updateGridEnd();
            support.firePropertyChange("gridEndUpdate", null, grid_end);
        }
    }

    /**
     * Ajoute un agent sur la grille de fin
     * @param origC le caractère à l'origine sur la case ('\0' si la case était vide, ou alors le caractère)
     * @param newC le nouveau caractère (soit un caractère si la case était '\0', soit '\0' s'il y avait un caractère)
     * @param rowEnd la ligne de la grille de fin sur laquelle on interagit
     * @param colEnd la colonne de la grille de fin sur laquelle on interagit
     */
    public void addAgentEnd(Character origC, Character newC, int rowEnd, int colEnd) {
        // Si la case est vide, alors on va rechercher l'agent sur le nouveau contenu (qui est donc un caractère != '\0')
        if (origC == null || origC == '\0') { origC = newC; }
        Agent agent = null; // setEndCase
        for (Agent a : agents) {
            if (a.getC() == origC) {
                agent = a;
                break;
            }
        }

        // Si un agent est null, alors on a essayé de placer un agent sur une grille de fin sans qu'elle y soit au début - problème!
        if (agent == null) {
            if (Config.DEBUG)
                System.out.println("Tentative de placer un agent " + newC + " sur la grille de fin alors qu'il n y en a pas au début");
            return;
        }

        // Si le nouveau caractère est \0, alors on va supprimer le end case (cas de fin) de l'agent sur lequel on pointait
        // Sinon, on définit le nouveau end case
        if (newC == '\0') {
            agent.setEndCase(null);
            charEnd.remove(origC);
        } else {
            agent.setEndCase(new Cell(rowEnd, colEnd));
            charEnd.add(newC);
        }

        // On met à jour la grille de fin (non trivial)
        updateGridEnd();
        // On appelle la vue avec la grille de fin pour la notifier du changement
        support.firePropertyChange("gridEndUpdate", null, grid_end);
    }

    /**
     * Met à jour la grille de fin (basée sur les positions end_case des agents)
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

    /**
     * Permet d'obtenir la grille de début
     * @return la grille de début
     */
    public Grid getGridStart() {
        return grid_start;
    }

    /**
     * Permet d'obtenir la grille de fin
     * Nécessite d'appeler la méthode updateGridEnd() (car la grille dépend du end_case de chaque agent)
     * @return la grille de fin
     */
    public Grid getGridEnd() {
        updateGridEnd();
        return grid_end;
    }


    /**
     * Méthode appelée pour lancer la simulation
     * Cette méthode vérifiera si la simulation est valide (si elle ne l'est pas, elle notifiera la vue qu'il y a un problème)
     * puis fera en sorte de lancer tous les mouvements des agents, en laissant un délai (Thread.sleep) pour le visuel de Swing
     * L'ensemble des variables se réinitialiseront lorsque la simulation sera terminée
     * @throws InterruptedException
     */
    public void launchSimulation() throws InterruptedException {
        for (Agent a : agents) {
            if (a.getEndCase() == null) {
                support.firePropertyChange("wrongGrids", null, null);
                if (Config.DEBUG)
                    System.out.println("Erreur simulation (mauvaises entrées pour les grilles): STOP");
                return;
            }
        }

        simulationProgress = true;
        if (Config.DEBUG)
            System.out.println("Simulation en cours");
        GridController.instance().freezeGridButtons(true);
        Grid grid_at_start = grid_start.clone();

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

        if (Config.DEBUG)
            System.out.println("Fin de la simulation");
        GridController.instance().freezeGridButtons(false);
        simulationProgress = false;
        grid_start = grid_at_start;
    }

    /**
     * Retourne si tous les agents ont terminé d'effectuer leur mouvement (donc que la simulation est terminée)
     * @param agents la liste des agents pour laquelle on vérifie si ils sont en mouvement
     * @return si tous les agents ont terminé leur mouvement
     */
    private static boolean moveDone(List<Agent> agents) {
        for (Agent agent : agents) {
            if (!agent.isMoveDone()) {
                return false;
            }
        }

        return true;
    }

    /**
     * Permet d'obtenir le dernier caractère qui n'est pas utilisé dans la grille (soit de début soit de fin)
     * @param isStart pemet de définir si on regarde le dernier caractère parmi les agents de la grille de début ou celle de fin (isStart = true on regarde la grille de début / false on regarde la grille de fin)
     * @return
     */
    public Character getLastCharNotUsed(boolean isStart) {
        Character c = '\0';
        for (Character oneC : Config.charList) {
            boolean alreadyUsed = isStart ? charStart.contains(oneC) : charEnd.contains(oneC);
            if (!alreadyUsed) {
                c = oneC;
                break;
            }
        }
        return c;
    }

    public void addPropertyChangeListener(PropertyChangeListener pcl) {
        this.support.addPropertyChangeListener(pcl);
    }
}
