import Controller_Layer.Checker.Result;
import Controller_Layer.Checker.SudokuIntVerifier;
import Controller_Layer.SolveIntegerGame.UnsolvedGameFlyweight;
import gameExceptions.InvalidGame;

public class Tester {
    public static void main(String[] args) throws InvalidGame {
        // String csvFile = "test/valid1.csv";
        // int[][] board = App.readBoard(csvFile);

        // Integer[][] integerBoard = new Integer[board.length][board[0].length];

        // for (int i = 0; i < board.length; i++) {
        // for (int j = 0; j < board[i].length; j++) {
        // integerBoard[i][j] = Integer.valueOf(board[i][j]);
        // }
        // }

        // SodokuBoard<Integer> sodokuBoard = new SodokuBoard<>(9, integerBoard);

        // CreateBoardGames.creatingBoardGames(sodokuBoard, "valid1.csv");

        // test solve funtionality
        String csvFile = "test/incomplete1.csv";
        int[][] board = App.readBoard(csvFile);
        Integer[][] integerBoard = new Integer[board.length][board[0].length];
        for (int i = 0; i < board.length; i++) {
            for (int j = 0; j < board[i].length; j++) {
                integerBoard[i][j] = Integer.valueOf(board[i][j]);
            }
        }

        UnsolvedGameFlyweight unsolvedGame = new UnsolvedGameFlyweight(9, integerBoard);
        int[][] solvedBoard = unsolvedGame.solve();
        for (int i = 0; i < solvedBoard.length; i++) {
            for (int j = 0; j < solvedBoard[i].length; j++) {
                System.out.print(solvedBoard[i][j] + " ");
            }
            System.out.println();
        }

        SudokuIntVerifier verifier = new SudokuIntVerifier(solvedBoard);
        Result<Integer> result = verifier.verify();
        System.out.println(result);
    }

}
