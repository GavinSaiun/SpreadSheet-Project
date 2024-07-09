package sheep.features.files;

import sheep.sheets.Sheet;
import sheep.ui.Perform;
import sheep.ui.Prompt;
import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.Optional;

/**
 * Represents a file loading operation.
 */
public class Load implements Perform {
    private final Sheet sheet;

    /**
     * Constructs a Load operation.
     *
     * @param sheet The sheet to load data into.
     * @requires sheet != null
     */
    public Load(Sheet sheet) {
        this.sheet = sheet;
    }

    /**
     * Performs the file loading operation. Checks if File
     * Path exists, if so, loads file.
     *
     * @param row     The row index to start loading from.
     * @param column  The column index to start loading from.
     * @param prompt  The prompt for user interaction.
     */
    @Override
    public void perform(int row, int column, Prompt prompt) {

        // Clear Sheet for the load operation
        sheet.clear();
        sheet.updateDimensions(4, 4);

        // Asks User for their desire Path to Load
        Optional<String> fileName = prompt.ask("Enter a file directory to Load In");

        // Checks if File is Present
        if (!fileName.isPresent()) {
            prompt.message("file does not exist");
            return;
        }

        loadData(fileName.get(), prompt);

    }

    /**
     * Loads data from a file into the sheet. If error
     * occurs, flag IO exception.
     *
     * @param filePath The path to the file to be loaded.
     * @param prompt   The prompt for user interaction.
     * @requires filePath != null, prompt != null;
     * @ensures specified file is loaded into current instance of Sheet
     */
    private void loadData(String filePath, Prompt prompt) {
        try (BufferedReader reader = new BufferedReader(new FileReader(filePath))) {
            String line;
            int sheetRow = -1;

            // Read each line from the file
            while ((line = reader.readLine()) != null) {
                sheetRow++;

                // Split the line into individual cell values
                String[] cells = line.split("\\|");
                int sheetColumn = -1;

                // Update the sheet with cell values from the current line
                for (String cell : cells) {
                    sheetColumn++;
                    sheet.update(sheetRow, sheetColumn, cell);
                }
            }

        } catch (IOException e) {
            // Display error message if an IOException occurs during file reading
            prompt.message("Error loading sheet: " + e.getMessage());
        }
    }
}

