package Controller_Layer;

import Controller_Layer.Board.BoardUtility;
import Controller_Layer.CreatingBoards.DifficultyEnum;
import Controller_Layer.Logging.LogActions;

public class Game {
    public int[][] board;
    private DifficultyEnum difficulty;
    private boolean isComplete;
    private String sourceFilename;

    private final String CURRENT_FOLDER_PATH = "AppData/current/";

    public Game(int[][] board) {
        this.board = board;
        this.difficulty = null;
        this.isComplete = false;
    }

    public Game(int[][] board, DifficultyEnum difficulty) {
        this.board = board;
        this.difficulty = difficulty;
        this.isComplete = false;
    }


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

    public String getSourceFilename() {
        return sourceFilename;
    }

    public void setSourceFilename(String sourceFilename) {
        this.sourceFilename = sourceFilename;
    }

    public void addInput(int x, int y, int val) {
        this.board[x][y] = val;

        if (sourceFilename != null) {
            BoardUtility.saveBoard(board, CURRENT_FOLDER_PATH + sourceFilename);
        }
    }

    public void endGame() {
        BoardUtility.deleteFile(LogActions.LOG_FILE_PATH);
    }
}