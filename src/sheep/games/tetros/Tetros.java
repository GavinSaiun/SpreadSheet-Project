package sheep.games.tetros;

import sheep.expression.TypeError;
import sheep.expression.basic.Constant;
import sheep.expression.basic.Nothing;
import sheep.features.Feature;
import sheep.games.random.RandomTile;
import sheep.sheets.CellLocation;
import sheep.sheets.Sheet;
import sheep.ui.*;
import java.util.*;

/**
 * Represents a Tetris game.
 * This class implements the Tick and Feature interfaces.
 */
public class Tetros implements Tick, Feature {
    private final Sheet sheet;
    private boolean started = false;
    private int blockType = 1;
    private List<CellLocation> tetrosCells = new ArrayList<>();
    private final RandomTile randomTile;

    /**
     * Constructs a Tetris game with the given sheet and random tile generator.
     *
     * @param sheet       The sheet to render the Tetris game.
     * @param randomTile  The generator for random tiles.
     * @requires sheet != null, randomTIle != null
     */
    public Tetros(Sheet sheet, RandomTile randomTile) {
        this.sheet = sheet;
        this.randomTile = randomTile;
    }

    /**
     * Registers Tetris game features with the provided UI.
     * Handles user movement, start and ticks
     *
     * @param ui The UI to register features with.
     * @requires ui != null
     */
    @Override
    public void register(UI ui) {
        ui.onTick(this);

        // Get Move is fine as it calls Move, will change Later
        ui.addFeature("tetros", "Start Tetros", new GameStart());
        ui.onKey("a", "Move Left", new Move(-1));
        ui.onKey("d", "Move Right", new Move(1));
        ui.onKey("q", "Rotate Left", new Rotate(-1));
        ui.onKey("e", "Rotate Right", new Rotate(1));
        ui.onKey("s", "Drop", new Move(0));
    }

    /**
     * Handles tick events in the Tetris game.
     *
     * @param prompt The prompt to display messages.
     * @return True if the game is still running, false otherwise.
     */
    @Override
    public boolean onTick(Prompt prompt) {
        if (!started) {
            return false;
        }

        // Handles Game Over
        if (dropTile()) {
            if (initializeTetromino()) {
                prompt.message("Game Over!");
                started = false;
            }
        }

        // Checks if user has a full row, if so, clear it, else, continue
        lineClear();
        return true;
    }


    /**
     * Checks if the new shifted location is a stopper.
     * i.e. if the block has collided with another preplaced block
     *
     * @param locations The locations to check.
     * @return True if any of the locations are stoppers, false otherwise.
     * @requires locations != null
     */
    private boolean isStopper(List<CellLocation> locations) {
        for (CellLocation location : locations) {
            if (location.getRow() >= sheet.getRows()
                    || location.getColumn() >= sheet.getColumns()
                    || !sheet.valueAt(location.getRow(), location.getColumn())
                    .getContent().isEmpty()) {
                return true;
            }
        }
        return false;
    }

    /**
     * Checks if the new shifted location is within the bounds of the sheet.
     *
     * @param locations The locations to check.
     * @return True if all locations are within bounds, false otherwise.
     * @requires locations != null
     */
    private boolean inBounds(List<CellLocation> locations) {

        for (CellLocation location : locations.subList(3, locations.size())) {
            if (!sheet.contains(location)) {
                return false;
            }
        }
        return true;
    }

    /**
     * Calculates the cells positions after they are dropped by one row.
     *
     * @return The list of dropped cells positions.
     */
    private List<CellLocation> calculateDroppedCells() {
        List<CellLocation> droppedCells = new ArrayList<>();

        // Move each cell down one row
        for (CellLocation cell : tetrosCells) {
            droppedCells.add(new CellLocation(cell.getRow() + 1, cell.getColumn()));
        }
        return droppedCells;
    }

    /**
     * Drops the current tile down by one row. If the tile cannot be dropped further,
     * it renders the tile and checks for game over.
     *
     * @return True if the tile cannot be dropped further, indicating game over, otherwise false.
     */
    private boolean dropTile() {
        // Calculate the dropped cells positions
        List<CellLocation> droppedCells = calculateDroppedCells();
        removeCells(tetrosCells);
        if (isStopper(droppedCells)) {
            renderCells(tetrosCells);
            return true;
        }

        // Render the dropped cells
        renderCells(droppedCells);
        this.tetrosCells = droppedCells;
        return false;
    }

    /**
     * Continuously drops the current tile until it cannot be dropped further.
     */
    private void fullDrop() {
        while (!dropTile()) {
            // keep dropping until dropTile method is true
        }
    }

    /**
     * Moves the current tile horizontally by the specified direction.
     *
     * @param direction The direction in which to move the tile: -1 for left, 1 for right.
     * @requires direction != null
     */
    private void shift(int direction) {
        // Given that the user presses 's', call fullDrop()
        if (direction == 0) {
            fullDrop();
        } else {
            shiftTetrominoHorizontally(direction);
        }

    }

    /**
     * Moves the current tile horizontally by the specified direction.
     *
     * @param direction The direction in which to move the tile: -1 for left, 1 for right.
     * @requires direction != null
     */
    private void shiftTetrominoHorizontally(int direction) {
        List<CellLocation> shiftedCells = new ArrayList<>();

        // Moves each cell sideways by the specified direction
        for (CellLocation cell : tetrosCells) {
            int shiftedCell = cell.getColumn() + direction;

            // Checks if shifted cell is within Bounds (handles negative CellLocation)
            if (shiftedCell >= 0 && shiftedCell < sheet.getColumns()) {
                shiftedCells.add(new CellLocation(cell.getRow(), shiftedCell));
            } else {
                return;
            }
        }

        // Checks if shiftedCells are within Sheet bounds
        if (!inBounds(shiftedCells)) {
            return;
        }

        // Render shifted cells and Remove previous unshifted cell
        removeCells(tetrosCells);
        renderCells(shiftedCells);
        this.tetrosCells = shiftedCells;
    }

    /**
     * Removes the specified cells from the sheet.
     *
     * @param items The list of cell locations to remove.
     * @requires items != null
     * @ensures specified items becomes Nothing()
     */
    private void removeCells(List<CellLocation> items) {
        for (CellLocation cell : items) {
            try {
                sheet.update(cell, new Nothing());
            } catch (TypeError e) {
                throw new RuntimeException(e);
            }
        }
    }

    /**
     * Renders the specified cells on the sheet with the current block type.
     *
     * @param items The list of cell locations to render.
     * @requires items != null
     * @ensures specified items have value of their blockType
     */
    private void renderCells(List<CellLocation> items) {
        for (CellLocation cell : items) {
            try {
                sheet.update(cell, new Constant(blockType));
            } catch (TypeError e) {
                throw new RuntimeException(e);
            }
        }
    }

    /**
     * Initializes a new Tetromino on the sheet.
     *
     * @return True if the game is over (the Tetromino cannot be initialized), otherwise false.
     */
    private boolean initializeTetromino() {
        tetrosCells.clear();

        // adds a new tetros block
        addTetromino();
        for (CellLocation cell : tetrosCells) {
            if (!sheet.valueAt(cell).render().equals("")) {
                return true;
            }
        }
        renderCells(tetrosCells);

        return false;
    }

    /**
     * Adds a Tetromino to the TetrosCells list based on a random value.
     */
    private void addTetromino() {
        int randomValue = randomTile.pick();
        switch (randomValue) {
            case 1 -> addOrangeRickyShape();
            case 2 -> addHeroShape();
            case 3 -> addBlueRickyShape();
            case 4 -> addTeeweeShape();
            case 5 -> addClevelandShape();
            case 6 -> addSmashBoyShape();
            case 0 -> addRhodeIslandShape();
        }
    }

    /**
     * Adds an Orange Ricky Tetromino shape to the TetrosCells list.
     * The block type is set to 7.
     */
    private void addOrangeRickyShape() {
        tetrosCells.add(new CellLocation(0, 0));
        tetrosCells.add(new CellLocation(1, 0));
        tetrosCells.add(new CellLocation(2, 0));
        tetrosCells.add(new CellLocation(2, 1));
        blockType = 7;
    }

    /**
     * Adds a Hero Tetromino shape to the TetrosCells list.
     * The block type is set to 8.
     */
    private void addHeroShape() {
        tetrosCells.add(new CellLocation(0, 1));
        tetrosCells.add(new CellLocation(1, 1));
        tetrosCells.add(new CellLocation(2, 1));
        tetrosCells.add(new CellLocation(2, 0));
        blockType = 5;
    }

    /**
     * Adds a Blue Ricky Tetromino shape to the TetrosCells list.
     * The block type is set to 8.
     */
    private void addBlueRickyShape() {
        tetrosCells.add(new CellLocation(0, 0));
        tetrosCells.add(new CellLocation(0, 1));
        tetrosCells.add(new CellLocation(0, 2));
        tetrosCells.add(new CellLocation(1, 1));
        blockType = 8;
    }

    /**
     * Adds a Teewee Tetromino shape to the TetrosCells list.
     * The block type is set to 3.
     */
    private void addTeeweeShape() {
        tetrosCells.add(new CellLocation(0, 0));
        tetrosCells.add(new CellLocation(0, 1));
        tetrosCells.add(new CellLocation(1, 0));
        tetrosCells.add(new CellLocation(1, 1));
        blockType = 3;
    }

    /**
     * Adds a Cleveland Tetromino shape to the TetrosCells list.
     * The block type is set to 6.
     */
    private void addClevelandShape() {
        tetrosCells.add(new CellLocation(0, 0));
        tetrosCells.add(new CellLocation(1, 0));
        tetrosCells.add(new CellLocation(2, 0));
        tetrosCells.add(new CellLocation(3, 0));
        blockType = 6;
    }

    /**
     * Adds a Smash Boy Tetromino shape to the TetrosCells list.
     * The block type is set to 2.
     */
    private void addSmashBoyShape() {
        tetrosCells.add(new CellLocation(0, 1));
        tetrosCells.add(new CellLocation(0, 2));
        tetrosCells.add(new CellLocation(1, 1));
        tetrosCells.add(new CellLocation(0, 1));
        blockType = 2;
    }

    /**
     * Adds a Rhode Island Tetromino shape to the TetrosCells list.
     * The block type is set to 4.
     */
    private void addRhodeIslandShape() {
        tetrosCells.add(new CellLocation(0, 0));
        tetrosCells.add(new CellLocation(0, 1));
        tetrosCells.add(new CellLocation(1, 1));
        tetrosCells.add(new CellLocation(1, 2));
        blockType = 4;
    }

    /**
     * Calculates the average column index of the current Tetromino.
     *
     * @return The average column index.
     */
    private int calculateAverageColumn() {
        int averageColumn = 0;

        // Find each block cell's column, and find average
        for (CellLocation cellLocation : tetrosCells) {
            averageColumn += cellLocation.getColumn();
        }
        return averageColumn / tetrosCells.size();
    }

    /**
     * Calculates the average row index of the current Tetromino.
     *
     * @return The average row index.
     */
    private int calculateAverageRow() {
        int averageRow = 0;

        // Find each block cell's row, and find average
        for (CellLocation cellLocation : tetrosCells) {
            averageRow += cellLocation.getRow();
        }
        return averageRow / tetrosCells.size();
    }

    /**
     * Calculates the flipped positions of the current Tetromino.
     *
     * @param direction      The direction in which to flip: -1 for left, 1 for right.
     * @param averageRow     The average row index of the Tetromino.
     * @param averageColumn  The average column index of the Tetromino.
     * @return The list of flipped cell locations.
     * @requires direction != null, averageRow != null, averageColumn != null
     * @ensures new List containing cellLocations of their flipped positions
     */
    private List<CellLocation> calculateFlippedPositions(int direction,
                                                         int averageRow,
                                                         int averageColumn) {
        List<CellLocation> flippedPositions = new ArrayList<>();

        // Flip each Block's cell, and return new flipped location
        for (CellLocation cell : tetrosCells) {
            int flippedCellColumn = averageColumn + ((averageRow - cell.getRow()) * direction);
            int flippedCellRow = averageRow + ((averageColumn - cell.getColumn()) * direction);

            CellLocation flippedCell = new CellLocation(flippedCellRow, flippedCellColumn);
            flippedPositions.add(flippedCell);
        }
        return flippedPositions;
    }

    /**
     * Renders the current flipped Tetromino, checks if
     * new positions are valid.
     *
     * @param direction The direction in which to flip: -1 for left, 1 for right.
     * @requires direction != null
     */
    private void flip(int direction) {
        int averageColumn = calculateAverageColumn();
        int averageRow = calculateAverageRow();

        // Calculate new Flipped positions of tetros blocks
        List<CellLocation> flippedPositions = calculateFlippedPositions(direction,
                averageRow,
                averageColumn);

        if (!inBounds(flippedPositions)) {
            return;
        }

        // Renders new flipped positions
        removeCells(tetrosCells);
        tetrosCells = flippedPositions;
        renderCells(flippedPositions);
    }

    /**
     * Checks if the specified row on the sheet is full.
     *
     * @param row The row index to check.
     * @return True if the row is full, otherwise false.
     * @requires row != null
     */
    private boolean isRowFull(int row) {

        // Returns True if all cells in the row are not Null
        for (int col = 0; col < sheet.getColumns(); col++) {
            if (sheet.valueAt(row, col).getContent().equals("")) {
                return false;
            }
        }
        return true;
    }

    /**
     * Handles clearing a full row in sheet.
     * Updates sheet with new cleared row. and updates
     * rows above it appropriately.
     *
     * @param row The row index to clear.
     * @requires row != null
     * @ensures Sheet would clear the specified row
     */
    private void clearRow(int row) {

        // Iterate each row that is above the specified row
        for (int precedingRow = row; precedingRow > 0; precedingRow--) {
            for (int column = 0; column < sheet.getColumns(); column++) {
                try {
                    if (tetrosCells.contains(new CellLocation(precedingRow - 1, column))) {
                        continue;
                    }

                    // Update current row from the value of cells above
                    sheet.update(new CellLocation(precedingRow, column),
                            sheet.valueAt(new CellLocation(precedingRow - 1, column)));
                } catch (TypeError e) {
                    throw new RuntimeException(e);
                }
            }
        }
    }

    /**
     * Clears full rows on the sheet.
     */
    private void lineClear() {
        // Iterate each row and Check if they are full
        for (int row = sheet.getRows() - 1; row >= 0; row--) {

            // If full, clear it and bring down each row above down 1 row
            if (isRowFull(row)) {
                clearRow(row);
                row++;
            }
        }
    }

    /**
     * An implementation of the Perform interface for starting the Tetros game.
     * When performed, it sets the 'started' flag to true and initializes a new Tetromino.
     */
    private class GameStart implements Perform {

        /**
         * Performs the action of starting the Tetros game.
         *
         * @param row     The row coordinate of the action (unused).
         * @param column  The column coordinate of the action (unused).
         * @param prompt  The prompt interface used for displaying messages (unused).
         */
        @Override
        public void perform(int row, int column, Prompt prompt) {
            started = true;
            initializeTetromino();
        }
    }

    /**
     * An implementation of the Perform interface for handling Tetromino movements.
     * When performed, it shifts the current Tetromino horizontally based on the specified direction.
     */
    private class Move implements Perform {
        private final int direction;

        /**
         * Constructs a Move object with the specified direction.
         *
         * @param direction The direction in which to move the Tetromino: -1 for left, 1 for right.
         * @requires move != null
         */
        public Move(int direction) {
            this.direction = direction;
        }

        /**
         * Performs the action of moving the Tetromino horizontally.
         *
         * @param row     The row coordinate of the action (unused).
         * @param column  The column coordinate of the action (unused).
         * @param prompt  The prompt interface used for displaying messages (unused).
         */
        @Override
        public void perform(int row, int column, Prompt prompt) {
            if (started) {
                shift(direction);
            }

        }
    }

    /**
     * An implementation of the Perform interface for handling Tetromino rotations.
     * When performed, it rotates the current Tetromino based on the specified direction.
     */
    private class Rotate implements Perform {
        private final int direction;

        /**
         * Constructs a Rotate object with the specified direction.
         *
         * @param direction The direction in which to rotate the Tetromino: -1 for left, 1 for right.
         * @requires direction != null
         */
        public Rotate(int direction) {
            this.direction = direction;
        }

        /**
         * Performs the action of rotating the Tetromino.
         *
         * @param row     The row coordinate of the action (unused).
         * @param column  The column coordinate of the action (unused).
         * @param prompt  The prompt interface used for displaying messages (unused).
         */
        @Override
        public void perform(int row, int column, Prompt prompt) {
            if (started) {
                flip(direction);
            }

        }
    }
}