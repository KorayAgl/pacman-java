package pacman;

/**
 * Repräsentiert das Pac-Man Labyrinth als 2D-Array.
 *
 * Legende:
 *   1 = Wand
 *   0 = Punkt (Dot)
 *   2 = Power-Pellet
 *   3 = Leeres Feld
 *   4 = Geister-Haus
 */
public class Maze {

    private static final int[][] LAYOUT = {
        {1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1},
        {1, 0, 0, 0, 0, 0, 0, 0, 0, 1, 0, 0, 0, 0, 0, 0, 0, 0, 1},
        {1, 2, 1, 1, 0, 1, 1, 1, 0, 1, 0, 1, 1, 1, 0, 1, 1, 2, 1},
        {1, 0, 1, 1, 0, 1, 1, 1, 0, 1, 0, 1, 1, 1, 0, 1, 1, 0, 1},
        {1, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 1},
        {1, 0, 1, 1, 0, 1, 0, 1, 1, 1, 1, 1, 0, 1, 0, 1, 1, 0, 1},
        {1, 0, 0, 0, 0, 1, 0, 0, 0, 1, 0, 0, 0, 1, 0, 0, 0, 0, 1},
        {1, 1, 1, 1, 0, 1, 1, 1, 3, 1, 3, 1, 1, 1, 0, 1, 1, 1, 1},
        {3, 3, 3, 1, 0, 1, 3, 3, 3, 3, 3, 3, 3, 1, 0, 1, 3, 3, 3},
        {1, 1, 1, 1, 0, 1, 3, 1, 1, 4, 1, 1, 3, 1, 0, 1, 1, 1, 1},
        {3, 3, 3, 3, 0, 3, 3, 1, 4, 4, 4, 1, 3, 3, 0, 3, 3, 3, 3},
        {1, 1, 1, 1, 0, 1, 3, 1, 1, 1, 1, 1, 3, 1, 0, 1, 1, 1, 1},
        {3, 3, 3, 1, 0, 1, 3, 3, 3, 3, 3, 3, 3, 1, 0, 1, 3, 3, 3},
        {1, 1, 1, 1, 0, 1, 3, 1, 1, 1, 1, 1, 3, 1, 0, 1, 1, 1, 1},
        {1, 0, 0, 0, 0, 0, 0, 0, 0, 1, 0, 0, 0, 0, 0, 0, 0, 0, 1},
        {1, 0, 1, 1, 0, 1, 1, 1, 0, 1, 0, 1, 1, 1, 0, 1, 1, 0, 1},
        {1, 2, 0, 1, 0, 0, 0, 0, 0, 3, 0, 0, 0, 0, 0, 1, 0, 2, 1},
        {1, 1, 0, 1, 0, 1, 0, 1, 1, 1, 1, 1, 0, 1, 0, 1, 0, 1, 1},
        {1, 0, 0, 0, 0, 1, 0, 0, 0, 1, 0, 0, 0, 1, 0, 0, 0, 0, 1},
        {1, 0, 1, 1, 1, 1, 1, 1, 0, 1, 0, 1, 1, 1, 1, 1, 1, 0, 1},
        {1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1}
    };

    private final int[][] grid;

    public Maze() {
        grid = new int[ROWS()][COLS()];
        for (int r = 0; r < ROWS(); r++) {
            System.arraycopy(LAYOUT[r], 0, grid[r], 0, COLS());
        }
    }

    public int  getCell(int row, int col)              { return grid[row][col]; }
    public void setCell(int row, int col, int value)   { grid[row][col] = value; }

    public boolean isWall(int row, int col) {
        if (row < 0 || row >= ROWS() || col < 0 || col >= COLS()) return true;
        return grid[row][col] == 1;
    }

    public boolean hasDot(int row, int col)        { return grid[row][col] == 0; }
    public boolean hasPowerPellet(int row, int col){ return grid[row][col] == 2; }

    public int countRemainingDots() {
        int count = 0;
        for (int[] row : grid)
            for (int cell : row)
                if (cell == 0 || cell == 2) count++;
        return count;
    }

    public int ROWS() { return LAYOUT.length; }
    public int COLS() { return LAYOUT[0].length; }
}
