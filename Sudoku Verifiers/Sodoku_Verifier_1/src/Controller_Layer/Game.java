package Controller_Layer;

import Controller_Layer.CreatingBoards.DifficultyEnum;

public class Game {
    public int[][] board; // Public as per existing code style
    private DifficultyEnum difficulty;
    private boolean isComplete;

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
}