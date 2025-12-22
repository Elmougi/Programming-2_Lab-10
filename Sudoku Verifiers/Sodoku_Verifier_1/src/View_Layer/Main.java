package View_Layer;

import javax.swing.*;
import java.io.File;

public class Main {

    public static void main(String[] args) {
        try {
            UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
        } catch (Exception e) {
            System.err.println("Could not set system look and feel: " + e.getMessage());
        }

        System.out.println("=== Sudoku Game Application ===");
        System.out.println("Starting application...");

        ensureDirectoryStructure();
        FacadeAdapter facadeAdapter = new FacadeAdapter();
        testBackendConnection(facadeAdapter);
        SwingUtilities.invokeLater(new Runnable() {
            @Override
            public void run() {
                try {
                    System.out.println("Launching GUI...");
                    SudokuGame game = new SudokuGame(facadeAdapter);
                    game.setVisible(true);
                    System.out.println("GUI launched successfully!");
                } catch (Exception e) {
                    System.err.println("Error launching GUI: " + e.getMessage());
                    e.printStackTrace();
                    showErrorAndExit(e);
                }
            }
        });
    }

    private static void ensureDirectoryStructure() {
        System.out.println("\nChecking directory structure...");
        String[] directories = {
                "AppData", "AppData/easy", "AppData/medium", "AppData/hard", "AppData/current"
        };

        for (String dir : directories) {
            File directory = new File(dir);
            if (!directory.exists()) {
                directory.mkdirs();
                System.out.println("Created directory: " + dir);
            }
        }
    }
    private static void testBackendConnection(FacadeAdapter facade) {
        System.out.println("\nTesting backend connection...");
        try {
            System.out.print("Testing getCatalog()... ");
            boolean[] catalog = facade.getCatalog();
            System.out.println("SUCCESS");
            System.out.println("  - Has incomplete game: " + catalog[0]);
            System.out.println("  - Has all difficulties: " + catalog[1]);
            System.out.println("\nBackend tests completed successfully!");
        } catch (Exception e) {
            System.err.println("FAILED");
            System.err.println("Backend test error: " + e.getMessage());
            e.printStackTrace();
        }
    }

    private static void showErrorAndExit(Exception e) {
        JOptionPane.showMessageDialog(null, "Failed to start Sudoku Game:\n\n" + e.getMessage(), "Startup Error", JOptionPane.ERROR_MESSAGE);
        System.exit(1);
    }
}