package fr.tia.projet;

import fr.tia.projet.listener.EditAgentListener;
import fr.tia.projet.listener.LaunchSimulationListener;

import javax.swing.Box;
import javax.swing.BoxLayout;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.SwingUtilities;
import javax.swing.border.Border;
import javax.swing.border.CompoundBorder;
import javax.swing.border.EmptyBorder;
import javax.swing.border.LineBorder;
import java.awt.Color;
import java.awt.Component;
import java.awt.Container;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.Font;
import java.awt.GridLayout;
import java.awt.event.ActionListener;
import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.util.List;

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

        GridController.instance().addPropertyChangeListener(this);
        Simulation.instance().addPropertyChangeListener(this);

        SwingUtilities.invokeLater(new Runnable() {
            @Override
            public void run() {
                SwingView.this.display(width, height);
                // System.out.println("READY");
                createGridStartEnd(grid_start, true);
                createArrow();
                createGridStartEnd(grid_end, false);
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

        contentPane.add(Box.createRigidArea(new Dimension(0,10)));

        gridBox = createGridBox();
        contentPane.add(gridBox);

        contentPane.add(Box.createRigidArea(new Dimension(0,10)));

        gridCalcBox = createGridCalcBox();
        contentPane.add(gridCalcBox);

        contentPane.add(Box.createRigidArea(new Dimension(0,10)));

        bottomBox = createBottomBox();
        contentPane.add(bottomBox);

        contentPane.add(Box.createRigidArea(new Dimension(0,10)));

        frame.setVisible(true);
    }

    private JPanel createTopBox() {
        JPanel cTopBox = new JPanel();
        cTopBox.setLayout(new BoxLayout(cTopBox, BoxLayout.Y_AXIS));

        JPanel titlePanel = new JPanel();
        titlePanel.setLayout(new BoxLayout(titlePanel, BoxLayout.X_AXIS));

        JLabel title = new JLabel("INTERACTION MULTI-AGENTS");
        title.setHorizontalAlignment(JLabel.CENTER);
        title.setFont(new Font("Sans-Serif", Font.PLAIN, 35));
        title.setForeground(Color.BLUE);
        titlePanel.add(title);

        JPanel instructionsPanel = new JPanel();
        instructionsPanel.setLayout(new BoxLayout(instructionsPanel, BoxLayout.X_AXIS));

        JLabel instructions = new JLabel("Cliquez sur une des cases pour modifier son agent (A, B, C, D ou case vide)");
        instructions.setHorizontalAlignment(JLabel.CENTER);
        instructions.setFont(new Font("Sans-Serif", Font.ITALIC, 15));
        instructions.setForeground(Color.DARK_GRAY);
        instructionsPanel.add(instructions);

        cTopBox.add(titlePanel);
        cTopBox.add(instructionsPanel);

        return cTopBox;
    }

    private JPanel createGridBox() {
        JPanel cGridBox = new JPanel();
        cGridBox.setLayout(new BoxLayout(cGridBox, BoxLayout.LINE_AXIS));
        cGridBox.setMaximumSize(new Dimension(Integer.MAX_VALUE, 200));

        return cGridBox;
    }

    private JPanel createGridCalcBox() {

        JPanel cGridCalcBox = new JPanel();
        cGridCalcBox.setLayout(new BoxLayout(cGridCalcBox, BoxLayout.X_AXIS));

        return cGridCalcBox;
    }

    private JPanel createBottomBox() {
        JPanel cBottomBox = new JPanel();
        cBottomBox.setLayout(new BoxLayout(cBottomBox, BoxLayout.X_AXIS));

        JButton simulateBtn = new JButton("Simuler test");
        cBottomBox.add(simulateBtn);

        simulateBtn.setForeground(Color.BLACK);
        simulateBtn.setBackground(Color.CYAN);
        Border line = new LineBorder(Color.BLACK);
        Border margin = new EmptyBorder(5, 15, 5, 15);
        Border compound = new CompoundBorder(line, margin);

        ActionListener skillHandler = new LaunchSimulationListener(this);
        simulateBtn.addActionListener(skillHandler);
        return cBottomBox;
    }

    Color beige = new Color(255, 183, 168, 220);
    Color brown = new Color(124, 94, 57, 220);

    public JPanel createGridStartEnd(Grid grid, boolean isStart) {
        JPanel newGrid = new JPanel(new GridLayout(5, 5));

        List<Cell> cells = grid.getCells();
        for (Cell cell : cells) {
            JPanel elem = new JPanel();
            elem.setBackground((cell.getRow()+cell.getCol()) % 2 == 0 ? beige : brown);

            Character c;
            // synchronized (grid.agents)
            Agent a = grid.getAgent(cell);
            if (a == null)
            {
                c = '\0';
            } else {
                c = a.getC();
            }

            JButton btn = new JButton();

            btn.setFont(new Font("Serif", Font.BOLD, 18));
            btn.setForeground(Color.BLACK);
            btn.setBackground(Config.colorList.get(c));
            Border line = new LineBorder(Color.BLACK);
            Border margin = new EmptyBorder(5, 15, 5, 15);
            Border compound = new CompoundBorder(line, margin);

            elem.add(btn);

            btn.setText(c.toString());

            ActionListener agentHandler = new EditAgentListener(this, isStart, cell.getRow(), cell.getCol(), c);
            btn.addActionListener(agentHandler);

            newGrid.add(elem);
        }

        // System.out.println("GRID BOX : " + gridBox);
        gridBox.add(newGrid, isStart ? 0 : 2);

        return newGrid;
    }

    public JPanel createGridCalc(Grid grid) {
        JPanel newGrid = new JPanel(new GridLayout(5, 5));

        List<Cell> cells = grid.getCells();
        for (Cell cell : cells) {
            JPanel elem = new JPanel();
            elem.setBackground((cell.getRow()+cell.getCol()) % 2 == 0 ? beige : brown);
            JLabel txt = new JLabel();
            txt.setFont(new Font("Serif", Font.BOLD, 18));
            txt.setForeground(Color.WHITE);
            elem.add(txt);

            Character c;
            // synchronized (grid.agents)
            Agent a = grid.getAgent(cell);
            if (a == null)
            {
                c = '\0';
            } else {
                c = a.getC();
            }
            txt.setText(c.toString());
            newGrid.add(elem);
        }

        gridCalcBox.add(newGrid);

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

    private void revalipaint(Component component) {
        component.revalidate();
        component.repaint();
    }

    @Override
    public void propertyChange(PropertyChangeEvent evt) {
        SwingUtilities.invokeLater(new Runnable() {
            public void run() {
                if (evt.getPropertyName().equals("newGrid")) {
                    System.out.println("[Swing] Simulation - updated grid");
                    Grid newGrid = (Grid) evt.getNewValue();
                    gridCalcBox.removeAll();
                    newGrid.display();
                    createGridCalc(newGrid);
                    gridCalcBox.revalidate();
                } else if (evt.getPropertyName().equals("gridStartUpdate")) {
                    System.out.println("[Swing] Simulation - start grid updated");
                    Grid newGridStart = (Grid) evt.getNewValue();
                    newGridStart.display();

                    gridBox.remove(0); // TODO: tester
                    createGridStartEnd(newGridStart, true);
                    gridBox.revalidate();
                } else if (evt.getPropertyName().equals("gridEndUpdate")) {
                    System.out.println("[Swing] Simulation - end grid updated");
                    Grid newGridEnd = (Grid) evt.getNewValue();
                    newGridEnd.display();

                    gridBox.remove(2); // TODO: tester
                    createGridStartEnd(newGridEnd, false);
                    gridBox.revalidate();
                }
            }
        });
    }
}
