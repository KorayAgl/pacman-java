package pacman;

/**
 * Zentrale Klasse für alle Spielkonstanten.
 */
public final class GameConstants {

    public static final int TILE_SIZE  = 24;
    public static final int COLS       = 19;
    public static final int ROWS       = 21;

    public static final int SCREEN_WIDTH  = COLS * TILE_SIZE;
    public static final int SCREEN_HEIGHT = ROWS * TILE_SIZE + 50;

    public static final int GAME_DELAY = 150;

    public static final int POINTS_DOT          = 10;
    public static final int POINTS_POWER_PELLET  = 50;
    public static final int POINTS_GHOST         = 200;

    public static final int FRIGHTENED_DURATION = 30;
    public static final int STARTING_LIVES       = 3;

    private GameConstants() {}
}
