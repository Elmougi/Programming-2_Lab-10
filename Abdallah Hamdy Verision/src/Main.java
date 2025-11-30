import Sudoku.SudokuBoard;
import Sudoku.SudokuValidator;

public class Main {
    public static void main(String[] args) {
        if (args.length != 2) {
            System.out.println("Usage: java -jar SudokuValidator.jar <csvFilePath> <mode>");
            System.out.println("Modes: 0 = sequential, 3 = 3 threads, 27 = 27 threads");
            return;
        }

        String csvFilePath = args[0];
        int mode;

        try {
            mode = Integer.parseInt(args[1]);
            if (mode != 0 && mode != 3 && mode != 27) {
                throw new IllegalArgumentException();
            }
        } catch (Exception e) {
            System.out.println("Invalid mode. Must be 0, 3, or 27.");
            return;
        }

        try {
            // Load board
            SudokuBoard board = new SudokuBoard(csvFilePath);

            // Validate
            SudokuValidator validator = new SudokuValidator(board);
            validator.validate(mode);

        } catch (Exception e) {
            System.out.println("Error: " + e.getMessage());
            e.printStackTrace();
        }
    }
}

