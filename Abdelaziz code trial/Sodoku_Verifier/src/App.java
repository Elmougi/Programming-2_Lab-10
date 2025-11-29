import VerifyModes.VerifyFactory;
import VerifyModes.Verifier;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.Scanner;

import VerifyModes.Result;

public class App {
    public static void main(String[] args) {
        if (args.length != 2) {
            System.out.println("Missing input -> write: java -jar app.jar <csv-file> <mode>");
            return;
        }

        String csvFile = args[0];
        int mode = Integer.parseInt(args[1]);

        run(csvFile, mode);
    }

    public static void run(String csvFile, int mode) {
        try {
            // Read board
            int[][] board = readBoard(csvFile);

            // Create appropriate verifier using factory
            Verifier verifier = VerifyFactory.createVerifier(mode, board);

            // Perform verification
            Result result = verifier.verify();

            System.out.println(result);

        } catch (Exception e) {
            System.out.println("Error: " + e.getMessage());
        }
    }

    private static int[][] readBoard(String filePath) {
        int[][] grid = new int[9][9];
        
        try (Scanner scanner = new Scanner(new File(filePath))) {
            int row = 0;
            
            while (scanner.hasNextLine() && row < 9) {
                String line = scanner.nextLine().trim();
                
                if (line.isEmpty()) {
                    continue;
                } // incase there is an extra newline
                
                String[] values = line.split(",");
                
                if (values.length != 9) {
                    System.out.println("Error: Row " + (row + 1) + " must contain exactly 9 values");
                    return null;
                }
                
                for (int col = 0; col < 9; col++) {
                    try {
                        String value = values[col].trim();
                        
                        if (value.isEmpty() || value.equals(" ") || value.equals("0")) {
                            System.out.println("Error: Value at [" + row + "," + col + "] must be between 1-9");
                            return null;
                        } else {
                            grid[row][col] = Integer.parseInt(value);
                            
                            if (grid[row][col] < 1 || grid[row][col] > 9) {
                                System.out.println("Error: Value at [" + row + "," + col + "] must be between 1-9");
                                return null;
                            }
                        }
                    } catch (NumberFormatException e) {
                        System.out.println("Error: Invalid number format at row " + (row + 1) + ", column " + (col + 1));
                        return null;
                    }
                }
                row++;
            }
            
            // Check if we read exactly 9 rows
            if (row != 9) {
                System.out.println("Error: CSV file must contain exactly 9 rows for a Sudoku grid");
                return null;
            }
            
            return grid;
            
        } catch (FileNotFoundException e) {
            System.out.println("Error: File not found - " + filePath);
            return null;
        }
    }
}