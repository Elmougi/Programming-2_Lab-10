package Controller_Layer.Board;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.Scanner;

public class BoardUtility
{
    public static int[][] readBoard(String filePath) {
        int[][] grid = new int[9][9];

        try (Scanner scanner = new Scanner(new File(filePath))) {
            int row = 0;

            while (scanner.hasNextLine() && row < 9) {
                String line = scanner.nextLine().trim();

                if (line.isEmpty()) {
                    continue; // in case there is an extra newline
                }

                String[] values = line.split(",");

                if (values.length != 9) {
                    System.out.println("Error: Row " + (row + 1) + " must contain exactly 9 values");
                    return null;
                }

                for (int col = 0; col < 9; col++) {
                    try {
                        String value = values[col].trim();

                        if (value.isEmpty() || value.equals(" ") || value.equals("0")) {
                            grid[row][col] = 0; // Empty cell
                        } else {
                            grid[row][col] = Integer.parseInt(value);

                            if (grid[row][col] < 0 || grid[row][col] > 9) {
                                System.out.println("Error: Value at [" + row + "," + col + "] must be between 0-9");
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
