package Controller_Layer.SolveIntegerGame;

public class SolutionFlyweightContext implements Runnable {
    private final UnsolvedGameFlyweight game;
    private int[] valuesToTry = null;

    public SolutionFlyweightContext(UnsolvedGameFlyweight game, int[] valuesToTry) {
        this.game = game;
        setValuesToTry(valuesToTry);
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

        if (game.isSolved()) {
            return; // Stop if another thread already found the solution
        }

        int[] result = solveBoard();
        if (result != null) {
            game.updateSolveStatus(result); // Publish solution
        }
    }


    public int[] solveBoard() {
        Integer[][] board = game.board; // Access the board directly (Flyweight)

        for (int i = 0; i < game.missingCount; i++) {
            int r = game.rows.get(i);
            int c = game.cols.get(i);
            int val = valuesToTry[i];

            // Check Row Constraint
            if (!isValidInUnit(r, c, val, true, false)) return null;

            //  Check Column Constraint
            if (!isValidInUnit(r, c, val, false, true)) return null;

            // Check Box Constraint
            if (!isValidInBox(r, c, val)) return null;
        }

        return valuesToTry;
    }

    private boolean isValidInUnit(int r, int c, int val, boolean checkRow, boolean checkCol) {
        // Check against Fixed Board Values
        for (int k = 0; k < game.SIZE; k++) {
            // Determine coordinate based on mode (Row or Col iteration)
            int currRow = checkRow ? r : k;
            int currCol = checkCol ? c : k;

            Integer cellValue = game.board[currRow][currCol];
            // If cell is not empty (0) and matches our value, it's a conflict
            if (cellValue != null && cellValue != 0 && cellValue == val) {
                return false;
            }
        }

        // Check against other Transient Values (in the permutation)
        for (int j = 0; j < game.missingCount; j++) {
            // Determine if the j-th missing value is in the same unit
            boolean sameUnit = checkRow ? (game.rows.get(j) == r) : (game.cols.get(j) == c);

            if (sameUnit) {
                if (valuesToTry[j] == val && j != getCurrentIndexFor(r, c)) {
                    return false; // Conflict with another value in the solution attempt
                }
            }
        }
        return true;
    }

    private boolean isValidInBox(int r, int c, int val) {
        int boxRowStart = (r / 3) * 3;
        int boxColStart = (c / 3) * 3;


        for (int i = 0; i < 3; i++) {
            for (int j = 0; j < 3; j++) {
                Integer cellValue = game.board[boxRowStart + i][boxColStart + j];
                if (cellValue != null && cellValue != 0 && cellValue == val) {
                    return false;
                }
            }
        }


        for (int j = 0; j < game.missingCount; j++) {
            int otherR = game.rows.get(j);
            int otherC = game.cols.get(j);


            if ((otherR / 3) * 3 == boxRowStart && (otherC / 3) * 3 == boxColStart) {
                if (valuesToTry[j] == val && j != getCurrentIndexFor(r, c)) {
                    return false;
                }
            }
        }
        return true;
    }


    private int getCurrentIndexFor(int r, int c) {
        for (int i = 0; i < game.missingCount; i++) {
            if (game.rows.get(i) == r && game.cols.get(i) == c) {
                return i;
            }
        }
        return -1;
    }
}