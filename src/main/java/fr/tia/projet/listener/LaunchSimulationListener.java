package fr.tia.projet.listener;

import fr.tia.projet.Simulation;
import fr.tia.projet.SwingView;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class LaunchSimulationListener extends Listener implements ActionListener {
    public LaunchSimulationListener(SwingView view) {
        super(view);
    }

    @Override
    public void actionPerformed(ActionEvent actionEvent) {
        new Thread() {
            public void run() {
                try {
                    Simulation.instance().launchSimulation();
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        }.start();
    }
}
