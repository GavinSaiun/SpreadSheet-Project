package sheep.games.snake;

import sheep.expression.TypeError;
import sheep.expression.basic.Constant;
import sheep.expression.basic.Nothing;
import sheep.sheets.CellLocation;
import sheep.sheets.Sheet;
import java.util.List;

/**
 * Renders the snake on the game board and removes its trail.
 */
public class SnakeRender {
    private final Sheet sheet;

    /**
     * Constructs a SnakeRender object with the given sheet.
     *
     * @param sheet The sheet representing the game board.
     * @requires sheet != null
     */
    public SnakeRender(Sheet sheet) {
        this.sheet = sheet;
    }

    /**
     * Renders the snake on the game board.
     *
     * @param snakeBody The list of cell locations representing the snake's body.
     * @requires snakeBody != null
     * @ensures Every CellLocation in snakeBody have value Constant(1)
     */
    public void renderSnake(List<CellLocation> snakeBody) {
        for (CellLocation snakeCell : snakeBody) {
            try {
                sheet.update(snakeCell, new Constant(1));
            } catch (TypeError e) {
                // Handle type error
            }
        }
    }

    /**
     * Removes the trail of the snake from the game board.
     *
     * @param snakeTail The cell location representing the snake's tail.
     * @requires snakeTail != null
     * @ensures trail in Sheet instance becomes Nothing()
     */
    public void removeTrail(CellLocation snakeTail) {
        try {
            sheet.update(snakeTail, new Nothing());
        } catch (TypeError e) {
            // Handle type error
        }
    }
}
