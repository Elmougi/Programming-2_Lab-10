package View_Layer;

import gameExceptions.InvalidGame;
import gameExceptions.NotFoundException;

import javax.swing.*;
import java.awt.*;
import java.io.IOException;

public class GameController {
    private ViewFacade viewFacade;
    private BoardGrid boardGrid;
    private SudokuGame gameWindow;
    private int[][] originalBoard;

    public GameController(ViewFacade viewFacade, BoardGrid boardGrid, SudokuGame gameWindow) {
        this.viewFacade = viewFacade;
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
        int[][] board = viewFacade.getGame(difficulty);
        originalBoard = copyBoard(board);
        boardGrid.setBoard(board, originalBoard);
        updateEmptyCellsDisplay();
        gameWindow.updateStatus("Playing");


        UserAction action = new UserAction();
        viewFacade.logUserAction(action);
    }

    public void verifyBoard() throws IOException {
        int[][] currentBoard = boardGrid.getCurrentBoard();

        boolean[][] validityMap = viewFacade.verifyGame(currentBoard);

        boardGrid.clearHighlighting();
        boardGrid.highlightInvalidCells(validityMap);

        boolean allValid = checkAllValid(validityMap);
        int emptyCells = boardGrid.getEmptyCellCount();

        if (allValid && emptyCells == 0) {
            showSuccessMessage();
            gameWindow.updateStatus("Solved!");

            UserAction action = new UserAction().end();
            viewFacade.logUserAction(action);
        } else if (allValid) {
            JOptionPane.showMessageDialog(gameWindow,
                    "Board is valid so far. Keep going!",
                    "Valid",
                    JOptionPane.INFORMATION_MESSAGE);
        } else {
            JOptionPane.showMessageDialog(gameWindow,
                    "Invalid entries detected (highlighted in red).",
                    "Invalid",
                    JOptionPane.WARNING_MESSAGE);
        }
    }

    //TODO: To be implemented
    public void solveBoard() {
        try {
            int[][] currentBoard = boardGrid.getCurrentBoard();


            int[][] solution = viewFacade.solveGame(currentBoard);


            applySolutionToBoard(solution);

            updateEmptyCellsDisplay();
            JOptionPane.showMessageDialog(gameWindow,
                    "Puzzle solved!",
                    "Solution",
                    JOptionPane.INFORMATION_MESSAGE);

            gameWindow.updateStatus("Solved (Auto)");
        } catch (InvalidGame e) {
            JOptionPane.showMessageDialog(gameWindow,
                    "Cannot solve: " + e.getMessage(),
                    "Error",
                    JOptionPane.ERROR_MESSAGE);
        }
    }

    //TODO: To be implemented
    public void performUndo() {
        JOptionPane.showMessageDialog(gameWindow,
                "Undo feature to be implemented.",
                "Not Available",
                JOptionPane.INFORMATION_MESSAGE);

    }

    // ============== Helper Methods ==============

    private void logCellChange(int row, int col, int newValue, int oldValue) throws IOException {
        UserAction action = new UserAction().addValue(row, col, newValue, oldValue);
        viewFacade.logUserAction(action);
    }

    private void updateEmptyCellsDisplay() {
        int emptyCells = boardGrid.getEmptyCellCount();
        gameWindow.updateEmptyCells(emptyCells);
    }

    private void updateSolveButtonState() {
        int emptyCells = boardGrid.getEmptyCellCount();
        gameWindow.setSolveButtonEnabled(emptyCells == 5);
    }

    private boolean checkAllValid(boolean[][] validityMap) {
        for (int i = 0; i < 9; i++) {
            for (int j = 0; j < 9; j++) {
                if (!validityMap[i][j]) {
                    return false;
                }
            }
        }
        return true;
    }
    private void applySolutionToBoard(int[][] solution) {
        // Solution format from FacadeAdapter.decodeSolution:
        // Returns int[][] where each row is [row, col, value]
        for (int i = 0; i < solution.length; i++) {
            int row = solution[i][0];
            int col = solution[i][1];
            int value = solution[i][2];

            boardGrid.updateCell(row, col, value, new Color(0, 150, 0));
        }
    }

    private void showSuccessMessage() {
        JOptionPane.showMessageDialog(gameWindow,
                "Congratulations! You solved the puzzle correctly!",
                "Success",
                JOptionPane.INFORMATION_MESSAGE);
    }

    private int[][] copyBoard(int[][] board) {
        int[][] copy = new int[9][9];
        for (int i = 0; i < 9; i++) {
            System.arraycopy(board[i], 0, copy[i], 0, 9);
        }
        return copy;
    }
}