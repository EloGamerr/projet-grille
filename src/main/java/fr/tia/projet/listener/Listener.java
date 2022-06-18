package fr.tia.projet.listener;

import fr.tia.projet.SwingView;

public class Listener {
    private final SwingView view;

    public Listener(SwingView view) {
        this.view = view;
    }

    /**
     * Permet de récupérer la vue courante sur laquelle le Listener a été lancé (juste une norme, pas vraiment exploité ici dans ce projet)
     * @return
     */
    public SwingView getView() {
        return view;
    }
}
