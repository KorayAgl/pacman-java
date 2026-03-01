package pacman;

import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.RenderingHints;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

/**
 * Repräsentiert einen Geist.
 * Jeder Geist hat eine eigene Farbe und ein eigenes KI-Verhalten.
 */
public class Ghost {

    public enum GhostType { BLINKY, PINKY, INKY, CLYDE }

    private static final Color FRIGHTENED_BLUE  = new Color(0, 0, 200);
    private static final Color FRIGHTENED_WHITE = new Color(255, 255, 255);
    private static final Random RANDOM = new Random();

    private final GhostType type;
    private final Color normalColor;
    private final int startCol;
    private final int startRow;

    private int col;
    private int row;
    private Direction direction;
    private boolean frightened;
    private int frightenedTimer;

    public Ghost(GhostType type, int startCol, int startRow, Color color) {
        this.type        = type;
        this.startCol    = startCol;
        this.startRow    = startRow;
        this.normalColor = color;
        reset();
    }

    public void reset() {
        col             = startCol;
        row             = startRow;
        direction       = Direction.LEFT;
        frightened      = false;
        frightenedTimer = 0;
    }

    public void move(Maze maze, PacMan pacMan) {
        List<Direction> possible = getPossibleDirections(maze);
        if (possible.isEmpty()) return;

        Direction chosen;
        if (frightened) {
            chosen = possible.get(RANDOM.nextInt(possible.size()));
            if (--frightenedTimer <= 0) frightened = false;
        } else {
            chosen = chooseDirection(possible, pacMan);
        }

        direction = chosen;
        col += direction.dx;
        row += direction.dy;

        // Tunnel-Wrap
        if (col < 0) col = maze.COLS() - 1;
        else if (col >= maze.COLS()) col = 0;
    }

    private Direction chooseDirection(List<Direction> possible, PacMan pacMan) {
        switch (type) {
            case BLINKY:
                return towards(possible, pacMan.getCol(), pacMan.getRow());
            case PINKY:
                return towards(possible,
                        pacMan.getCol() + pacMan.getDirection().dx * 4,
                        pacMan.getRow() + pacMan.getDirection().dy * 4);
            default: // INKY, CLYDE
                if (RANDOM.nextInt(3) == 0)
                    return possible.get(RANDOM.nextInt(possible.size()));
                return towards(possible, pacMan.getCol(), pacMan.getRow());
        }
    }

    private Direction towards(List<Direction> possible, int targetCol, int targetRow) {
        Direction best = possible.get(0);
        double bestDist = Double.MAX_VALUE;
        for (Direction d : possible) {
            double dist = Math.hypot((col + d.dx) - targetCol, (row + d.dy) - targetRow);
            if (dist < bestDist) { bestDist = dist; best = d; }
        }
        return best;
    }

    private List<Direction> getPossibleDirections(Maze maze) {
        List<Direction> list = new ArrayList<>();
        for (Direction d : Direction.values()) {
            if (d == Direction.NONE || d == direction.opposite()) continue;
            int nc = col + d.dx;
            int nr = row + d.dy;
            if (nc < 0) nc = maze.COLS() - 1;
            else if (nc >= maze.COLS()) nc = 0;
            if (!maze.isWall(nr, nc)) list.add(d);
        }
        // Umkehren als Fallback
        if (list.isEmpty()) {
            Direction opp = direction.opposite();
            if (!maze.isWall(row + opp.dy, col + opp.dx)) list.add(opp);
        }
        return list;
    }

    public void setFrightened() {
        frightened      = true;
        frightenedTimer = GameConstants.FRIGHTENED_DURATION;
    }

    public void draw(Graphics2D g2) {
        int t = GameConstants.TILE_SIZE;
        int x = col * t;
        int y = row * t;

        g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);

        Color body;
        if (frightened) {
            body = (frightenedTimer < 10 && frightenedTimer % 4 < 2) ? FRIGHTENED_WHITE : FRIGHTENED_BLUE;
        } else {
            body = normalColor;
        }

        // Körper
        g2.setColor(body);
        g2.fillArc(x + 1, y + 1, t - 2, t - 2, 0, 180);
        g2.fillRect(x + 1, y + t / 2, t - 2, t / 2 - 2);

        // Zacken unten
        int zw = (t - 2) / 3;
        for (int i = 0; i < 3; i++) {
            int[] xs = {x + 1 + i * zw, x + 1 + i * zw + zw / 2, x + 1 + (i + 1) * zw};
            int[] ys = {y + t - 2, y + t - 7, y + t - 2};
            g2.setColor(Color.BLACK);
            g2.fillPolygon(xs, ys, 3);
        }

        // Augen
        if (!frightened) {
            g2.setColor(Color.WHITE);
            g2.fillOval(x + 4, y + 5, 6, 7);
            g2.fillOval(x + t - 10, y + 5, 6, 7);
            g2.setColor(Color.BLUE);
            g2.fillOval(x + 6  + direction.dx * 2, y + 7 + direction.dy * 2, 3, 3);
            g2.fillOval(x + t - 8 + direction.dx * 2, y + 7 + direction.dy * 2, 3, 3);
        } else {
            g2.setColor(Color.WHITE);
            g2.drawLine(x + 5,      y + 8,  x + 8,      y + 11);
            g2.drawLine(x + 8,      y + 11, x + 11,     y + 8);
            g2.drawLine(x + t - 11, y + 8,  x + t - 8,  y + 11);
            g2.drawLine(x + t - 8,  y + 11, x + t - 5,  y + 8);
        }
    }

    // --- Getter ---
    public int getCol()           { return col; }
    public int getRow()           { return row; }
    public boolean isFrightened() { return frightened; }
}
