package sheep.games.life;

import sheep.features.Feature;
import sheep.sheets.Sheet;
import sheep.ui.UI;

/**
 * Represents the Game of Life feature.
 * Handles UI of the Game
 */
public class Life implements Feature {
    private final LifeGame lifeGame;

    /**
     * Constructs a Life feature with the given sheet.
     * Calls upon the LifeGame class to handle simulation
     *
     * @param sheet The sheet representing the game board.
     * @requires sheet != null
     */
    public Life(Sheet sheet) {
        this.lifeGame = new LifeGame(sheet);
    }

    /**
     * Creates a Life UI application that calls upon the LifeGame class
     *
     * @param ui The UI that enables the user to contact the LifeGame class
     * @requires ui != null
     */
    @Override
    public void register(UI ui) {
        ui.addFeature("gol-start", "Start GOL", (row, column, prompt) -> startGame());
        ui.addFeature("gol-end", "End GOL", (row, column, prompt) -> endGame());
        ui.onTick(lifeGame);
    }

    /**
     * Starts the Game of Life.
     */
    private void startGame() {
        lifeGame.startGame();
    }

    /**
     * Ends the Game of Life.
     */
    private void endGame() {
        lifeGame.endGame();
    }
}
