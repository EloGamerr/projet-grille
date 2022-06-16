package fr.tia.projet.listener;

import fr.tia.projet.SwingView;

public class Listener {
    private final SwingView view;

    public Listener(SwingView view) {
        this.view = view;
    }

    public SwingView getView() {
        return view;
    }
}
