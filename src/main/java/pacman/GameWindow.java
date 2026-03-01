package pacman;

import javax.swing.JFrame;

/**
 * Das Hauptfenster des Spiels.
 * Erstellt das JFrame und bettet das GamePanel ein.
 */
public class GameWindow extends JFrame {

    public GameWindow() {
        setTitle("Pac-Man");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setResizable(false);

        GamePanel gamePanel = new GamePanel();
        add(gamePanel);

        pack();
        setLocationRelativeTo(null);
    }
}
