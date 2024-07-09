//package sheep.games.tetros;
//
//import sheep.sheets.CellLocation;
//
//import java.util.ArrayList;
//import java.util.List;
//
//public class TetrosDrop {
//    public boolean dropTile(List<CellLocation> tetrosCells, Sheet sheet) {
//        // Calculate the dropped cells positions
//        List<CellLocation> droppedCells = calculateDroppedCells();
//        removeCells(tetrosCells);
//        if (isStopper(droppedCells)) {
//            renderCells(tetrosCells);
//            return true;
//        }
//
//        // Render the dropped cells
//        renderCells(droppedCells);
//        this.tetrosCells = droppedCells;
//        return false;
//    }
//
//    public void fullDrop(List<CellLocation> tetrosCells, Sheet sheet) {
//        while (!dropTile(tetrosCells, sheet)) {
//            // keep dropping until dropTile method is true
//        }
//    }
//}
