package sheep.features.files;

import sheep.features.Feature;
import sheep.sheets.Sheet;
import sheep.ui.UI;

/**
 * Represents a feature for loading files into a sheet.
 * Handles the UI component of fileLoading
 */
public class FileLoading implements Feature {
    private final Load load;

    /**
     * Constructs a FileLoading feature.
     * Calls upon Load class
     *
     * @param sheet The sheet to load files into.
     * @requires sheet != null
     */
    public FileLoading(Sheet sheet) {
        this.load = new Load(sheet);
    }

    /**
     * Creates a load-file UI application that calls upon the Load class
     *
     * @param ui The UI that enables the user to contact the load class
     * @requires ui != null
     */
    @Override
    public void register(UI ui) {
        ui.addFeature("load-file", "Load Sheet", load);

    }
}
