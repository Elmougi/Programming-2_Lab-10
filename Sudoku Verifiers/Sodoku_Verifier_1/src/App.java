import java.io.File;
import java.io.FileNotFoundException;
import java.util.Scanner;

import Controller_Layer.Checker.SudokuIntVerifier;
import Controller_Layer.Checker.Result;

/*
compile all source files forming the .class instead of the .java
& "C:\Program Files\Java\jdk-25\bin\javac.exe" -d out -sourcepath src src\App.java

create manifest with main class for the .jar
'Main-Class: App' | Out-File -Encoding ASCII MANIFEST.MF

creating the .jar:
& "C:\Program Files\Java\jdk-25\bin\jar.exe" cfm app.jar MANIFEST.MF -C out .

-> I used the whole paths because my device did not read them easily on their own

summary:
& "C:\Program Files\Java\jdk-25\bin\javac.exe" -d out -sourcepath src src\App.java
'Main-Class: App' | Out-File -Encoding ASCII MANIFEST.MF
& "C:\Program Files\Java\jdk-25\bin\jar.exe" cfm app.jar MANIFEST.MF -C out .
java -jar app.jar <csv-file>
*/

public class App {
    public static void main(String[] args) {
        if (args.length != 1) {
            System.out.println("Missing input -> write: java -jar app.jar <csv-file>");
            return;
        }

        String csvFile = args[0];
        run(csvFile);
    }

    public static void run(String csvFile) {
        try {
            // Read board
            int[][] board = readBoard(csvFile);

            SudokuIntVerifier verifier = new SudokuIntVerifier(board);

            Result<Integer> result = verifier.verify();

            System.out.println(result);

        } catch (Exception e) {
            System.out.println("Error: " + e.getMessage());
        }
    }

    public static int[][] readBoard(String filePath) {
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
                            // System.out.println("Error: Value at [" + row + "," + col + "] must be between 0-9");
                            // return null;
                        } else {
                            grid[row][col] = Integer.parseInt(value);

                            if (grid[row][col] < 0 || grid[row][col] > 9) {
                                System.out.println("Error: Value at [" + row + "," + col + "] must be between 0-9");
                                return null;
                            }
                        }
                    } catch (NumberFormatException e) {
                        System.out
                                .println("Error: Invalid number format at row " + (row + 1) + ", column " + (col + 1));
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