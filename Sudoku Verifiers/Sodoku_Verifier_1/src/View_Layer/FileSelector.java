package View_Layer;

import javax.swing.*;
import java.io.File;

public class FileSelector {
    private JFrame parent;
    private ViewFacade viewFacade;

    public FileSelector(JFrame parent, ViewFacade viewFacade) {
        this.parent = parent;
        this.viewFacade = viewFacade;
    }

    public boolean selectAndGenerateGames() {
        JFileChooser fileChooser = new JFileChooser();
        fileChooser.setDialogTitle("Select Solved Sudoku File");
        fileChooser.setFileFilter(new javax.swing.filechooser.FileFilter() {
            @Override
            public boolean accept(File f) {
                return f.isDirectory() || f.getName().toLowerCase().endsWith(".csv");
            }

            @Override
            public String getDescription() {
                return "CSV Files (*.csv)";
            }
        });

        int result = fileChooser.showOpenDialog(parent);
        if (result == JFileChooser.APPROVE_OPTION) {
            File selectedFile = fileChooser.getSelectedFile();
            try {
                viewFacade.driveGames(selectedFile.getAbsolutePath());
                JOptionPane.showMessageDialog(parent,
                        "Games generated successfully! Please select difficulty.",
                        "Success",
                        JOptionPane.INFORMATION_MESSAGE);
                return true;
            } catch (Exception e) {
                JOptionPane.showMessageDialog(parent,
                        "Error generating games: " + e.getMessage(),
                        "Error",
                        JOptionPane.ERROR_MESSAGE);
                return false;
            }
        }
        return false;
    }

    public char selectDifficulty() {
        String[] options = {"Easy", "Medium", "Hard"};
        int choice = JOptionPane.showOptionDialog(parent,
                "Select difficulty:",
                "Difficulty Selection",
                JOptionPane.DEFAULT_OPTION,
                JOptionPane.QUESTION_MESSAGE,
                null,
                options,
                options[0]);

        if (choice == 0) return 'E';
        if (choice == 1) return 'M';
        if (choice == 2) return 'H';
        return 0;
    }
}