package Controller_Layer.SolveIntegerGame;

import Controller_Layer.Board.BoardIterator;

public class SolutionFlyweightContext implements Runnable { // trying a possible solution
    private final UnsolvedGameFlyweight game;
    private int[] valuesToTry = null;

    public SolutionFlyweightContext(UnsolvedGameFlyweight game, int[] valuesToTry) {
        this.game = game;
        setValuesToTry(valuesToTry);
    }

    public SolutionFlyweightContext(UnsolvedGameFlyweight game) {
        this.game = game;
    }

    public void setValuesToTry(int[] valuesToTry) {
        if (valuesToTry.length != game.missingCount) {
            throw new IllegalArgumentException("Values to try length must match missing count");
        }

        this.valuesToTry = valuesToTry;
    }

    @Override
    public void run() {
        if (valuesToTry == null) {
            throw new IllegalStateException("Values to try not set");
        }

        if(game.isSolved()) {
            return; // another thread found the solution
        }

        game.updateSolveStatus(solveBoard());
    }

    public int[] solveBoard() { // if true, the board in Unsolved game is now solved
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
                return null;

            valid = verifyList(colIter, colIndex, valuesToTry[i]);
            if (!valid)
                return null;

            valid = verifyList(boxIter, boxIndex, valuesToTry[i]);
            if (!valid)
                return null;
        }
        return valuesToTry;
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
}
