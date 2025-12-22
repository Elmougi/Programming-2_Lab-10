package View_Layer;

import gameExceptions.InvalidGame;
import gameExceptions.NotFoundException;

import javax.swing.*;
import java.awt.*;
import java.io.IOException;

public class GameController {
    private FacadeAdapter facadeAdapter;
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
            JOptionPane.showMessageDialog(gameWindow, "Board is valid so far., Keep Up the good work", "Valid", JOptionPane.INFORMATION_MESSAGE);
        } else {
            JOptionPane.showMessageDialog(gameWindow, "Invalid entries detected. Are highlighted in red", "Invalid", JOptionPane.WARNING_MESSAGE);
        }
    }


    public void solveBoard() {

        if (boardGrid.getEmptyCellCount() != 5) {
            JOptionPane.showMessageDialog(gameWindow, "Solver is only available when exactly 5 cells are remaining.", "Solver Unavailable", JOptionPane.WARNING_MESSAGE);
            return;
        }

        int[][] currentBoard = boardGrid.getCurrentBoard();
        boolean[][] validityMap = facadeAdapter.verifyGame(currentBoard);


        if (!checkAllValid(validityMap)) {
            boardGrid.clearHighlighting();
            boardGrid.highlightInvalidCells(validityMap);
            showUserWrongMessage();
            return;
        }


        SwingWorker<int[][], Void> worker = new SwingWorker<>() {
            @Override
            protected int[][] doInBackground() throws Exception {

                return facadeAdapter.solveGame(currentBoard);
            }

            @Override
            protected void done() {

                gameWindow.hideLoading();
                try {
                    int[][] solution = get();
                    if (solution != null) {
                        applySolutionToBoard(solution);
                        updateEmptyCellsDisplay();
                        updateSolveButtonState();

                        JOptionPane.showMessageDialog(gameWindow, "Puzzle solved! Next time try to solve it yourself :)", "Solution", JOptionPane.INFORMATION_MESSAGE);
                        try { verifyBoard(); } catch (IOException e) { e.printStackTrace(); }
                    } else {
                        showUserWrongMessage();
                    }
                } catch (Exception e) {

                    Throwable cause = e.getCause();
                    if (cause instanceof InvalidGame) {
                        showUserWrongMessage();
                    } else {
                        JOptionPane.showMessageDialog(gameWindow, "An error occurred: " + e.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
                        e.printStackTrace();
                    }
                }
            }
        };


        worker.execute();
        gameWindow.showLoading();
    }


    public void performUndo() {
        try {
            String undoneAction = facadeAdapter.undo();
            if (undoneAction != null) {
                String[] parts = undoneAction.split(",");
                if (parts.length >= 4) {
                    int row = Integer.parseInt(parts[0]);
                    int col = Integer.parseInt(parts[1]);
                    int prevVal = Integer.parseInt(parts[3]);

                    boardGrid.updateCell(row, col, prevVal, new Color(0, 102, 204));
                    updateEmptyCellsDisplay();
                    updateSolveButtonState();
                    gameWindow.updateStatus("Undo Performed");
                }
            } else {
                JOptionPane.showMessageDialog(gameWindow, "No more moves to undo!", "Undo", JOptionPane.INFORMATION_MESSAGE);
            }
        } catch (IOException | NumberFormatException e) {
            System.err.println("Error performing undo: " + e.getMessage());
        }
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

    private void applySolutionToBoard(int[][] solution) {
        for (int row = 0; row < 9; row++) {
            for (int col = 0; col < 9; col++) {
                if (originalBoard[row][col] == 0) {
                    boardGrid.updateCell(row, col, solution[row][col], new Color(0, 150, 0));
                }
            }
        }
    }

    private void showSuccessMessage() {
        JOptionPane.showMessageDialog(gameWindow, "Congratulations! You solved the puzzle correctly!", "Success", JOptionPane.INFORMATION_MESSAGE);
    }

    private void showUserWrongMessage() {
        JOptionPane.showMessageDialog(gameWindow, "The cells you entered were wrong.\nGo solve them correctly and come back to try the solver again.", "Incorrect Solution", JOptionPane.ERROR_MESSAGE);
    }

    private int[][] copyBoard(int[][] board) {
        int[][] copy = new int[9][9];
        for (int i = 0; i < 9; i++) {
            System.arraycopy(board[i], 0, copy[i], 0, 9);
        }
        return copy;
    }
}