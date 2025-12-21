package View_Layer;

import javax.swing.*;
import java.io.File;

public class Main {

    public static void main(String[] args) {
        // Set system look and feel for better UI
        try {
            UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
        } catch (Exception e) {
            System.err.println("Could not set system look and feel: " + e.getMessage());
        }


        System.out.println("=== Sudoku Game Application ===");
        System.out.println("Starting application...");


        ensureDirectoryStructure();


        testBackendConnection();


        SwingUtilities.invokeLater(new Runnable() {
            @Override
            public void run() {
                try {
                    System.out.println("Launching GUI...");
                    SudokuGame game = new SudokuGame();
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
                "AppData",
                "AppData/easy",
                "AppData/medium",
                "AppData/hard",
                "AppData/current"
        };

        for (String dir : directories) {
            File directory = new File(dir);
            if (!directory.exists()) {
                if (directory.mkdirs()) {
                    System.out.println("Created directory: " + dir);
                } else {
                    System.err.println("Failed to create directory: " + dir);
                }
            } else {
                System.out.println("Directory exists: " + dir);
            }
        }

        File logFile = new File("AppData/current/logs.txt");
        if (!logFile.exists()) {
            try {
                if (logFile.createNewFile()) {
                    System.out.println("Created log file: " + logFile.getPath());
                }
            } catch (Exception e) {
                System.err.println("Failed to create log file: " + e.getMessage());
            }
        } else {
            System.out.println("Log file exists: " + logFile.getPath());
        }
    }


    private static void testBackendConnection() {
        System.out.println("\nTesting backend connection...");

        try {
            ViewFacade viewFacade = new ViewFacade();


            System.out.print("Testing getCatalog()... ");
            boolean[] catalog = viewFacade.getCatalog();
            System.out.println("SUCCESS");
            System.out.println("  - Has incomplete game: " + catalog[0]);
            System.out.println("  - Has all difficulties: " + catalog[1]);


            System.out.print("Testing board verification... ");
            int[][] testBoard = createValidTestBoard();
            boolean[][] validity = viewFacade.verifyGame(testBoard);
            System.out.println("SUCCESS");


            System.out.print("Testing logging system... ");
            UserAction testAction = new UserAction();
            viewFacade.logUserAction(testAction);
            System.out.println("SUCCESS");

            System.out.println("\nBackend tests completed successfully!");

        } catch (Exception e) {
            System.err.println("FAILED");
            System.err.println("Backend test error: " + e.getMessage());
            e.printStackTrace();
        }
    }

    private static int[][] createValidTestBoard() {
        return new int[][] {
                {5, 3, 4, 6, 7, 8, 9, 1, 2},
                {6, 7, 2, 1, 9, 5, 3, 4, 8},
                {1, 9, 8, 3, 4, 2, 5, 6, 7},
                {8, 5, 9, 7, 6, 1, 4, 2, 3},
                {4, 2, 6, 8, 5, 3, 7, 9, 1},
                {7, 1, 3, 9, 2, 4, 8, 5, 6},
                {9, 6, 1, 5, 3, 7, 2, 8, 4},
                {2, 8, 7, 4, 1, 9, 6, 3, 5},
                {3, 4, 5, 2, 8, 6, 1, 7, 9}
        };
    }


    private static void showErrorAndExit(Exception e) {
        String message = "Failed to start Sudoku Game:\n\n" +
                e.getMessage() + "\n\n" +
                "Please check:\n" +
                "1. All required classes are present\n" +
                "2. AppData directory structure exists\n" +
                "3. No permission issues";

        JOptionPane.showMessageDialog(null,
                message,
                "Startup Error",
                JOptionPane.ERROR_MESSAGE);

        System.exit(1);
    }

    /**
     * Alternative main for testing specific components.
     */
    /*
    public static void testComponent(String component) {
        switch (component.toLowerCase()) {
            case "startup":
                testStartupDialog();
                break;
            case "board":
                testBoardGrid();
                break;
            case "backend":
                testBackendConnection();
                break;
            default:
                System.out.println("Unknown component: " + component);
                System.out.println("Available: startup, board, backend");
        }
    }*/
/*
    private static void testStartupDialog() {
        SwingUtilities.invokeLater(new Runnable() {
            @Override
            public void run() {
                JFrame dummy = new JFrame();
                ViewFacade facade = new ViewFacade();
                StartupDialog dialog = new StartupDialog(dummy, facade);
                dialog.setVisible(true);

                System.out.println("Selected difficulty: " + dialog.getSelectedDifficulty());
                System.out.println("Should load incomplete: " + dialog.shouldLoadIncomplete());
                System.out.println("Needs source file: " + dialog.needsSourceFile());
            }
        });
    }*/

/*
    private static void testBoardGrid() {
        SwingUtilities.invokeLater(new Runnable() {
            @Override
            public void run() {
                JFrame frame = new JFrame("Board Test");
                BoardGrid board = new BoardGrid();


                int[][] testBoard = createValidTestBoard();
                board.setBoard(testBoard, testBoard);

                frame.add(board.getPanel());
                frame.pack();
                frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
                frame.setLocationRelativeTo(null);
                frame.setVisible(true);

                System.out.println("Board test window opened");
            }
        });
    }*/
}