//package sheep.games.tetros;
//
//import sheep.games.random.RandomTile;
//import sheep.sheets.CellLocation;
//
//import java.util.List;
//
//public class TetrosBlocks {
//    public void addTetromino(List<CellLocation> tetrosCells,
//    int blockType, RandomTile randomTile) {
//        int randomValue = randomTile.pick();
//        switch (randomValue) {
//            case 1 -> {
//                tetrosCells.add(new CellLocation(0, 0));
//                tetrosCells.add(new CellLocation(1, 0));
//                tetrosCells.add(new CellLocation(2, 0));
//                tetrosCells.add(new CellLocation(2, 1));
//                blockType = 7;
//            }
//            case 2 -> {
//                tetrosCells.add(new CellLocation(0, 1));
//                tetrosCells.add(new CellLocation(1, 1));
//                tetrosCells.add(new CellLocation(2, 1));
//                tetrosCells.add(new CellLocation(2, 0));
//                blockType = 5;
//            }
//            case 3 -> {
//                tetrosCells.add(new CellLocation(0, 0));
//                tetrosCells.add(new CellLocation(0, 1));
//                tetrosCells.add(new CellLocation(0, 2));
//                tetrosCells.add(new CellLocation(1, 1));
//                blockType = 8;
//            }
//            case 4 -> {
//                tetrosCells.add(new CellLocation(0, 0));
//                tetrosCells.add(new CellLocation(0, 1));
//                tetrosCells.add(new CellLocation(1, 0));
//                tetrosCells.add(new CellLocation(1, 1));
//                blockType = 3;
//            }
//            case 5 -> {
//                tetrosCells.add(new CellLocation(0, 0));
//                tetrosCells.add(new CellLocation(1, 0));
//                tetrosCells.add(new CellLocation(2, 0));
//                tetrosCells.add(new CellLocation(3, 0));
//                blockType = 6;
//            }
//            case 6 -> {
//                tetrosCells.add(new CellLocation(0, 1));
//                tetrosCells.add(new CellLocation(0, 2));
//                tetrosCells.add(new CellLocation(1, 1));
//                tetrosCells.add(new CellLocation(0, 1));
//                blockType = 2;
//            }
//            case 0 -> {
//                tetrosCells.add(new CellLocation(0, 0));
//                tetrosCells.add(new CellLocation(0, 1));
//                tetrosCells.add(new CellLocation(1, 1));
//                tetrosCells.add(new CellLocation(1, 2));
//                blockType = 4;
//            }
//        }
//    }
//}
