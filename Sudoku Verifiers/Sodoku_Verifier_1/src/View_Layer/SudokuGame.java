package View_Layer;

import gameExceptions.InvalidGame;
import gameExceptions.NotFoundException;

import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.io.IOException;


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

    private ViewFacade viewFacade;
    private BoardGrid boardGrid;
    private GameController gameController;

    public SudokuGame() {
        this.viewFacade = new ViewFacade();

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

    private void initializeComponents() {

        boardGrid = new BoardGrid();
        boardPanel.setLayout(new BorderLayout());
        boardPanel.add(boardGrid.getPanel(), BorderLayout.CENTER);


        gameController = new GameController(viewFacade, boardGrid, this);
    }

    private void setupListeners() {
        verifyButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                try {
                    gameController.verifyBoard();
                } catch (IOException ex) {
                    throw new RuntimeException(ex);
                }
            }
        });


        // not implemented yet
        solveButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                gameController.solveBoard();
            }
        });


       // perform undo is not implemented yet
        undoButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                gameController.performUndo();
            }
        });

        newGameButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                startNewGame();
            }
        });
    }

    private void startGame() {
        StartupDialog dialog = new StartupDialog(this, viewFacade);
        dialog.setVisible(true);

        try {
            if (dialog.shouldLoadIncomplete()) {
                gameController.loadGame('I');
            } else if (dialog.getSelectedDifficulty() != null) {
                gameController.loadGame(dialog.getSelectedDifficulty().charAt(0));
            } else if (dialog.needsSourceFile()) {
                FileSelector fileSelector = new FileSelector(this, viewFacade);
                if (fileSelector.selectAndGenerateGames()) {
                    char difficulty = fileSelector.selectDifficulty();
                    if (difficulty != 0) {
                        gameController.loadGame(difficulty);
                    }
                }
            }
        } catch (Exception e) {
            JOptionPane.showMessageDialog(this,
                    "Error starting game: " + e.getMessage(),
                    "Error",
                    JOptionPane.ERROR_MESSAGE);
        }
    }

    private void startNewGame() {
        int choice = JOptionPane.showConfirmDialog(this,
                "Start a new game? Current progress will be lost.",
                "New Game",
                JOptionPane.YES_NO_OPTION);

        if (choice == JOptionPane.YES_OPTION) {
            dispose();
            SwingUtilities.invokeLater(new Runnable() {
                @Override
                public void run() {
                    new SudokuGame().setVisible(true);
                }
            });
        }
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



    public static void main(String[] args) {
        try {
            UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
        } catch (Exception e) {
            e.printStackTrace();
        }

        SwingUtilities.invokeLater(new Runnable() {
            @Override
            public void run() {
                new SudokuGame().setVisible(true);
            }
        });
    }
}