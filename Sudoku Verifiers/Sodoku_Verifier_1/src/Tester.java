import Controller_Layer.Board.SodokuBoard;
import Controller_Layer.CreatingBoards.CreateBoardGames;

public class Tester {
    public static void main(String[] args) {
        String csvFile = "test/valid1.csv";
        int[][] board = App.readBoard(csvFile);

        Integer[][] integerBoard = new Integer[board.length][board[0].length];

        for (int i = 0; i < board.length; i++) {
            for (int j = 0; j < board[i].length; j++) {
                integerBoard[i][j] = Integer.valueOf(board[i][j]);
            }
        }

        SodokuBoard<Integer> sodokuBoard = new SodokuBoard<>(9, integerBoard);

        CreateBoardGames.creatingBoardGames(sodokuBoard, "valid1.csv");
    }

}
