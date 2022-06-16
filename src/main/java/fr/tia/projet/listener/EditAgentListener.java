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

    @Override
    public void actionPerformed(java.awt.event.ActionEvent e) {
        Character newC = Simulation.nextChar(this.c);
        System.out.println("character : " + newC.toString());
        if (isStart) {
            Simulation.instance().addAgentStart(newC, this.row, this.col);
        } else {
            Simulation.instance().addAgentEnd(newC, this.row, this.col);
        }
        // SearchSkillsList.instance().addNewSkill(getView().getSkillToAdd());
    }

}
