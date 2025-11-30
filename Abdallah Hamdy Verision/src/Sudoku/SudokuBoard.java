package Sudoku;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;

public class SudokuBoard {

    private int[][] board; 

    public SudokuBoard(String csvFilePath) throws IOException {
        board = new int[9][9];
        loadBoard(csvFilePath);
    }

    private void loadBoard(String csvFilePath) throws IOException {
        BufferedReader br = new BufferedReader(new FileReader(csvFilePath));
        String line;
        int row = 0;

        while ((line = br.readLine()) != null && row < 9) {
            String[] tokens = line.split(","); 
            for (int col = 0; col < 9; col++) {
                board[row][col] = Integer.parseInt(tokens[col].trim());
            }
            row++;
        }

        br.close();
    }

    public int[] getRow(int rowIndex) {
        return board[rowIndex].clone(); 
    }

    public int[] getColumn(int colIndex) {
        int[] col = new int[9];
        for (int i = 0; i < 9; i++) {
            col[i] = board[i][colIndex];
        }
        return col;
    }

    public int[] getBox(int boxIndex) {
        int[] box = new int[9];
        int startRow = (boxIndex / 3) * 3;
        int startCol = (boxIndex % 3) * 3;

        int k = 0;
        for (int i = startRow; i < startRow + 3; i++) {
            for (int j = startCol; j < startCol + 3; j++) {
                box[k++] = board[i][j];
            }
        }

        return box;
    }
}
