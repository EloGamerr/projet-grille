package fr.tia.projet;

import java.awt.Color;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class Config {
    public static final int GRID_SIZE = 5;
    public static final ArrayList<Character> charList = new ArrayList<>() {{
        add('A');
        add('B');
        add('C');
        add('D');
        add('\0');
    }};

    public static final Map<Character, Color> colorList = new HashMap<>() {{
        put('A', Color.CYAN);
        put('B', Color.GREEN);
        put('C', Color.LIGHT_GRAY);
        put('D', Color.ORANGE);
        put('\0', Color.YELLOW);
    }};
}
