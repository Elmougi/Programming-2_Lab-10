
import Board.CSVParser;
import Board.SgBoard;
import Validations.SgVerifier;
import Validations.VerifierFactory;

public class Main {

    public static void main(String[] args) {

        if (args.length != 2) {
            System.out.println("Usage: java -jar Sudoku.jar <csv-file> <mode>");
            return;
        }

        String filePath = args[0];
        int mode;

        try {
            mode = Integer.parseInt(args[1]);
        } catch (NumberFormatException e) {
            System.out.println("Mode must be a number: 0, 3 or 27");
            return;
        }

        // Load board
        int[][] boardArray = CSVParser.parse(filePath);
        SgBoard board = new SgBoard(boardArray);

        // Create verifier
        SgVerifier verifier = VerifierFactory.createVerifier(mode, board);

        // Run validation
        verifier.verify();

        // Print results
        verifier.printResults();
    }
}
