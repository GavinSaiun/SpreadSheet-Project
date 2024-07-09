package sheep.games.life;

import sheep.expression.TypeError;
import sheep.expression.basic.Constant;
import sheep.sheets.CellLocation;
import sheep.sheets.Sheet;
import sheep.ui.Prompt;
import sheep.ui.Tick;
import java.util.List;

/**
 * Represents the Game of Life logic.
 * Handles Simulation of the GOL
 */
public class LifeGame implements Tick {
    private final Sheet sheet;
    private final EvolutionCalculator evolutionCalculator;
    private boolean started = false;

    /**
     * Constructs a LifeGame with the given sheet.
     *
     * @param sheet The sheet representing the game board.
     * @requires sheet != null
     */
    public LifeGame(Sheet sheet) {
        this.sheet = sheet;
        this.evolutionCalculator = new EvolutionCalculator(sheet);
    }

    /**
     * Performs a tick in the Game of Life.
     * Every Tick, it calls upon evolve() to evolve the
     * current generation of alive cells
     *
     * @param prompt The prompt for user interaction.
     * @return True if the tick was performed successfully, false otherwise.
     */
    @Override
    public boolean onTick(Prompt prompt) {
        if (!started) {
            return false;
        }
        evolve();
        return true;
    }

    /**
     * Evolves the game to the next generation.
     * Calculates the CellLocations that are alive in the next
     * generation, and renders them.
     */
    private void evolve() {
        List<CellLocation> nextGeneration = evolutionCalculator.evolvedGeneration();
        sheet.clear();
        render(nextGeneration);
    }

    /**
     * Renders the next generation of cells on the sheet.
     *
     * @param aliveCells The list of alive cell locations in the next generation.
     * @requires aliveCells != null
     */
    private void render(List<CellLocation> aliveCells) {
        for (CellLocation cell : aliveCells) {
            try {
                sheet.update(cell, new Constant(1));
            } catch (TypeError e) {
                throw new RuntimeException(e);
            }
        }
    }

    /**
     * Starts the Game of Life.
     */
    public void startGame() {
        started = true;
    }

    /**
     * Ends the Game of Life.
     */
    public void endGame() {
        started = false;
    }
}
