package fr.tia.projet.listener;

import fr.tia.projet.Simulation;
import fr.tia.projet.SwingView;

import java.awt.event.ActionListener;

public class EditAgentListener extends Listener implements ActionListener {
    private boolean isStart;
    private int row;
    private int col;
    private Character c;
    public EditAgentListener(SwingView view, boolean isStart, int row, int col, Character c) {
        super(view);
        this.isStart = isStart;
        this.row = row;
        this.col = col;
        this.c = c;
    }

    /**
     * Appelé lorsqu'on appuie sur un bouton sur la grille de début et de fin
     * Permet d'appeler la méthode de la classe Simulation qui ajoutera/supprimera un agent (soit pour la grille de début, soit pour celle de fin)
     * @param e
     */
    @Override
    public void actionPerformed(java.awt.event.ActionEvent e) {
        Character newC = '\0';
        if (this.c == null || this.c == '\0') {
            newC = Simulation.instance().getLastCharNotUsed(isStart);
        }
        if (isStart) {
            Simulation.instance().addAgentStart(newC, this.row, this.col);
        } else {
            Simulation.instance().addAgentEnd(this.c, newC, this.row, this.col);
        }
    }

}
