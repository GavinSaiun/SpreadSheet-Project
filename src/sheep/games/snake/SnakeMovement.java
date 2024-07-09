package sheep.games.snake;

import sheep.sheets.CellLocation;
import sheep.sheets.Sheet;
import java.util.ArrayList;
import java.util.List;

/**
 * Manages the movement of the snake in the Snake game.
 */
public class SnakeMovement {

    private final Sheet sheet;

    /**
     * Constructs a SnakeMovement object with the given sheet.
     *
     * @param sheet The sheet representing the game board.
     * @requires sheet != null
     */
    public SnakeMovement(Sheet sheet) {
        this.sheet = sheet;
    }

    /**
     * Moves the snake in the specified direction.
     *
     * @param snakeBody           The list of cell locations representing the snake's body.
     * @param horizontalDirection The horizontal direction (-1 for left, 1 for right, 0 for no change).
     * @param verticalDirection   The vertical direction (-1 for up, 1 for down, 0 for no change).
     * @return The updated list of cell locations representing the snake's body after movement.
     * @requires snakeBody != null, horizontalDirection != null, veritcalDirection != null
     * @ensures new List type containing new snakeBody location
     */
    public List<CellLocation> moveSnake(List<CellLocation> snakeBody,
                                        int horizontalDirection,
                                        int verticalDirection) {
        // Create a new list to hold the updated positions
        List<CellLocation> shiftedSnakeBody = new ArrayList<>();

        // Recreates the snakeHead
        int snakeHeadRowDirection = snakeBody.get(0).getRow() + horizontalDirection;
        int snakeHeadColumnDirection = snakeBody.get(0).getColumn() + verticalDirection;

        // move each element to its new position
        shiftedSnakeBody.add(new CellLocation(snakeHeadRowDirection, snakeHeadColumnDirection));
        for (int i = 0; i <= snakeBody.size() - 2; i++) {
            shiftedSnakeBody.add(snakeBody.get(i));
        }

        return shiftedSnakeBody;
    }

    /**
     * Retrieves the location of the snake's previous tail.
     * Handles the removal for later purposes.
     *
     * @param snakeBody The list of cell locations representing the snake's body.
     * @return The cell location of the snake's previous tail.
     * @requires snakeBody != null
     * @ensures CellLocation of the previous tick's tail
     */
    public CellLocation previousTail(List<CellLocation> snakeBody) {
        return snakeBody.get(snakeBody.size() - 1);
    }
}
