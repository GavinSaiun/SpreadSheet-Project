package sheep.games.life;

import sheep.sheets.CellLocation;
import sheep.sheets.Sheet;

import java.util.List;

/**
 * The Evolution interface represents the process of evolving a generation in a cellular automaton.
 */
public interface Evolution {

    /**
     * Generates the next evolved generation based on the game of life Rules
     *
     * @return A list of CellLocation that contain the position of the next tick's generation
     */
    List<CellLocation> evolvedGeneration();
}
