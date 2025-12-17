package Controller_Layer.Checker;

import java.util.List;

import Controller_Layer.Board.BoardIterator;
import Controller_Layer.Board.SodokuBoard;

public class BooleanMapAdapter<T> { // adapter does not extend another class because it is the unique output itself
    private SodokuBoard<T> board;
    private Result<T> result;

    public BooleanMapAdapter(SodokuBoard<T> board, Result<T> result) {
        this.board = board;
        this.result = result;
    }

    public boolean[][] resultMap() {
        boolean[][] map = new boolean[board.SIZE][board.SIZE];

        // initialize all to true
        for (int i = 0; i < board.SIZE; i++) {
            for (int j = 0; j < board.SIZE; j++) {
                map[i][j] = true;
            }
        }

        // no false
        if (result.isValid()) {
            return map;
        }

        // mark false for violations
        BoardIterator<T> rowIterator = board.rowIterator();
        BoardIterator<T> colIterator = board.columnIterator();
        BoardIterator<T> boxIterator = board.boxIterator();

        markFalse(map, rowIterator, result.rowViolations);
        markFalse(map, colIterator, result.colViolations);
        markFalse(map, boxIterator, result.boxViolations);

        return map;
    }

    private void markFalse(boolean[][] map, BoardIterator<T> iterator, List<Violation<T>> violations) {
        for (Violation<T> violation : violations) {
            T value = violation.getValue();
            int list = violation.getIndex();

            iterator.setToList(list);
            while (iterator.hasNextElement()) {
                T cellValue = iterator.nextElement();
                if (cellValue.equals(value)) {
                    int[] pos = iterator.currentElementIndex();
                    map[pos[0]][pos[1]] = false;
                    //System.out.println("Marking false at (" + pos[0] + ", " + pos[1] + ") for value " + value);
                }
            }
        }
    }

    @Override
    public String toString() { // for debugging and may help in future
        StringBuilder sb = new StringBuilder();
        boolean[][] map = resultMap();
        for (int i = 0; i < board.SIZE; i++) {
            for (int j = 0; j < board.SIZE; j++) {
                sb.append(map[i][j] ? "T" : "F").append(" ");
            }
            sb.append("\n");
        }
        return sb.toString();
    }

    // Debug
    public static void main(String[] args) {
        Integer[][] boardArray = {
            { 5,3,4,6,7,8,9,1,2 },
            { 6,7,2,1,9,5,3,4,8 },
            { 1,9,8,3,4,2,5,6,7 },
            { 8,5,9,7,6,1,4,2,3 },
            { 4,2,6,8,5,3,7,9,1 },
            { 7,1,3,9,2,4,8,5,6 },
            { 9,6,1,5,3,7,2,8,4 },
            { 2,8,7,4,1,5,6,3,9 },
            { 3,4,9,2,8,6,1,7,5 }
        };

        SodokuBoard<Integer> board = new SodokuBoard<>(9, boardArray);
        SudokuIntegerChecker verifier = new SudokuIntegerChecker(board);
        Result<Integer> result = verifier.getViolations();
        BooleanMapAdapter<Integer> adapter = new BooleanMapAdapter<>(board, result);
        System.out.println(result);
        System.out.println("---------------------\n---------------------\n");
        System.out.println(adapter);
    }
}
