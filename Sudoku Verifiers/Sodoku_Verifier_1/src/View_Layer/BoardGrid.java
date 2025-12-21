package View_Layer;

import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.io.IOException;


public class BoardGrid {
    private JPanel gridPanel;
    private JTextField[][] cells;
    private int[][] currentBoard;
    private int[][] originalBoard;
    private CellChangeListener changeListener;

    public interface CellChangeListener {
        void onCellChange(int row, int col, int newValue, int oldValue) throws IOException;
    }

    public BoardGrid() {
        cells = new JTextField[9][9];
        gridPanel = new JPanel(new GridLayout(9, 9, 1, 1));
        gridPanel.setBackground(Color.BLACK);
        gridPanel.setPreferredSize(new Dimension(500, 500));

        initializeCells();
    }

    private void initializeCells() {
        for (int i = 0; i < 9; i++) {
            for (int j = 0; j < 9; j++) {
                JTextField cell = new JTextField();
                cell.setHorizontalAlignment(JTextField.CENTER);
                cell.setFont(new Font("Arial", Font.BOLD, 24));

                // Add thicker borders for 3x3 boxes
                int top = (i % 3 == 0) ? 3 : 1;
                int left = (j % 3 == 0) ? 3 : 1;
                int bottom = (i == 8) ? 3 : 1;
                int right = (j == 8) ? 3 : 1;
                cell.setBorder(BorderFactory.createMatteBorder(top, left, bottom, right, Color.BLACK));

                final int row = i;
                final int col = j;


                cell.addKeyListener(new KeyAdapter() {
                    @Override
                    public void keyTyped(KeyEvent e) {
                        char c = e.getKeyChar();
                        if (c < '1' || c > '9') {
                            e.consume();
                        }
                        if (cell.getText().length() >= 1) {
                            e.consume();
                        }
                    }
                });


                cell.addFocusListener(new FocusAdapter() {
                    @Override
                    public void focusLost(FocusEvent e) {
                        try {
                            handleCellChange(row, col);
                        } catch (IOException ex) {
                            throw new RuntimeException(ex);
                        }
                    }
                });

                cells[i][j] = cell;
                gridPanel.add(cell);
            }
        }
    }

    private void handleCellChange(int row, int col) throws IOException {
        if (originalBoard == null || originalBoard[row][col] != 0) {
            return;
        }

        String text = cells[row][col].getText().trim();
        int newValue = text.isEmpty() ? 0 : Integer.parseInt(text);
        int oldValue = currentBoard[row][col];

        if (newValue != oldValue) {
            currentBoard[row][col] = newValue;

            if (changeListener != null) {
                changeListener.onCellChange(row, col, newValue, oldValue);
            }
        }
    }

    public void setBoard(int[][] board, int[][] original) {
        this.currentBoard = copyBoard(board);
        this.originalBoard = copyBoard(original);
        displayBoard();
    }

    public void displayBoard() {
        for (int i = 0; i < 9; i++) {
            for (int j = 0; j < 9; j++) {
                JTextField cell = cells[i][j];
                int value = currentBoard[i][j];

                if (originalBoard[i][j] == 0) {
                    // Editable cell
                    if (value == 0) {
                        cell.setText("");
                    } else {
                        cell.setText(String.valueOf(value));
                    }
                    cell.setEditable(true);
                    cell.setBackground(Color.WHITE);
                    cell.setForeground(new Color(0, 102, 204)); // Blue
                } else {
                    cell.setText(String.valueOf(value));
                    cell.setEditable(false);
                    cell.setBackground(new Color(240, 240, 240)); // Light gray
                    cell.setForeground(Color.BLACK);
                }
            }
        }
    }

    public void clearHighlighting() {
        for (int i = 0; i < 9; i++) {
            for (int j = 0; j < 9; j++) {
                if (originalBoard[i][j] == 0) {
                    cells[i][j].setBackground(Color.WHITE);
                    cells[i][j].setForeground(new Color(0, 102, 204));
                } else {
                    cells[i][j].setBackground(new Color(240, 240, 240));
                    cells[i][j].setForeground(Color.BLACK);
                }
            }
        }
    }

    public void highlightInvalidCells(boolean[][] validityMap) {
        for (int i = 0; i < 9; i++) {
            for (int j = 0; j < 9; j++) {
                if (!validityMap[i][j] && originalBoard[i][j] == 0) {
                    // Only highlight editable cells that are invalid
                    cells[i][j].setBackground(new Color(255, 200, 200)); // Light red
                }
            }
        }
    }

    public void updateCell(int row, int col, int value, Color color) {
        currentBoard[row][col] = value;
        if (value == 0) {
            cells[row][col].setText("");
        } else {
            cells[row][col].setText(String.valueOf(value));
        }
        if (color != null) {
            cells[row][col].setForeground(color);
        }
    }

    public int[][] getCurrentBoard() {
        updateBoardFromCells();
        return currentBoard;
    }

    private void updateBoardFromCells() {
        for (int i = 0; i < 9; i++) {
            for (int j = 0; j < 9; j++) {
                String text = cells[i][j].getText().trim();
                currentBoard[i][j] = text.isEmpty() ? 0 : Integer.parseInt(text);
            }
        }
    }

    public int getEmptyCellCount() {
        int count = 0;
        for (int i = 0; i < 9; i++) {
            for (int j = 0; j < 9; j++) {
                if (currentBoard[i][j] == 0) {
                    count++;
                }
            }
        }
        return count;
    }

    public void setCellChangeListener(CellChangeListener listener) {
        this.changeListener = listener;
    }

    public JPanel getPanel() {
        return gridPanel;
    }

    private int[][] copyBoard(int[][] board) {
        if (board == null) return null;
        int[][] copy = new int[9][9];
        for (int i = 0; i < 9; i++) {
            System.arraycopy(board[i], 0, copy[i], 0, 9);
        }
        return copy;
    }
}