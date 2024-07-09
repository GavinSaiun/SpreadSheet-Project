package sheep.features.files;

import sheep.sheets.Sheet;
import sheep.ui.Perform;
import sheep.ui.Prompt;
import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Optional;

/**
 * Represents a file saving operation.
 */
public class Save implements Perform {
    private final Sheet sheet;

    /**
     * Constructs a Save operation.
     *
     * @param sheet The sheet to save data from.
     */
    public Save(Sheet sheet) {
        this.sheet = sheet;
    }

    /**
     * Performs the file saving operation. When activated,
     * asks User for a desired file Path to save in.
     *
     * @param row     The row index to start saving from.
     * @param column  The column index to start saving from.
     * @param prompt  The prompt for user interaction.
     */
    @Override
    public void perform(int row, int column, Prompt prompt) {
        Optional<String> fileName = prompt.ask("Enter a file directory to Save In");
        Path filePath = Paths.get(fileName.get());
        saveData(filePath, prompt);
    }

    /**
     * Saves the sheet data to the specified file path.
     *
     * @param filePath The path to save the sheet data to.
     * @param prompt   The prompt for user interaction.
     * @requires filePath != null prompt != null;
     * @ensures specified file is saved into user specified file
     */
    private void saveData(Path filePath, Prompt prompt) {
        try (BufferedWriter writer = new BufferedWriter(new FileWriter(String.valueOf(filePath)))) {

            // Encode Sheet into a string format and write to new file
            String encodedSheet = sheet.encode();
            writer.write(encodedSheet);
        } catch (IOException e) {
            prompt.message("Error Saving Sheet" + e.getMessage());
        }
    }
}
