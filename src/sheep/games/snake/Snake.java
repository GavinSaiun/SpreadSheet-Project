package sheep.games.snake;

import sheep.expression.TypeError;
import sheep.expression.basic.Constant;
import sheep.features.Feature;
import sheep.games.random.RandomCell;
import sheep.sheets.CellLocation;
import sheep.sheets.Sheet;
import sheep.ui.Perform;
import sheep.ui.Prompt;
import sheep.ui.Tick;
import sheep.ui.UI;
import java.util.*;

/**
 * Represents the Snake game feature.
 * Handles the UI behind the SnakeGame.
 */
public class Snake implements Feature, Tick {
    private final Sheet sheet;
    private CellLocation previousTail;
    private boolean started = false;
    private boolean hasEaten = false;
    private final RandomCell randomCell;
    private List<CellLocation> snakeBody = new ArrayList<>();
    private final SnakeMovement snakeMovement;
    private final SnakeGameLogic snakeGameLogic;
    private final SnakeFood snakeFood;
    private final SnakeRender snakeRender;
    private final SnakeController snakeController;


    /**
     * Constructs a Snake game feature with the given sheet and random cell generator.
     * Calls upon helper classes to handle Snake properties.
     *
     * @param sheet      The sheet representing the game board.
     * @param randomCell The random cell generator.
     * @requires sheet != null, randomCell != null;
     */
    public Snake(Sheet sheet, RandomCell randomCell) {
        this.sheet = sheet;
        this.randomCell = randomCell;

        this.snakeMovement = new SnakeMovement(sheet);
        this.snakeGameLogic = new SnakeGameLogic(sheet);
        this.snakeFood = new SnakeFood(sheet, randomCell);
        this.snakeRender = new SnakeRender(sheet);
        this.snakeController = new SnakeController(sheet);

    }

    /**
     * Registers the Snake game feature with the UI, enabling interaction and control.
     * Controls Snake Start, Snake Speed and Snake controls.
     *
     * @param ui The UI to register the Snake game with.
     * @requires ui != null
     */
    @Override
    public void register(UI ui) {
        ui.onTick(this);
        ui.addFeature("snake", "Start Snake", new SnakeStart());

        // Snake UI controls
        ui.onKey("w", "Move Up", (row, column, prompt) -> snakeController.changeDirection(-1, 0));
        ui.onKey("a", "Move Left", (row, column, prompt) -> snakeController.changeDirection(0, -1));
        ui.onKey("s", "Move Down", (row, column, prompt) -> snakeController.changeDirection(1, 0));
        ui.onKey("d", "Move Right", (row, column, prompt) -> snakeController.changeDirection(0, 1));

        // Controls Snake Game Speed (ms)
        ui.setTickSpeed(300);
    }

    /**
     * Executes the logic for each tick of the Snake game.
     *
     * @param prompt The prompt for user interaction.
     * @return True if the tick was executed successfully, false otherwise.
     */
    @Override
    public boolean onTick(Prompt prompt) {

        if (!started) {
            return false;
        }

        // Controls Snake Movement, Handles default movement
        previousTail = snakeMovement.previousTail(snakeBody);
        snakeBody = snakeMovement.moveSnake(snakeBody,
                snakeController.getUpdatedHorizontalDirection(),
                snakeController.getUpdatedVerticalDirection());

        // Checks for Snake Game Over
        if (!snakeGameLogic.inBounds(snakeBody) || !snakeGameLogic.selfCollision(snakeBody)) {
            prompt.message("Game Over!");
            started = false;
            return false;
        }

        // Handles the removal of the snakeTail
        if (previousTail != null && !snakeFood.checkFoodCollision(snakeBody)) {
            snakeRender.removeTrail(previousTail);
        }

        // Handles snake growth i.e. Snake eats -> tick -> Snake grows
        if (hasEaten) {
            snakeBody.add(previousTail);
            hasEaten = false;
        }

        // Handles when Snake Eats Food
        if (snakeFood.checkFoodCollision(snakeBody)) {
            snakeRender.removeTrail(previousTail);
            snakeFood.addRandomFood();
            hasEaten = true;
        }

        // Render the snake in sheet
        snakeRender.renderSnake(snakeBody);
        return true;
    }

    /**
     * Represents the action of starting the Snake game.
     */
    private class SnakeStart implements Perform {

        /**
         * Performs the action of starting the Snake game.
         *
         * @param row     The row where the Snake should start. (-2 for absence of user, -1 for default location)
         * @param column  The column where the Snake should start. (-2 for absence of user, -1 for default location)
         * @param prompt  The prompt for user interaction.
         */
        @Override
        public void perform(int row, int column, Prompt prompt) {
            try {
                CellLocation startLocation;

                // Converts any non null cellLocations
                snakeFood.convertNonNullToFood();
                // Absence of User on Sheet is represented as (-2, -2), default location is (1, 1)
                if (row == -2 && column == -2) {
                    startLocation = new CellLocation(1, 1);
                } else {
                    // if User has specified snake location, prioritise user.
                    startLocation = new CellLocation(row, column);
                }
                sheet.update(startLocation, new Constant(1));
                snakeBody.add(startLocation);
            } catch (TypeError e) {
                // Handle type error
            }
            // Enable onTick method to execute caller methods
            started = true;
        }
    }
}
