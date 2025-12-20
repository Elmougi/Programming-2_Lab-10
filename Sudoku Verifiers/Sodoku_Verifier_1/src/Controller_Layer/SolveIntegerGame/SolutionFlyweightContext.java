package Controller_Layer.SolveIntegerGame;

import Controller_Layer.Board.BoardIterator;

public class SolutionFlyweightContext { // trying a possible solution
    private UnsolvedGameFlyweight game;
    private int[] valuesToTry;

    public SolutionFlyweightContext(UnsolvedGameFlyweight game, int[] valuesToTry) {
        this.game = game;
        this.valuesToTry = valuesToTry;
    }

    public boolean solveBoard() { // if true, the board in Unsolved game is now solved
        // to decrease computation, I will just verify the affected rows, cols, boxes

        BoardIterator<Integer> colIter = game.columnIterator();
        BoardIterator<Integer> boxIter = game.boxIterator();
        BoardIterator<Integer> rowIter = game.rowIterator();
        boolean valid = true;

        for (int i = 0; i < game.missingCount; i++) {
            int rowIndex = game.rows.get(i);
            int colIndex = game.cols.get(i);
            int boxIndex = game.boxes.get(i);

            valid = verifyList(rowIter, rowIndex, valuesToTry[i]);
            if (!valid)
                return false;

            valid = verifyList(colIter, colIndex, valuesToTry[i]);
            if (!valid)
                return false;

            valid = verifyList(boxIter, boxIndex, valuesToTry[i]);
            if (!valid)
                return false;
        }
        applyValues();
        return true;
    }

    private boolean verifyList(BoardIterator<Integer> iterator, int index, int missingValue) {
        iterator.setToList(index);
        boolean[] seen = new boolean[game.SIZE + 1]; // assuming values are 1 to SIZE
        while (iterator.hasNextElement()) {
            Integer val = iterator.nextElement();
            if (val == 0) {
                val = missingValue;
            }
            if (seen[val]) {
                return false; // it was found before; hence, wrong list
            }
            seen[val] = true;
        }
        return true;
    }

    private void applyValues() {
        for (int i = 0; i < game.missingCount; i++) {
            game.board[game.rows.get(i)][game.cols.get(i)] = valuesToTry[i];
        }
    }
}
