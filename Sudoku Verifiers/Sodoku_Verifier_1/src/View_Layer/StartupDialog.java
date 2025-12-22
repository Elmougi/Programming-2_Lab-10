package View_Layer;

import javax.swing.*;
import java.awt.event.ActionListener;

public class StartupDialog extends JDialog {
    private JPanel mainPanel;
    private JLabel titleLabel; // Restored field
    private JLabel messageLabel;
    private JPanel buttonPanel;
    private JButton yesButton;
    private JButton noButton;
    private JButton okButton;
    private JPanel difficultyPanel;
    private JButton easyButton;
    private JButton mediumButton;
    private JButton hardButton;

    private FacadeAdapter facadeAdapter;
    private String selectedDifficulty = null;
    private boolean shouldLoadIncomplete = false;
    private boolean needsSourceFile = false;


    public StartupDialog(JFrame parent, FacadeAdapter facadeAdapter) {
        super(parent, "Sudoku Game Startup", true);
        this.facadeAdapter = facadeAdapter;

        setContentPane(mainPanel);
        setDefaultCloseOperation(DO_NOTHING_ON_CLOSE);
        pack();
        setLocationRelativeTo(parent);

        setupListeners();
        initializeGameFlow();
    }

    private void setupListeners() {
        yesButton.addActionListener(e -> {
            shouldLoadIncomplete = true;
            dispose();
        });
        noButton.addActionListener(e -> {
            shouldLoadIncomplete = false;
            checkDifficultiesAvailable();
        });
        okButton.addActionListener(e -> dispose());

        ActionListener diffListener = e -> {
            JButton source = (JButton) e.getSource();
            if (source == easyButton) selectedDifficulty = "E";
            else if (source == mediumButton) selectedDifficulty = "M";
            else if (source == hardButton) selectedDifficulty = "H";
            dispose();
        };

        easyButton.addActionListener(diffListener);
        mediumButton.addActionListener(diffListener);
        hardButton.addActionListener(diffListener);
    }

    private void initializeGameFlow() {
        try {
            boolean[] catalog = facadeAdapter.getCatalog();
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
            JOptionPane.showMessageDialog(this, "Error checking game catalog: " + e.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
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
            boolean[] catalog = facadeAdapter.getCatalog();
            if (catalog[1]) {
                showDifficultySelection();
            } else {
                showSourceFilePrompt();
            }
        } catch (Exception e) {
            e.printStackTrace();
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

    public String getSelectedDifficulty() { return selectedDifficulty; }
    public boolean shouldLoadIncomplete() { return shouldLoadIncomplete; }
    public boolean needsSourceFile() { return needsSourceFile; }
}