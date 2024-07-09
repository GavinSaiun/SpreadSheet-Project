package sheep.games.snake;

import sheep.sheets.CellLocation;
import sheep.sheets.Sheet;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

/**
 * Manages the game logic of the Snake game.
 */
public class SnakeGameLogic {

    private final Sheet sheet;

    /**
     * Constructs a SnakeGameLogic object with the given sheet.
     *
     * @param sheet The sheet representing the game board.
     * @requires sheet != null
     */
    public SnakeGameLogic(Sheet sheet) {
        this.sheet = sheet;
    }

    /**
     * Checks if the snake is within the bounds of the game board.
     *
     * @param snakeBody The list of cell locations representing the snake's body.
     * @return True if the snake is within bounds, false otherwise.
     * @requires snakeBody != null
     */
    public boolean inBounds(List<CellLocation> snakeBody) {
        for (CellLocation snakeCell : snakeBody) {
            // if snake is out of bounds, return false
            if (!sheet.contains(snakeCell)) {
                return false;
            }
        }
        return true;
    }

    /**
     * Checks if the snake has collided with itself.
     *
     * @param snakeBody The list of cell locations representing the snake's body.
     * @return True if the snake has collided with itself, false otherwise.
     * @requires snakeBody != null
     */
    public boolean selfCollision(List<CellLocation> snakeBody) {
        // Check for duplicates i.e. if the snake has collided with its own body
        Set<CellLocation> snake = new HashSet<>(snakeBody);
        return snake.size() >= snakeBody.size();
    }
}
