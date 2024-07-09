package sheep.games.snake;

import sheep.sheets.Sheet;

/**
 * Controls the movement direction of the snake in the Snake game.
 */
public class SnakeController {
    private int updatedHorizontalDirection = 0;
    private int updatedVerticalDirection = 0;
    private final Sheet sheet;

    /**
     * Constructs a SnakeController with the given sheet.
     * Controls Snake Movement, Start, and default movement
     *
     * @param sheet The sheet representing the game board.
     * @requires sheet != null
     */
    public SnakeController(Sheet sheet) {
        this.sheet = sheet;
    }

    /**
     * Changes the direction of the snake movement.
     *
     * @param horizontalDirection The horizontal direction (-1 for left, 1 for right, 0 for no change).
     * @param verticalDirection   The vertical direction (-1 for up, 1 for down, 0 for no change).
     * @requires horizontalDirection != null, veriticalDirection != null
     * @ensures snake direction would change to specified user direction
     */
    public void changeDirection(int horizontalDirection, int verticalDirection) {
        // Change the direction of the snake
        updatedHorizontalDirection = horizontalDirection;
        updatedVerticalDirection = verticalDirection;
    }

    /**
     * Gets the updated horizontal direction of the snake movement.
     *
     * @return The updated horizontal direction.
     */
    public int getUpdatedHorizontalDirection() {
        return updatedHorizontalDirection;
    }

    /**
     * Gets the updated vertical direction of the snake movement.
     *
     * @return The updated vertical direction.
     */
    public int getUpdatedVerticalDirection() {
        return updatedVerticalDirection;
    }
}
