package pacman;

import javax.swing.SwingUtilities;

/**
 * Einstiegspunkt der Anwendung.
 * Startet das Spielfenster im Event Dispatch Thread.
 */
public class Main {

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            GameWindow window = new GameWindow();
            window.setVisible(true);
        });
    }
}
