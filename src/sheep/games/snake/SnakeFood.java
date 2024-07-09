package sheep.games.snake;

import sheep.expression.Expression;
import sheep.expression.TypeError;
import sheep.expression.basic.Constant;
import sheep.games.random.RandomCell;
import sheep.sheets.CellLocation;
import sheep.sheets.Sheet;

import java.util.List;

/**
 * Manages the food items in the Snake game.
 */
public class SnakeFood {
    private final Sheet sheet;
    private final RandomCell randomCell;

    /**
     * Constructs a SnakeFood object with the given sheet and random cell generator.
     *
     * @param sheet      The sheet representing the game board.
     * @param randomCell The random cell generator.
     * @requires sheet != null, randomCell != null
     */
    public SnakeFood(Sheet sheet, RandomCell randomCell) {
        this.sheet = sheet;
        this.randomCell = randomCell;

    }

    /**
     * Adds random food to the game board
     */
    public void addRandomFood() {
        // Handles the random food location
        CellLocation foodLocation = randomCell.pick();
        try {
            sheet.update(foodLocation, new Constant(2));
        } catch (TypeError e) {
            // Handle type error
        }
    }

    /**
     * Checks if the snakeHead has collided with food.
     * if so, return true, else false
     *
     * @param snakeBody The list of cell locations representing the snake's body.
     * @return True if the snake has collided with food, false otherwise.
     * @requires snakeBody != null
     */
    public boolean checkFoodCollision(List<CellLocation> snakeBody) {

        // Verifies if snake head is in contact with Food (2)
        CellLocation head = snakeBody.get(0);
        Expression valueAtHead = sheet.valueAt(head);
        return valueAtHead != null && valueAtHead.equals(new Constant(2));
    }

    /**
     * Converts non-null cells to food.
     * Non-null cells are those cells that are not empty ("") or do not contain the value "1".
     * Any non-null cell is updated to contain the value 2, representing food.
     */
    public void convertNonNullToFood() {
        // Iterate through each cell
        for (int sheetRow = 0; sheetRow < sheet.getRows(); sheetRow++) {
            for (int sheetColumn = 0; sheetColumn < sheet.getColumns(); sheetColumn++) {
                CellLocation nonNullFood = new CellLocation(sheetRow, sheetColumn);

                // if the cell's value is non null, then convert to food
                if (sheet.valueAt(sheetRow, sheetColumn).getContent() != ""
                        || sheet.valueAt(sheetRow, sheetColumn).getContent() == "1") {
                    try {
                        sheet.update(nonNullFood, new Constant(2));
                    } catch (TypeError e) {
                        // Handle type error
                    }
                }
            }
        }
    }
}
