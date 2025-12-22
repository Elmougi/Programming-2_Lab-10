package View_Layer;

import javax.swing.*;
import java.awt.*;

public class SudokuGame extends JFrame {
    private JPanel mainPanel;
    private JPanel topPanel;
    private JLabel titleLabel;
    private JPanel infoPanel;
    private JLabel statusLabel;
    private JLabel emptyCellsLabel;
    private JPanel boardPanel;
    private JPanel controlPanel;
    private JButton verifyButton;
    private JButton solveButton;
    private JButton undoButton;
    private JButton newGameButton;

    private FacadeAdapter facadeAdapter;
    private BoardGrid boardGrid;
    private GameController gameController;
    private JDialog loadingDialog;

    public SudokuGame(FacadeAdapter facadeAdapter) {
        this.facadeAdapter = facadeAdapter;

        setTitle("Sudoku Game");
        setContentPane(mainPanel);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        initializeComponents();
        setupListeners();
        createLoadingDialog();

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
        gameController = new GameController(facadeAdapter, boardGrid, this);
    }


    private void createLoadingDialog() {
        loadingDialog = new JDialog(this, "Solver", true);
        JPanel panel = new JPanel(new BorderLayout());
        panel.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));

        JLabel messageLabel = new JLabel("Solver is running, please wait...", SwingConstants.CENTER);
        panel.add(messageLabel, BorderLayout.NORTH);

        JProgressBar progressBar = new JProgressBar();
        progressBar.setIndeterminate(true);
        panel.add(progressBar, BorderLayout.CENTER);

        loadingDialog.add(panel);
        loadingDialog.pack();
        loadingDialog.setLocationRelativeTo(this);
    }

    public void showLoading() {
        SwingUtilities.invokeLater(() -> loadingDialog.setVisible(true));
    }

    public void hideLoading() {
        if (loadingDialog != null) {
            loadingDialog.setVisible(false);
        }
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
        int choice = JOptionPane.showConfirmDialog(this, "You sure want to exit ?", "New Game", JOptionPane.YES_NO_OPTION);
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