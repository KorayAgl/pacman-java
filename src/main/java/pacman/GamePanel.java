package pacman;

import javax.swing.JPanel;
import javax.swing.Timer;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.RenderingHints;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.util.ArrayList;
import java.util.List;

/**
 * Haupt-Panel des Spiels.
 * Enthält Spielschleife, Eingabeverarbeitung, Kollisionserkennung und Rendering.
 */
public class GamePanel extends JPanel implements ActionListener {

    private Maze maze;
    private final PacMan pacMan;
    private final List<Ghost> ghosts;

    private int score;
    private int lives;
    private boolean gameOver;
    private boolean won;
    private boolean paused;

    private final Timer gameTimer;

    public GamePanel() {
        setPreferredSize(new Dimension(GameConstants.SCREEN_WIDTH, GameConstants.SCREEN_HEIGHT));
        setBackground(Color.BLACK);
        setFocusable(true);

        maze   = new Maze();
        pacMan = new PacMan();
        ghosts = createGhosts();
        score  = 0;
        lives  = GameConstants.STARTING_LIVES;

        addKeyListener(new KeyAdapter() {
            @Override
            public void keyPressed(KeyEvent e) {
                handleInput(e);
            }
        });

        gameTimer = new Timer(GameConstants.GAME_DELAY, this);
        gameTimer.start();
    }

    private List<Ghost> createGhosts() {
        List<Ghost> list = new ArrayList<>();
        list.add(new Ghost(Ghost.GhostType.BLINKY,  9,  9, new Color(255,   0,   0)));
        list.add(new Ghost(Ghost.GhostType.PINKY,   9, 10, new Color(255, 184, 255)));
        list.add(new Ghost(Ghost.GhostType.INKY,    8, 10, new Color(  0, 255, 255)));
        list.add(new Ghost(Ghost.GhostType.CLYDE,  10, 10, new Color(255, 184,  82)));
        return list;
    }

    // -------------------------------------------------------
    //  Eingabe
    // -------------------------------------------------------

    private void handleInput(KeyEvent e) {
        if (gameOver || won) {
            if (e.getKeyCode() == KeyEvent.VK_ENTER) restartGame();
            return;
        }
        switch (e.getKeyCode()) {
            case KeyEvent.VK_UP:    case KeyEvent.VK_W: pacMan.setNextDirection(Direction.UP);    break;
            case KeyEvent.VK_DOWN:  case KeyEvent.VK_S: pacMan.setNextDirection(Direction.DOWN);  break;
            case KeyEvent.VK_LEFT:  case KeyEvent.VK_A: pacMan.setNextDirection(Direction.LEFT);  break;
            case KeyEvent.VK_RIGHT: case KeyEvent.VK_D: pacMan.setNextDirection(Direction.RIGHT); break;
            case KeyEvent.VK_P: paused = !paused; break;
        }
    }

    // -------------------------------------------------------
    //  Spielschleife
    // -------------------------------------------------------

    @Override
    public void actionPerformed(ActionEvent e) {
        if (!gameOver && !won && !paused) update();
        repaint();
    }

    private void update() {
        pacMan.move(maze);
        collectDots();
        for (Ghost g : ghosts) g.move(maze, pacMan);
        checkCollisions();
        if (maze.countRemainingDots() == 0) { won = true; gameTimer.stop(); }
    }

    private void collectDots() {
        int r = pacMan.getRow(), c = pacMan.getCol();
        if (maze.hasDot(r, c)) {
            score += GameConstants.POINTS_DOT;
            maze.setCell(r, c, 3);
        } else if (maze.hasPowerPellet(r, c)) {
            score += GameConstants.POINTS_POWER_PELLET;
            maze.setCell(r, c, 3);
            ghosts.forEach(Ghost::setFrightened);
        }
    }

    private void checkCollisions() {
        for (Ghost g : ghosts) {
            if (g.getCol() == pacMan.getCol() && g.getRow() == pacMan.getRow()) {
                if (g.isFrightened()) {
                    score += GameConstants.POINTS_GHOST;
                    g.reset();
                } else {
                    loseLife();
                    return;
                }
            }
        }
    }

    private void loseLife() {
        if (--lives <= 0) {
            gameOver = true;
            gameTimer.stop();
        } else {
            pacMan.reset();
            ghosts.forEach(Ghost::reset);
        }
    }

    private void restartGame() {
        score    = 0;
        lives    = GameConstants.STARTING_LIVES;
        gameOver = false;
        won      = false;
        paused   = false;
        maze     = new Maze();
        pacMan.reset();
        ghosts.forEach(Ghost::reset);
        gameTimer.start();
        requestFocusInWindow();
    }

    // -------------------------------------------------------
    //  Rendering
    // -------------------------------------------------------

    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);
        Graphics2D g2 = (Graphics2D) g;
        g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);

        drawMaze(g2);
        drawStatusBar(g2);

        if (gameOver) {
            drawOverlay(g2, "GAME OVER", "ENTER drücken zum Neustart");
        } else if (won) {
            drawOverlay(g2, "YOU WIN!", "ENTER drücken zum Neustart");
        } else if (paused) {
            drawOverlay(g2, "PAUSE", "P drücken zum Fortfahren");
        } else {
            pacMan.draw(g2);
            ghosts.forEach(gh -> gh.draw(g2));
        }
    }

    private void drawMaze(Graphics2D g2) {
        int t = GameConstants.TILE_SIZE;
        for (int r = 0; r < maze.ROWS(); r++) {
            for (int c = 0; c < maze.COLS(); c++) {
                int cell = maze.getCell(r, c);
                int x = c * t, y = r * t;
                if (cell == 1) {
                    g2.setColor(new Color(33, 33, 255));
                    g2.fillRect(x, y, t, t);
                    g2.setColor(new Color(0, 0, 150));
                    g2.drawRect(x, y, t - 1, t - 1);
                } else if (cell == 0) {
                    g2.setColor(new Color(255, 255, 200));
                    g2.fillOval(x + t / 2 - 2, y + t / 2 - 2, 4, 4);
                } else if (cell == 2) {
                    if ((System.currentTimeMillis() / 400) % 2 == 0) {
                        g2.setColor(new Color(255, 255, 200));
                        g2.fillOval(x + t / 2 - 6, y + t / 2 - 6, 12, 12);
                    }
                }
            }
        }
    }

    private void drawStatusBar(Graphics2D g2) {
        int y0 = maze.ROWS() * GameConstants.TILE_SIZE;
        g2.setColor(Color.BLACK);
        g2.fillRect(0, y0, GameConstants.SCREEN_WIDTH, 50);

        g2.setColor(Color.WHITE);
        g2.setFont(new Font("Arial", Font.BOLD, 16));
        g2.drawString("Score: " + score, 10, y0 + 22);

        g2.setColor(Color.YELLOW);
        g2.setFont(new Font("Arial", Font.BOLD, 14));
        g2.drawString("Leben:", 10, y0 + 44);
        for (int i = 0; i < lives; i++)
            g2.fillArc(80 + i * 22, y0 + 30, 16, 16, 30, 300);

        g2.setFont(new Font("Arial", Font.PLAIN, 12));
        g2.drawString("P = Pause", GameConstants.SCREEN_WIDTH - 80, y0 + 22);
    }

    private void drawOverlay(Graphics2D g2, String headline, String sub) {
        g2.setColor(new Color(0, 0, 0, 170));
        g2.fillRect(0, 0, GameConstants.SCREEN_WIDTH, GameConstants.SCREEN_HEIGHT);

        g2.setColor(Color.YELLOW);
        g2.setFont(new Font("Arial", Font.BOLD, 36));
        int hw = g2.getFontMetrics().stringWidth(headline);
        g2.drawString(headline, (GameConstants.SCREEN_WIDTH - hw) / 2, GameConstants.SCREEN_HEIGHT / 2 - 20);

        g2.setColor(Color.WHITE);
        g2.setFont(new Font("Arial", Font.PLAIN, 16));
        int sw = g2.getFontMetrics().stringWidth(sub);
        g2.drawString(sub, (GameConstants.SCREEN_WIDTH - sw) / 2, GameConstants.SCREEN_HEIGHT / 2 + 20);

        String sc = "Punkte: " + score;
        g2.setColor(Color.LIGHT_GRAY);
        g2.setFont(new Font("Arial", Font.PLAIN, 14));
        int scw = g2.getFontMetrics().stringWidth(sc);
        g2.drawString(sc, (GameConstants.SCREEN_WIDTH - scw) / 2, GameConstants.SCREEN_HEIGHT / 2 + 50);
    }
}
