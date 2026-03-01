package pacman;

import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.RenderingHints;

/**
 * Repräsentiert den Spieler (Pac-Man).
 * Verwaltet Position, Bewegungslogik und Darstellung.
 */
public class PacMan {

    public static final int START_COL = 9;
    public static final int START_ROW = 15;

    private int col;
    private int row;
    private Direction direction;
    private Direction nextDirection;

    private int mouthAngle = 45;
    private int mouthDelta = -5;

    public PacMan() {
        reset();
    }

    public void reset() {
        col           = START_COL;
        row           = START_ROW;
        direction     = Direction.LEFT;
        nextDirection = Direction.LEFT;
        mouthAngle    = 45;
        mouthDelta    = -5;
    }

    /**
     * Bewegt Pac-Man um einen Schritt.
     * Versucht zuerst die gewünschte Richtung, fällt sonst auf die aktuelle zurück.
     */
    public void move(Maze maze) {
        // Gewünschte Richtung versuchen
        int nc = wrapCol(col + nextDirection.dx, maze);
        int nr = row + nextDirection.dy;
        if (!maze.isWall(nr, nc)) {
            direction = nextDirection;
            col = nc;
            row = nr;
        } else {
            // Aktuelle Richtung beibehalten
            int cc = wrapCol(col + direction.dx, maze);
            int cr = row + direction.dy;
            if (!maze.isWall(cr, cc)) {
                col = cc;
                row = cr;
            }
        }

        // Mund animieren
        mouthAngle += mouthDelta;
        if (mouthAngle <= 5 || mouthAngle >= 45) mouthDelta = -mouthDelta;
    }

    private int wrapCol(int c, Maze maze) {
        if (c < 0) return maze.COLS() - 1;
        if (c >= maze.COLS()) return 0;
        return c;
    }

    public void draw(Graphics2D g2) {
        int t = GameConstants.TILE_SIZE;
        int x = col * t;
        int y = row * t;

        g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
        g2.setColor(Color.YELLOW);
        g2.fillArc(x + 1, y + 1, t - 2, t - 2, startAngle() + mouthAngle / 2, 360 - mouthAngle);

        // Auge
        g2.setColor(Color.BLACK);
        int ex = 0, ey = 0;
        switch (direction) {
            case RIGHT: ex =  4; ey = -4; break;
            case LEFT:  ex = -4; ey = -4; break;
            case UP:    ex =  4; ey = -6; break;
            case DOWN:  ex =  4; ey =  2; break;
            default: break;
        }
        g2.fillOval(x + t / 2 + ex - 2, y + t / 2 + ey - 2, 4, 4);
    }

    private int startAngle() {
        switch (direction) {
            case RIGHT: return 0;
            case LEFT:  return 180;
            case UP:    return 90;
            case DOWN:  return 270;
            default:    return 0;
        }
    }

    // --- Getter & Setter ---
    public int getCol()                       { return col; }
    public int getRow()                       { return row; }
    public Direction getDirection()           { return direction; }
    public void setNextDirection(Direction d) { this.nextDirection = d; }
}
