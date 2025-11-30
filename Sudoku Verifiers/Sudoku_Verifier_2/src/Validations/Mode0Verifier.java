
package Validations;

import Board.*;

public class Mode0Verifier extends SgVerifier {

    public Mode0Verifier(SgBoard board) {
        super(board);
    }

    @Override
    public void verify() {
        // Validate all rows
        for (int i = 0; i < 9; i++) {
            validateCells(board.getRow(i), "ROW", i + 1);
        }

        // Validate all columns
        for (int i = 0; i < 9; i++) {
            validateCells(board.getColumn(i), "COL", i + 1);
        }

        // Validate all boxes
        for (int i = 0; i < 9; i++) {
            validateCells(board.getBox(i), "BOX", i + 1);
        }
    }
}

