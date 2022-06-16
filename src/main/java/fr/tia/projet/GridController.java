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

    public void updateGrid(Grid grid) {
        System.out.println("Firing the event to update the simulated grid");
        support.firePropertyChange("newGrid", null, grid);
    }

    public void addPropertyChangeListener(PropertyChangeListener pcl) {
        this.support.addPropertyChangeListener(pcl);
    }
}
