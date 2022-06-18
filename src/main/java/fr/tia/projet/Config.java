package fr.tia.projet;

import java.awt.Color;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class Config {
    public static final boolean DEBUG = true; // Pour nous en tant que développeurs
    public static final Color beige = new Color(255, 183, 168, 220);
    public static final Color brown = new Color(124, 94, 57, 220);

    public static final int GRID_SIZE = 5;
    public static final ArrayList<Character> charList = new ArrayList<>() {{
        add('A');
        add('B');
        add('C');
        add('D');
        add('E');
        add('F');
        add('G');
        add('H');
        add('I');
        add('J');
        add('K');
        add('L');
        add('M');
        add('N');
        add('O');
        add('P');
        add('Q');
        add('R');
        add('S');
        add('T');
        add('U');
        add('V');
        add('W');
        add('X');
        add('Y');
        add('Z');
    }};

    public static final Map<Character, Color> colorList = new HashMap<>() {{
        put('A', Color.RED);
        put('B', Color.GREEN);
        put('C', Color.BLUE);
        put('D', Color.MAGENTA);
        put('E', Color.CYAN);
        put('F', Color.ORANGE);
        put('G', Color.RED);
        put('H', Color.YELLOW);
        put('I', Color.GRAY);
        put('J', Color.DARK_GRAY);
        put('K', Color.LIGHT_GRAY);
        put('L', Color.ORANGE);
        put('M', Color.PINK);
        put('N', Color.RED);
        put('O', Color.GREEN);
        put('P', Color.BLUE);
        put('Q', Color.MAGENTA);
        put('R', Color.CYAN);
        put('S', Color.MAGENTA);
        put('T', Color.PINK);
        put('U', Color.YELLOW);
        put('V', Color.GRAY);
        put('W', Color.DARK_GRAY);
        put('X', Color.LIGHT_GRAY);
        put('Y', Color.ORANGE);
        put('Z', Color.PINK);
        put('\0', Color.WHITE);
    }};
}
