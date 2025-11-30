
package Verifier2.Board;

public class SgBoard {

    private Cell[][] board;
    private static final int SIZE = 9;

    public SgBoard(int[][] values) {
        if (values == null || values.length != SIZE) {
            throw new IllegalArgumentException("Board must be 9x9");
        }

        board = new Cell[SIZE][SIZE];

        for (int row = 0; row < SIZE; row++) {
            if (values[row].length != SIZE)
                throw new IllegalArgumentException("Each row must have 9 columns");
            for (int col = 0; col < SIZE; col++) {
                board[row][col] = new Cell(row, col, values[row][col]);
            }
        }
    }

    public Cell getCell(int row, int col) {
        validateIndex(row, "row");
        validateIndex(col, "column");
        return board[row][col];
    }

    public Cell[] getRow(int rowIndex) {
        validateIndex(rowIndex, "row");

        Cell[] row = new Cell[SIZE];
        for (int col = 0; col < SIZE; col++) {
            row[col] = board[rowIndex][col];
        }
        return row;
    }

    public Cell[] getColumn(int colIndex) {
        validateIndex(colIndex, "column");

        Cell[] column = new Cell[SIZE];
        for (int row = 0; row < SIZE; row++) {
            column[row] = board[row][colIndex];
        }
        return column;
    }

    public Cell[] getBox(int boxIndex) {
        if (boxIndex < 0 || boxIndex >= SIZE) {
            throw new IllegalArgumentException(
                    "Box index must be between 0 and 8, got: " + boxIndex
            );
        }

        Cell[] box = new Cell[SIZE];

        // Calculate starting position of the box
        int startRow = (boxIndex / 3) * 3;
        int startCol = (boxIndex % 3) * 3;

        int index = 0;
        for (int row = startRow; row < startRow + 3; row++) {
            for (int col = startCol; col < startCol + 3; col++) {
                box[index++] = board[row][col];
            }
        }

        return box;
    }

    public int getSize() {
        return SIZE;
    }

    /**
     * Validates that an index is within valid range (0-8).
     */
    private void validateIndex(int index, String indexType) {
        if (index < 0 || index >= SIZE) {
            throw new IllegalArgumentException(
                    indexType + " index must be between 0 and 8, got: " + index
            );
        }
    }


    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append("Sudoku Board:\n");

        for (int row = 0; row < SIZE; row++) {
            if (row % 3 == 0 && row != 0) {
                sb.append("------+-------+------\n");
            }

            for (int col = 0; col < SIZE; col++) {
                if (col % 3 == 0 && col != 0) {
                    sb.append("| ");
                }
                sb.append(board[row][col].getValue()).append(" ");
            }
            sb.append("\n");
        }

        return sb.toString();
    }


    public void print() {
        System.out.println(this.toString());
    }
}