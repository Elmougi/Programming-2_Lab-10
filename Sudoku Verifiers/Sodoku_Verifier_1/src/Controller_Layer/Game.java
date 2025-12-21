package Controller_Layer;

import Controller_Layer.Board.Board;
import Controller_Layer.Board.BoardUtility;
import Controller_Layer.CreatingBoards.DifficultyEnum;
import Controller_Layer.Logging.LogActions;

public class Game {
    public int[][] board; // Public as per existing code style
    private DifficultyEnum difficulty;
    private boolean isComplete;

    private final String Game_File_PATH = "AppData/current/game.csv";

    public Game(int[][] board) {
        // IMPORTANT: DON'T COPY THE BOARD BY VALUE
        // USE REFERENCES
        this.board = board;
        this.difficulty = null;
        this.isComplete = false;
    }

    public Game(int[][] board, DifficultyEnum difficulty) {
        this.board = board;
        this.difficulty = difficulty;
        this.isComplete = false;
    }

    // Getters and Setters
    public DifficultyEnum getDifficulty() {
        return difficulty;
    }

    public void setDifficulty(DifficultyEnum difficulty) {
        this.difficulty = difficulty;
    }

    public boolean isComplete() {
        return isComplete;
    }

    public void setComplete(boolean complete) {
        isComplete = complete;
    }

    public int[][] getBoard() {
        return board;
    }

    public void addInput(int x, int y, int val) {
        this.board[x][y] = val;
        BoardUtility.saveBoard(board, Game_File_PATH);
    }

    public void endGame() {
        BoardUtility.deleteFile(LogActions.LOG_FILE_PATH);

    }
}