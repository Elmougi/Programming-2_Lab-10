package View_Layer;

import gameExceptions.SolutionInvalidException;
import javax.swing.*;
import java.io.File;

public class FileSelector {
    private JFrame parent;
    private FacadeAdapter facadeAdapter; // Reference

    public FileSelector(JFrame parent, FacadeAdapter facadeAdapter) {
        this.parent = parent;
        this.facadeAdapter = facadeAdapter;
    }

    public boolean selectAndGenerateGames() {
        JFileChooser fileChooser = new JFileChooser();
        fileChooser.setDialogTitle("Select Solved Sudoku CSV");

        int userSelection = fileChooser.showOpenDialog(parent);

        if (userSelection == JFileChooser.APPROVE_OPTION) {
            File fileToRead = fileChooser.getSelectedFile();
            try {
                facadeAdapter.driveGames(fileToRead.getAbsolutePath());
                JOptionPane.showMessageDialog(parent, "Games generated successfully!", "Success", JOptionPane.INFORMATION_MESSAGE);
                return true;
            } catch (SolutionInvalidException e) {
                JOptionPane.showMessageDialog(parent, "Invalid Solution File: " + e.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
            }
        }
        return false;
    }

    public char selectDifficulty() {
        String[] options = {"Easy", "Medium", "Hard"};
        int choice = JOptionPane.showOptionDialog(parent,
                "Select Difficulty to Play",
                "Difficulty Selection",
                JOptionPane.DEFAULT_OPTION,
                JOptionPane.QUESTION_MESSAGE,
                null, options, options[0]);

        if (choice == 0) return 'E';
        if (choice == 1) return 'M';
        if (choice == 2) return 'H';
        return 0;
    }
}