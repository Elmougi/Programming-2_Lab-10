package CreatingBoards;

import java.io.FileWriter;
import java.io.IOException;
import java.util.List;

import Board.SodokuBoard;

public class CreateBoardGames {
    private static final int BOARD_SIZE = 9;
    private static int serial = 0;
    private static final int MAX_GAMES_PER_DIFFICULTY = 100;

    public static void creatingBoardGames(SodokuBoard<Integer> board, String filename) {
        if (board.SIZE != BOARD_SIZE) {
            throw new IllegalArgumentException("Board must be of size " + BOARD_SIZE + "x" + BOARD_SIZE + ".");
        }

        for (DifficultyEnum difficulty : DifficultyEnum.values()) {
            int cellsToRemove = difficulty.getValue();
            // System.out.println("Creating board for difficulty: " + difficulty.name() + "
            // with " + cellsToRemove + " cells to remove.");

            RandomPairs randomPairs = new RandomPairs();
            List<int[]> pairs = randomPairs.generateDistinctPairs(cellsToRemove);
            // System.out.println("Generated " + pairs.size() + " distinct pairs for
            // removal.");

            // for (int[] pair : pairs) {
            // System.out.println("Removing cell at: (" + pair[0] + ", " + pair[1] + ") ");
            // // x = pair[0]
            // // y = pair[1]
            // board.board[pair[0]][pair[1]] = 0;
            // }
            // System.out.println("\nFinal board for difficulty " + difficulty.name() +
            // ":\n" + board);

            StringBuilder sb = new StringBuilder();
            boolean[] jTo0 = new boolean[9];
            for (int i = 0; i < BOARD_SIZE; i++) {
                for (int[] pair : pairs) {
                    if (pair[0] == i) {
                        jTo0[pair[1]] = true;
                    }
                }
                for (int j = 0; j < BOARD_SIZE; j++) {
                    if (jTo0[j]) {
                        sb.append("0,");
                        jTo0[j] = false;
                    } else {
                        sb.append(board.board[i][j]).append(",");
                    }

                }
                sb.append("\n");
            }

            try (FileWriter writer = new FileWriter("AppData/" + difficulty.name() + "/" + filename)) {
                writer.write(sb.toString());
            } catch (IOException e) {
                System.out.println("An error occurred while writing the board to file: " + e.getMessage());
            }
            if (serial >= MAX_GAMES_PER_DIFFICULTY) {
                throw new IllegalStateException("Maximum number of games per difficulty reached.");
            }
        }
        serial++;
    }
}