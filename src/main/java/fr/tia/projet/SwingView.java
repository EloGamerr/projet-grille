package fr.tia.projet;

import javax.swing.BoxLayout;
import javax.swing.JButton;
import javax.swing.JCheckBox;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTextField;
import javax.swing.SwingUtilities;
import java.awt.Color;
import java.awt.Component;
import java.awt.Container;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.Font;
import java.awt.GridLayout;
import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class SwingView implements PropertyChangeListener {
    private JFrame frame;
    private JPanel topBox;
    private JPanel gridBox;
    private JPanel gridCalcBox;
    private JPanel bottomBox;
    private boolean ready = false;

    /**
     * Creates swing view and display it.
     * @param width .
     * @param height .
     */
    public SwingView(int width, int height, Grid grid_start, Grid grid_end) {
        super();

        SwingUtilities.invokeLater(new Runnable() {
            @Override
            public void run() {
                SwingView.this.display(width, height);
                // System.out.println("READY");
                createGrid(grid_start);
                createArrow();
                createGrid(grid_end);
                ready = true;
            }
        });
    }

    private void display(int width, int height) {
        frame = new JFrame("TIA - Projet - Grille");
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setSize(width, height);

        Container contentPane = frame.getContentPane();

        contentPane.setLayout(new BoxLayout(contentPane, BoxLayout.Y_AXIS));

        topBox = createTopBox();
        contentPane.add(topBox);

        gridBox = createGridBox();
        contentPane.add(gridBox);

        gridCalcBox = createGridCalcBox();
        contentPane.add(gridCalcBox);

        bottomBox = createBottomBox();
        contentPane.add(bottomBox);

        frame.setVisible(true);
    }

    private JPanel createTopBox() {
        JPanel cTopBox = new JPanel(new FlowLayout(0, 10, 10));
        JLabel label = new JLabel("Les grilles de début et de fin sont dans le code");
        cTopBox.add(label);

        return cTopBox;
    }

    private JPanel createGridBox() {
        JPanel cGridBox = new JPanel(new FlowLayout(0, 10, 10));

        return cGridBox;
    }

    private JPanel createGridCalcBox() {
        JPanel cGridCalcBox = new JPanel(new FlowLayout(0, 10, 10));

        return cGridCalcBox;
    }

    private JPanel createBottomBox() {
        JPanel cBottomBox = new JPanel(new FlowLayout(0, 10, 10));

        JButton testButton = new JButton("Simuler test");
        cBottomBox.add(testButton);

        /*ActionListener skillHandler = new SwingNewSkillEventListener(this);
        submitButton.addActionListener(skillHandler);
        skilltextField.addActionListener(skillHandler);
        ActionListener includeExperience = new SwingIncludeExperienceListener(this);
        takeExperiencesSettings.addActionListener(includeExperience);

         */
        return cBottomBox;
    }

    public JPanel createGrid(Grid grid) {
        JPanel newGrid = new JPanel(new GridLayout(5, 5));

        ArrayList<Cell> cells = grid.getCells();
        for (Cell cell : cells) {
            JPanel elem = new JPanel();
            JLabel txt = new JLabel();
            txt.setFont(new Font("Serif", Font.PLAIN, 18));
            elem.add(txt);

            Character c;
            Agent a = grid.getAgent(cell);
            if (a == null)
            {
                c = 'O';
            } else {
                c = a.getC();
            }
            txt.setText(c.toString());
            newGrid.add(elem);
        }

        // System.out.println("GRID BOX : " + gridBox);

        gridBox.add(newGrid);

        return newGrid;
    }

    public JPanel createArrow() {
        JPanel newArrow = new JPanel(new FlowLayout());
        JLabel arrowLabel = new JLabel();
        newArrow.add(arrowLabel);
        arrowLabel.setText("->");
        arrowLabel.setForeground(Color.RED);
        arrowLabel.setFont(new Font("Serif", Font.PLAIN, 28));

        gridBox.add(newArrow);

        return newArrow;
    }

    /*
    @Override
    public void propertyChange(PropertyChangeEvent evt) {
        SwingUtilities.invokeLater(new Runnable() {
            public void run() {
                if (evt.getPropertyName().equals("updateResultsList")) {
                    List<ResultItem> list = (List<ResultItem>) evt.getNewValue();
                    updateResultsList(list);
                } else if (evt.getPropertyName().equals("addNewSkill")) {
                    String skillName = (String) evt.getNewValue();
                    addNewSkill(skillName);
                } else if (evt.getPropertyName().equals("removeSkill")) {
                    String skillName = (String) evt.getNewValue();
                    removeSkill(skillName);
                } else if (evt.getPropertyName().equals("removeSkills")) {
                    searchSkillsBox.removeAll();
                    revalipaint(searchSkillsBox);
                } else if (evt.getPropertyName().equals("setIncludeExperience")) {
                    takeExperiencesSettings.setSelected((Boolean) evt.getNewValue());
                } else if (evt.getPropertyName().equals("setStrategySelection")) {
                    strategyComboBox.setSelectedItem(evt.getNewValue());
                } else if (evt.getPropertyName().equals("clearResultsList")) {
                    resultBox.removeAll();
                }
            }
        });
    }
     */

    private void revalipaint(Component component) {
        component.revalidate();
        component.repaint();
    }

    @Override
    public void propertyChange(PropertyChangeEvent propertyChangeEvent) {

    }
}
