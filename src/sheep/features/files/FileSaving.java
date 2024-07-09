package sheep.features.files;

import sheep.features.Feature;
import sheep.sheets.Sheet;
import sheep.ui.UI;

/**
 * Represents a feature for saving files from a sheet.
 * Handles the UI component of fileSaving
 */
public class FileSaving implements Feature {
    private final Save save;

    /**
     * Constructs a FileSaving feature.
     * Calls Save Class to handle the Savin
     *
     * @param sheet The sheet to save files from.
     * @requires sheet != null
     */
    public FileSaving(Sheet sheet) {
        this.save = new Save(sheet);
    }

    /**
     * Creates a save-file UI application that calls upon the Save class
     *
     * @param ui The UI that enables the user to contact the Save class
     * @requires ui != null
     */
    @Override
    public void register(UI ui) {
        ui.addFeature("save-file", "Save Sheet", save);
    }
}
