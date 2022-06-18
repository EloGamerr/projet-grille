package fr.tia.projet;

import fr.tia.projet.listener.EditAgentListener;
import fr.tia.projet.listener.LaunchSimulationListener;
import fr.tia.projet.listener.ResetSimulationListener;

import javax.imageio.ImageIO;
import javax.swing.Box;
import javax.swing.BoxLayout;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.SwingUtilities;
import javax.swing.border.Border;
import javax.swing.border.CompoundBorder;
import javax.swing.border.EmptyBorder;
import javax.swing.border.LineBorder;
import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Component;
import java.awt.Container;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.Font;
import java.awt.GridLayout;
import java.awt.Image;
import java.awt.event.ActionListener;
import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;

public class SwingView implements PropertyChangeListener {
    private JFrame frame;
    private JPanel topBox;
    private JPanel gridBox;
    private JPanel gridCalcBox;
    private JPanel bottomBox;
    private JPanel creditBox;
    private JPanel errorBox;
    private boolean ready = false;
    private boolean buttonsFreezing = false;
    private List<JButton> gridCalcButtons;

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
                gridCalcButtons = new ArrayList<>();
                createGridStartEnd(grid_start, true);
                createArrow();
                createGridStartEnd(grid_end, false);
                ready = true;
            }
        });
    }

    /**
     * Méthode qui créé les bons éléments à mettre dans le design
     * @param width largeur de la fenêtre
     * @param height hauteur de la fenêtre
     */
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

        errorBox = createErrorBox();
        contentPane.add(errorBox);
        errorBox.setVisible(false);

        contentPane.add(Box.createRigidArea(new Dimension(0,10)));

        creditBox = createCreditBox();
        contentPane.add(creditBox);

        contentPane.add(Box.createRigidArea(new Dimension(0,10)));

        frame.setVisible(true);
    }

    /**
     * Création de la "top box": le titre + les instructions globales
     * @return le JPanel qui correspond à une top box
     */
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

        JPanel instructionsPanel1 = new JPanel();
        instructionsPanel1.setLayout(new BoxLayout(instructionsPanel1, BoxLayout.X_AXIS));

        JLabel instructions1 = new JLabel("Cliquez sur une des cases pour mettre ou enlever un agent");
        instructions1.setHorizontalAlignment(JLabel.CENTER);
        instructions1.setFont(new Font("Sans-Serif", Font.ITALIC, 15));
        instructions1.setForeground(Color.DARK_GRAY);
        instructionsPanel1.add(instructions1);

        JPanel instructionsPanel2 = new JPanel();
        instructionsPanel2.setLayout(new BoxLayout(instructionsPanel2, BoxLayout.X_AXIS));

        JLabel instructions2 = new JLabel("Les m\u00eames agents doivent \u00eatre pr\u00e9sents sur la grille de d\u00e9part et la grille d'arriv\u00e9e");
        instructions2.setHorizontalAlignment(JLabel.CENTER);
        instructions2.setFont(new Font("Sans-Serif", Font.ITALIC, 13));
        instructions2.setForeground(Color.DARK_GRAY);
        instructionsPanel2.add(instructions2);

        cTopBox.add(titlePanel);
        cTopBox.add(instructionsPanel1);
        cTopBox.add(instructionsPanel2);

        return cTopBox;
    }

    /**
     * Création de la "grid box": un JPanel contenant les grilles de début et de fin
     * @return la grid box
     */
    private JPanel createGridBox() {
        JPanel cGridBox = new JPanel();
        cGridBox.setLayout(new BoxLayout(cGridBox, BoxLayout.LINE_AXIS));
        cGridBox.setMaximumSize(new Dimension(Integer.MAX_VALUE, 200));
        cGridBox.setBorder(new EmptyBorder(5, 5, 5, 5));

        return cGridBox;
    }

    /**
     * Création de la "grid calc box": un JPanel contenant la grille de simulation
     * @return la grid calc box
     */
    private JPanel createGridCalcBox() {

        JPanel cGridCalcBox = new JPanel();
        cGridCalcBox.setLayout(new BoxLayout(cGridCalcBox, BoxLayout.X_AXIS));

        return cGridCalcBox;
    }

    /**
     * Création de la "bottom box": un JPanel contenant les deux boutons "Faire la simulation" et "Réinitialiser"
     * @return la bottom box
     */
    private JPanel createBottomBox() {
        JPanel cBottomBox = new JPanel();
        cBottomBox.setLayout(new BoxLayout(cBottomBox, BoxLayout.X_AXIS));

        JButton simulateBtn = new JButton("Faire la simulation");
        simulateBtn.setForeground(Color.BLACK);
        simulateBtn.setBackground(Color.CYAN);
        cBottomBox.add(simulateBtn);

        cBottomBox.add(Box.createRigidArea(new Dimension(5, 0)));

        JButton resetBtn = new JButton("R\u00e9initialiser");
        resetBtn.setForeground(Color.BLACK);
        resetBtn.setBackground(Color.RED);
        cBottomBox.add(resetBtn);

        ActionListener launchSimulationHandler = new LaunchSimulationListener(this);
        simulateBtn.addActionListener(launchSimulationHandler);

        ActionListener resetHandler = new ResetSimulationListener(this);
        resetBtn.addActionListener(resetHandler);

        return cBottomBox;
    }

    /**
     * Création de l'error box: un JPanel contenant l'explication d'une erreur
     * @return l'error box
     */
    private JPanel createErrorBox() {
        JPanel cErrorBox = new JPanel();
        cErrorBox.setLayout(new BoxLayout(cErrorBox, BoxLayout.X_AXIS));

        JLabel title = new JLabel("Vous devez placer les m\u00eames agents (et le m\u00eame nombre) sur les grilles de d\u00e9but et de fin");
        title.setHorizontalAlignment(JLabel.CENTER);
        title.setFont(new Font("Sans-Serif", Font.PLAIN, 12));
        title.setForeground(Color.RED);
        cErrorBox.add(title);

        return cErrorBox;
    }



    /**
     * Création des grilles de début et de fin qui peuvent être renseignés par l'utilisateur
     * @param grid la grille du contrôleur sur laquelle on se base pour créer la vue en l'occurence
     * @param isStart booléen définissant s'il faut recréer les grilles de début ou de fin (isStart à true = on recréée la grille de début / false = on recréée la grille de fin)
     * @return
     */
    private JPanel createGridStartEnd(Grid grid, boolean isStart) {
        JPanel newGrid = new JPanel(new GridLayout(5, 5, 5, 5));
        newGrid.setBorder(new EmptyBorder(5, 5, 5, 5));
        newGrid.setBackground(Config.beige);

        List<Cell> cells = grid.getCells();
        for (Cell cell : cells) {
            JPanel elem = new JPanel(new BorderLayout());
            elem.setBackground((cell.getRow()+cell.getCol()) % 2 == 0 ? Config.beige : Config.brown);

            Character c;
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

            gridCalcButtons.add(btn);
            btn.setEnabled(!buttonsFreezing);

            elem.add(btn, BorderLayout.CENTER);

            btn.setText(c.toString());

            ActionListener agentHandler = new EditAgentListener(this, isStart, cell.getRow(), cell.getCol(), c);
            btn.addActionListener(agentHandler);

            newGrid.add(elem);
        }

        // La grille de début correspond à l'élément 0 du panel grid box - la grille de fin correspond à l'élément 2 (l'élément 1 = le panel contenant les flèches)
        gridBox.add(newGrid, isStart ? 0 : 2);

        return newGrid;
    }

    /**
     * Création de la grille de calcul
     * @param grid la grille sur laquelle on se base pour actualiser la vue
     * @return
     */
    private JPanel createGridCalc(Grid grid) {
        JPanel newGrid = new JPanel(new GridLayout(5, 5));
        newGrid.setBorder(new EmptyBorder(5, 100, 5, 100));
        // newGrid.setBackground(brown);

        List<Cell> cells = grid.getCells();
        for (Cell cell : cells) {
            JPanel elem = new JPanel();
            elem.setBackground((cell.getRow()+cell.getCol()) % 2 == 0 ? Config.beige : Config.brown);
            JLabel txt = new JLabel();
            txt.setFont(new Font("Serif", Font.BOLD, 18));
            txt.setForeground(Color.WHITE);
            elem.add(txt);

            Character c;
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

    /**
     * Créé un panel avec des flèches (le panel au centre des grilles entre la grille de début et la grille de fin)
     * @return le panel contenant les flèches
     */
    private JPanel createArrow() {

        JPanel newArrow = new JPanel(); // new FlowLayout()
        newArrow.setLayout(new BoxLayout(newArrow, BoxLayout.X_AXIS));
        newArrow.setBorder(new EmptyBorder(5, 5, 5, 5));

        Image img = Util.getImage("right-arrow.png");
        if (img != null) {
            int i = 0;
            while (i < 3) {
                Image newImg = img.getScaledInstance(64, 64, Image.SCALE_SMOOTH); // scale it the smooth way
                ImageIcon imgIcon = new ImageIcon(newImg);
                JLabel arrowLabel = new JLabel();
                arrowLabel.setIcon(imgIcon);
                newArrow.add(arrowLabel);
                i++;
            }
        }

        gridBox.add(newArrow);

        return newArrow;
    }

    /**
     * Créé le panel de crédits contenant l'icône représentant notre binôme et un texte
     * @return le panel de crédits
     */
    private JPanel createCreditBox() {

        JPanel creditBox = new JPanel(); // new FlowLayout()
        creditBox.setLayout(new BoxLayout(creditBox, BoxLayout.X_AXIS));
        creditBox.setBorder(new EmptyBorder(5, 5, 5, 5));
        Image img = Util.getImage("dorians-logo.png");
        if (img != null) {
            Image newImg = img.getScaledInstance(32, 32,  Image.SCALE_SMOOTH); // scale it the smooth way
            ImageIcon imgIcon = new ImageIcon(newImg);
            JLabel arrowLabel = new JLabel();
            arrowLabel.setIcon(imgIcon);
            creditBox.add(arrowLabel);
        }

        JLabel txt = new JLabel();
        txt.setFont(new Font("Sans-Serif", Font.BOLD, 18));
        txt.setForeground(Color.DARK_GRAY);
        txt.setText("Multithread, optimis\u00e9 et comment\u00e9 - R\u00e9alis\u00e9 par LOREK Dorian et RANCHON Dorian");
        creditBox.add(txt);

        if (img != null) {
            Image newImg = img.getScaledInstance(32, 32,  Image.SCALE_SMOOTH); // scale it the smooth way
            ImageIcon imgIcon = new ImageIcon(newImg);
            JLabel arrowLabel = new JLabel();
            arrowLabel.setIcon(imgIcon);
            creditBox.add(arrowLabel);
        }

        // bottomBox.add(creditBox);

        return creditBox;
    }

    /**
     * Méthode modifiant l'état des boutons sur les grilles de début et de fin
     * Permet de désactiver tous les boutons lorsque la simulation est en cours, et de les réactiver à la fin de la simulation
     */
    private void changeButtonCalcState() {
        for (JButton btn : gridCalcButtons) {
            btn.setEnabled(!buttonsFreezing);
        }
    }

    /**
     * Méthode appelée lorsqu'un contrôleur envoie une modification de propriété
     * newGrid : mise à jour de la grille de simulation (elle se supprime et se recréé)
     * gridStartUpdate : mise à jour de la grille de début (lorsqu'on clique sur un bouton et que l'état d'un agent change) -> on supprime la grille de début et on la recréée
     * gridEndUpdate : mise à jour de la grille de fin (lorsqu'on clique sur un bouton et que l'état d'un agent change) -> on supprime la grille de fin et on la recréée
     * wrongGrids : activation d'un message d'erreur si les deux grilles n'ont pas les mêmes agents
     * freezeGrids : activation / désactivation des boutons de la grille (on désactive les boutons lorsque la simulation démarre et on réactive ensuite)
     * @param evt
     */
    @Override
    public void propertyChange(PropertyChangeEvent evt) {
        SwingUtilities.invokeLater(new Runnable() {
            public void run() {
                String evtName = evt.getPropertyName();
                errorBox.setVisible(false);
                if (evtName.equals("newGrid")) {

                    if (Config.DEBUG)
                        System.out.println("[Swing] Simulation - updated grid");
                    Grid newGrid = (Grid) evt.getNewValue();
                    gridCalcBox.removeAll();
                    createGridCalc(newGrid);
                    gridCalcBox.revalidate();

                } else if (evtName.equals("gridStartUpdate")) {

                    if (Config.DEBUG)
                        System.out.println("[Swing] Simulation - start grid updated");
                    Grid newGridStart = (Grid) evt.getNewValue();

                    gridBox.remove(0);
                    createGridStartEnd(newGridStart, true);
                    gridBox.revalidate();

                } else if (evtName.equals("gridEndUpdate")) {

                    if (Config.DEBUG)
                        System.out.println("[Swing] Simulation - end grid updated");
                    Grid newGridEnd = (Grid) evt.getNewValue();
                    newGridEnd.display();

                    gridBox.remove(2); // TODO: tester
                    createGridStartEnd(newGridEnd, false);
                    gridBox.revalidate();

                } else if (evtName.equals("wrongGrids")) {

                    if (Config.DEBUG)
                        System.out.println("[Swing] Simulation - wrong grids");

                    errorBox.setVisible(true);

                } else if (evtName.equals("freezeGrids")) {

                    boolean freezeButtons = (boolean) evt.getNewValue();
                    if (Config.DEBUG)
                        System.out.println("[Swing] Simulation - freezing grids: " + freezeButtons);
                    buttonsFreezing = freezeButtons;
                    changeButtonCalcState();

                }
            }
        });
    }
}
