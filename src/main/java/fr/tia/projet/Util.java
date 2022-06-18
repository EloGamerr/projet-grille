package fr.tia.projet;

import javax.imageio.ImageIO;
import java.awt.Image;
import java.io.IOException;

public class Util {
    /**
     * Récupère une image de façon générique pour l'insérer sur Swing
     * @param filename destination du fichier à récupérer
     * @return
     */
    public static Image getImage(String filename) {
        try {
            return ImageIO.read(Util.class.getResourceAsStream(
                    "/" + filename));
        } catch (IOException e) {
            e.printStackTrace();
            return null;
        }
    }
}
