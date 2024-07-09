//package sheep.games.tetros;
//
//import sheep.sheets.CellLocation;
//import sheep.sheets.Sheet;
//
//import java.util.List;
//
//public class TetrosGameLogic {
//
//    private final Sheet sheet;
//    public TetrosGameLogic(Sheet sheet) {
//        this.sheet = sheet;
//    }
//    public boolean isStopper(List<CellLocation> locations) {
//        for (CellLocation location : locations) {
//            if (location.getRow() >= sheet.getRows()
//                    || location.getColumn() >= sheet.getColumns()
//                    || !sheet.valueAt(location.getRow(), location.getColumn())
//                    .getContent().isEmpty()) {
//                return true;
//            }
//        }
//        return false;
//    }
//
//    /**
//     * Checks if the new shifted location is within the bounds of the sheet.
//     *
//     * @param locations The locations to check.
//     * @return True if all locations are within bounds, false otherwise.
//     */
//    public boolean inBounds(List<CellLocation> locations) {
//
//        for (CellLocation location : locations.subList(3, locations.size())) {
//            if (!sheet.contains(location)) {
//                return false;
//            }
//        }
//        return true;
//    }
//}
