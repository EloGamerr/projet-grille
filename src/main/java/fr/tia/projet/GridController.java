package fr.tia.projet;

import java.beans.PropertyChangeListener;
import java.beans.PropertyChangeSupport;

public class GridController {

    private static GridController gridController;

    private final PropertyChangeSupport support;

    /**
     * Make constructor private.
     */
    private GridController() {
        support = new PropertyChangeSupport(this);
    }

    /**
     * @return singleton.
     */
    public static GridController instance() {
        if (gridController == null) {
            gridController = new GridController();
        }

        return gridController;
    }

    /**
     * Méthode qui appelle la vue Swing afin de figer les boutons
     * Appelée lors du début de la simulation avec freezing = true, puis lorsque la simulation est terminée avec freezing = false.
     * @param freezing
     */
    public void freezeGridButtons(boolean freezing) {
        if (Config.DEBUG)
            System.out.println("Freezing the grid buttons: " + freezing);

        support.firePropertyChange("freezeGrids", null, freezing);
    }

    /**
     * Méthode qui appelle la vue Swing afin d'envoyer la nouvelle grille de la simulation
     * Appelé lors d'une simulation de façon régulière tant que les agents sont en mouvement
     * @param grid
     */
    public void updateGrid(Grid grid) {
        if (Config.DEBUG)
            System.out.println("Firing the event to update the simulated grid");
        support.firePropertyChange("newGrid", null, grid);
    }

    public void addPropertyChangeListener(PropertyChangeListener pcl) {
        this.support.addPropertyChangeListener(pcl);
    }
}
