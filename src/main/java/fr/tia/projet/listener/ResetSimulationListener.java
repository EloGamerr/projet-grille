package fr.tia.projet.listener;

import fr.tia.projet.Simulation;
import fr.tia.projet.SwingView;

import java.awt.event.ActionListener;

public class ResetSimulationListener extends Listener implements ActionListener {
    public ResetSimulationListener(SwingView view) {
        super(view);
    }

    /**
     * Appelé lors de l'appui du bouton pour remettre les grilles dans un état intéressant que l'on propose dans le code
     * @param e
     */
    @Override
    public void actionPerformed(java.awt.event.ActionEvent e) {
        Simulation.instance().resetToDefault();
    }
}
