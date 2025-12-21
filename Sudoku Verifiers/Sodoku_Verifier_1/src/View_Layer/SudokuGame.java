package View_Layer;

import javax.swing.*;
import java.awt.*;
import java.awt.event.*;

public class SudokuGame extends JFrame {
    private JPanel mainPanel;
    private JPanel topPanel;       // Restored
    private JLabel titleLabel;     // Restored
    private JPanel infoPanel;      // Restored
    private JLabel statusLabel;
    private JLabel emptyCellsLabel;
    private JPanel boardPanel;
    private JPanel controlPanel;   // Restored
    private JButton verifyButton;
    private JButton solveButton;
    private JButton undoButton;
    private JButton newGameButton;

    private FacadeAdapter facadeAdapter;
    private BoardGrid boardGrid;
    private GameController gameController;

    // Constructor now accepts the shared FacadeAdapter
    public SudokuGame(FacadeAdapter facadeAdapter) {
        this.facadeAdapter = facadeAdapter;

        setTitle("Sudoku Game");
        setContentPane(mainPanel);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        initializeComponents();
        setupListeners();

        pack();
        setLocationRelativeTo(null);
        setResizable(false);

        startGame();
    }

    public SudokuGame() {
        this(new FacadeAdapter());
    }

    private void initializeComponents() {
        boardGrid = new BoardGrid();
        boardPanel.setLayout(new BorderLayout());
        boardPanel.add(boardGrid.getPanel(), BorderLayout.CENTER);

        // Pass the reference to GameController
        gameController = new GameController(facadeAdapter, boardGrid, this);
    }

    private void setupListeners() {
        verifyButton.addActionListener(e -> {
            try {
                gameController.verifyBoard();
            } catch (Exception ex) {
                throw new RuntimeException(ex);
            }
        });
        solveButton.addActionListener(e -> gameController.solveBoard());
        undoButton.addActionListener(e -> gameController.performUndo());
        newGameButton.addActionListener(e -> startNewGame());
    }

    private void startGame() {
        StartupDialog dialog = new StartupDialog(this, facadeAdapter);
        dialog.setVisible(true);

        try {
            if (dialog.shouldLoadIncomplete()) {
                gameController.loadGame('I');
            } else if (dialog.getSelectedDifficulty() != null) {
                gameController.loadGame(dialog.getSelectedDifficulty().charAt(0));
            } else if (dialog.needsSourceFile()) {
                FileSelector fileSelector = new FileSelector(this, facadeAdapter);
                if (fileSelector.selectAndGenerateGames()) {
                    char difficulty = fileSelector.selectDifficulty();
                    if (difficulty != 0) {
                        gameController.loadGame(difficulty);
                    }
                }
            }
        } catch (Exception e) {
            JOptionPane.showMessageDialog(this, "Error starting game: " + e.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
        }
    }

    private void startNewGame() {
        int choice = JOptionPane.showConfirmDialog(this, "Start a new game? Current progress will be lost.", "New Game", JOptionPane.YES_NO_OPTION);
        if (choice == JOptionPane.YES_OPTION) {
            restartGame();
        }
    }

    public void restartGame() {
        dispose();
        SwingUtilities.invokeLater(() -> new SudokuGame(this.facadeAdapter).setVisible(true));
    }

    public void updateStatus(String status) {
        statusLabel.setText("Status: " + status);
    }

    public void setSolveButtonEnabled(boolean enabled) {
        solveButton.setEnabled(enabled);
    }

    public void updateEmptyCells(int count) {
        emptyCellsLabel.setText("Empty cells: " + count);
    }
}