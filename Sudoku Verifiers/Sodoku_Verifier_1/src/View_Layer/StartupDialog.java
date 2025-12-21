package View_Layer;

import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

/**
 * Startup dialog that handles initial game flow:
 * 1. Check for incomplete game
 * 2. Check for available difficulties
 * 3. Prompt for source solution if needed
 */
public class StartupDialog extends JDialog {
    private JPanel mainPanel;
    private JLabel titleLabel;
    private JLabel messageLabel;
    private JPanel buttonPanel;
    private JButton yesButton;
    private JButton noButton;
    private JButton okButton;
    private JPanel difficultyPanel;
    private JButton easyButton;
    private JButton mediumButton;
    private JButton hardButton;

    private ViewFacade viewFacade;
    private String selectedDifficulty = null;
    private boolean shouldLoadIncomplete = false;
    private boolean needsSourceFile = false;

    public StartupDialog(JFrame parent, ViewFacade viewFacade) {
        super(parent, "Sudoku Game Startup", true);
        this.viewFacade = viewFacade;

        setContentPane(mainPanel);
        setDefaultCloseOperation(DO_NOTHING_ON_CLOSE);
        pack();
        setLocationRelativeTo(parent);

        setupListeners();
        initializeGameFlow();
    }

    private void setupListeners() {
        yesButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                shouldLoadIncomplete = true;
                dispose();
            }
        });

        noButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                shouldLoadIncomplete = false;
                checkDifficultiesAvailable();
            }
        });

        okButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                dispose();
            }
        });

        easyButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                selectedDifficulty = "E";
                dispose();
            }
        });

        mediumButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                selectedDifficulty = "M";
                dispose();
            }
        });

        hardButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                selectedDifficulty = "H";
                dispose();
            }
        });
    }

    private void initializeGameFlow() {
        try {
            boolean[] catalog = viewFacade.getCatalog();
            boolean hasIncomplete = catalog[0];
            boolean hasAllDifficulties = catalog[1];

            if (hasIncomplete) {
                showIncompleteGamePrompt();
            } else if (hasAllDifficulties) {
                showDifficultySelection();
            } else {
                showSourceFilePrompt();
            }
        } catch (Exception e) {
            JOptionPane.showMessageDialog(this,
                    "Error checking game catalog: " + e.getMessage(),
                    "Error",
                    JOptionPane.ERROR_MESSAGE);
            dispose();
        }
    }

    private void showIncompleteGamePrompt() {
        messageLabel.setText("<html><center>You have an unfinished game.<br>Do you want to continue?</center></html>");
        yesButton.setVisible(true);
        noButton.setVisible(true);
        buttonPanel.setVisible(true);
    }

    private void checkDifficultiesAvailable() {
        try {
            boolean[] catalog = viewFacade.getCatalog();
            boolean hasAllDifficulties = catalog[1];

            if (hasAllDifficulties) {
                showDifficultySelection();
            } else {
                showSourceFilePrompt();
            }
        } catch (Exception e) {
            JOptionPane.showMessageDialog(this,
                    "Error checking difficulties: " + e.getMessage(),
                    "Error",
                    JOptionPane.ERROR_MESSAGE);
        }
    }

    private void showDifficultySelection() {
        messageLabel.setText("<html><center>Select difficulty level:</center></html>");
        buttonPanel.setVisible(false);
        difficultyPanel.setVisible(true);
    }

    private void showSourceFilePrompt() {
        needsSourceFile = true;
        messageLabel.setText("<html><center>No games available.<br>Please provide a solved Sudoku file.</center></html>");
        okButton.setVisible(true);
        buttonPanel.setVisible(true);
    }

    public String getSelectedDifficulty() {
        return selectedDifficulty;
    }

    public boolean shouldLoadIncomplete() {
        return shouldLoadIncomplete;
    }

    public boolean needsSourceFile() {
        return needsSourceFile;
    }
}