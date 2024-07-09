package sheep.games.life;

import sheep.expression.basic.Constant;
import sheep.sheets.CellLocation;
import sheep.sheets.Sheet;

import java.util.ArrayList;
import java.util.List;

/**
 * Calculates the evolution of cells in the game of life based on its rules.
 */
public class EvolutionCalculator implements Evolution {

    private final Sheet sheet;

    /**
     * Constructs an EvolutionCalculator with the given sheet.
     *
     * @param sheet The sheet representing the game board.
     * @requires sheet != null
     */
    public EvolutionCalculator(Sheet sheet) {
        this.sheet = sheet;
    }

    /**
     * Calculates the next generation of cells positions based on the current state of the sheet.
     *
     * @return A list of cell locations representing the evolved generation.
     */
    public List<CellLocation> evolvedGeneration() {
        List<CellLocation> evolvedGeneration = new ArrayList<>();

        // Iterate through each cell and find its neighbourhood weight
        for (int row = 0; row < sheet.getRows(); row++) {
            for (int col = 0; col < sheet.getColumns(); col++) {

                int summedWeight = calculateNeightbourWeight(row, col);
                boolean isAlive = sheet.valueAt(new CellLocation(row, col)).equals(new Constant(1));

                // Apply GOL rules
                if (shouldSurvive(isAlive, summedWeight)) {
                    evolvedGeneration.add(new CellLocation(row, col));
                }
            }
        }
        return evolvedGeneration;
    }

    /**
     * Calculates the weight of neighboring cells around the given cell location.
     *
     * @param row    The row index of the cell.
     * @param column The column index of the cell.
     * @return The total weight of neighboring cells.
     * @requires row != null, column != null;
     * @ensures neighbourhood weight of current location
     */
    private int calculateNeightbourWeight(int row, int column) {
        int totalWeight = 0;

        // Iterate through the cell's neighbours
        for (int surroundingRow = -1; surroundingRow <= 1; surroundingRow++) {
            for (int surroundingColumn = -1; surroundingColumn <= 1; surroundingColumn++) {

                // ignore current cell position (is not considered a part of the neighbourhood)
                if (surroundingRow == 0 && surroundingColumn == 0) {
                    continue;
                }

                // Obtain cellLocation of neighbour
                int neighbourRow = row + surroundingRow;
                int neighbourColumn = column + surroundingColumn;

                // Checks if neighbour is within Sheet and find value
                if (inBounds(neighbourRow, neighbourColumn)
                        && sheet.valueAt(new CellLocation(neighbourRow, neighbourColumn))
                        .equals(new Constant(1))) {
                    totalWeight++;
                }
            }
        }
        return totalWeight;
    }

    /**
     * Checks if a cell location is within the bounds of the sheet.
     * Handles cases when cellLocation is at sheet edges, such that
     * neighbours are outside scope.
     *
     * @param row The row index of the cell.
     * @param col The column index of the cell.
     * @return True if the cell location is within bounds, false otherwise.
     * @requires row != null, col != null;
     */
    private boolean inBounds(int row, int col) {
        return row >= 0 && row < sheet.getRows() && col >= 0 && col < sheet.getColumns();
    }

    /**
     * Determines if a cell should survive based on the given conditions.
     * if current cell is '1', neighbour weight must be 2 or 3 to survive
     * if current cell is '0', neighbour weight must be 3 to become alive
     *
     * @param isAlive       Whether the cell is currently alive.
     * @param neighborCount The count of neighboring cells.
     * @return True if the cell should survive, false otherwise.
     * @requires isAlive != null, neighborCount != null;
     */
    private boolean shouldSurvive(boolean isAlive, int neighborCount) {
        if (isAlive) {
            return neighborCount == 2 || neighborCount == 3;
        } else {
            return neighborCount == 3;
        }
    }
}
