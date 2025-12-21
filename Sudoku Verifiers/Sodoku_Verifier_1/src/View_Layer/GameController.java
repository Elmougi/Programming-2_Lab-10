package View_Layer;

import gameExceptions.InvalidGame;
import gameExceptions.NotFoundException;

import javax.swing.*;
import java.awt.*;
import java.io.IOException;

public class GameController {
    private FacadeAdapter facadeAdapter; // Use Reference
    private BoardGrid boardGrid;
    private SudokuGame gameWindow;
    private int[][] originalBoard;

    public GameController(FacadeAdapter facadeAdapter, BoardGrid boardGrid, SudokuGame gameWindow) {
        this.facadeAdapter = facadeAdapter;
        this.boardGrid = boardGrid;
        this.gameWindow = gameWindow;

        boardGrid.setCellChangeListener(new BoardGrid.CellChangeListener() {
            @Override
            public void onCellChange(int row, int col, int newValue, int oldValue) throws IOException {
                logCellChange(row, col, newValue, oldValue);
                updateEmptyCellsDisplay();
                updateSolveButtonState();
            }
        });
    }

    public void loadGame(char difficulty) throws NotFoundException, IOException {
        int[][] currentBoard;
        if (difficulty == 'I' || difficulty == 'i') {
            currentBoard = facadeAdapter.getGame('I');
            originalBoard = facadeAdapter.getGame('O');
        } else {
            currentBoard = facadeAdapter.getGame(difficulty);
            originalBoard = copyBoard(currentBoard);
        }

        boardGrid.setBoard(currentBoard, originalBoard);
        updateEmptyCellsDisplay();
        updateSolveButtonState();
        gameWindow.updateStatus("Playing");
    }

    public void verifyBoard() throws IOException {
        int[][] currentBoard = boardGrid.getCurrentBoard();
        boolean[][] validityMap = facadeAdapter.verifyGame(currentBoard);

        boardGrid.clearHighlighting();
        boardGrid.highlightInvalidCells(validityMap);

        boolean allValid = checkAllValid(validityMap);
        int emptyCells = boardGrid.getEmptyCellCount();

        if (allValid && emptyCells == 0) {
            showSuccessMessage();
            facadeAdapter.gameSolved();
            gameWindow.restartGame();
        } else if (allValid) {
            JOptionPane.showMessageDialog(gameWindow, "Board is valid so far.", "Valid", JOptionPane.INFORMATION_MESSAGE);
        } else {
            JOptionPane.showMessageDialog(gameWindow, "Invalid entries detected.", "Invalid", JOptionPane.WARNING_MESSAGE);
        }
    }
// to be implemented
    public void solveBoard() {

    }
//to be implemented
    public void performUndo() {

    }

    private void logCellChange(int row, int col, int newValue, int oldValue) throws IOException {
        UserAction action = new UserAction().addValue(row, col, newValue, oldValue);
        facadeAdapter.logUserAction(action);
    }


    private void updateEmptyCellsDisplay() {
        gameWindow.updateEmptyCells(boardGrid.getEmptyCellCount());
    }

    private void updateSolveButtonState() {
        gameWindow.setSolveButtonEnabled(boardGrid.getEmptyCellCount() == 5);
    }

    private boolean checkAllValid(boolean[][] validityMap) {
        for (int i = 0; i < 9; i++) {
            for (int j = 0; j < 9; j++) {
                if (!validityMap[i][j]) return false;
            }
        }
        return true;
    }
// to be implemented
    private void applySolutionToBoard(int[][] solution) {

    }

    private void showSuccessMessage() {
        JOptionPane.showMessageDialog(gameWindow, "Congratulations! You solved the puzzle correctly!", "Success", JOptionPane.INFORMATION_MESSAGE);
    }

    private void showUserWrongMessage() {
        JOptionPane.showMessageDialog(gameWindow, "The solver has solved the five remaining cells, \nbut the cells you entered were wrong.\nGo solve them correctly.", "Incorrect Solution", JOptionPane.ERROR_MESSAGE);
    }

    private int[][] copyBoard(int[][] board) {
        int[][] copy = new int[9][9];
        for (int i = 0; i < 9; i++) {
            System.arraycopy(board[i], 0, copy[i], 0, 9);
        }
        return copy;
    }
}