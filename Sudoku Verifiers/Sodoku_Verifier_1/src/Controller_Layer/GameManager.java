package Controller_Layer;

import Controller_Layer.Board.BoardUtility;
import Controller_Layer.Board.SodokuBoard;
import Controller_Layer.Checker.Result;
import Controller_Layer.Checker.SudokuIntVerifier;
import Controller_Layer.CreatingBoards.CreateBoardGames;
import Controller_Layer.CreatingBoards.DifficultyEnum;
import gameExceptions.NotFoundException;
import gameExceptions.SolutionInvalidException;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.Random;

public class GameManager {
    private static final String APPDATA_PATH = "AppData/";
    private static final String CURRENT_PATH = "AppData/current/";
    private static final String INCOMPLETE_FILE = CURRENT_PATH + "incomplete.csv";
    private static final String LOG_FILE = CURRENT_PATH + "logs.txt";

    public Catalog getCatalog() {
        boolean hasCurrent = checkIncompleteGameExists();
        boolean hasAllModes = checkAllDifficultiesExist();
        return new Catalog(hasCurrent, hasAllModes);
    }

    public Game getGame(DifficultyEnum level) throws NotFoundException {
        // 1. Find a random game template
        String difficultyPath = APPDATA_PATH + level.name() + "/";
        File folder = new File(difficultyPath);

        if (!folder.exists() || !folder.isDirectory()) {
            throw new NotFoundException("No games found for difficulty: " + level.name());
        }

        File[] files = folder.listFiles((dir, name) -> name.endsWith(".csv"));
        if (files == null || files.length == 0) {
            throw new NotFoundException("No games found for difficulty: " + level.name());
        }

        Random rand = new Random();
        File gameFile = files[rand.nextInt(files.length)];

        int[][] board = BoardUtility.readBoard(gameFile.getPath());
        if (board == null) {
            throw new NotFoundException("Could not load game from: " + gameFile.getPath());
        }

        // 2. Setup "Incomplete" state
        // STRICT RULE: Folder must be empty or contain exactly two files.
        // We clean it first to ensure we are starting fresh.
        cleanCurrentFolder();

        try {
            // Create the Base Game File
            saveBoardToCsv(board, INCOMPLETE_FILE);
            // Create the Empty Log File
            new File(LOG_FILE).createNewFile();
        } catch (IOException e) {
            System.err.println("Warning: Could not save initial incomplete state: " + e.getMessage());
        }

        return new Game(board, level);
    }

    public Game getIncompleteGame() throws NotFoundException {
        File incompleteFile = new File(INCOMPLETE_FILE);
        if (!incompleteFile.exists()) {
            throw new NotFoundException("No incomplete game found");
        }

        // 1. Load the base board (the state at the start of the game)
        int[][] board = BoardUtility.readBoard(incompleteFile.getPath());
        if (board == null) {
            throw new NotFoundException("Could not load incomplete game");
        }

        // 2. Apply all moves from the log file to restore current state
        applyLogsToBoard(board);

        return new Game(board);
    }

    public Game getOriginalIncompleteGame() throws NotFoundException {
        File incompleteFile = new File(INCOMPLETE_FILE);
        if (!incompleteFile.exists()) throw new NotFoundException("No incomplete game found");
        int[][] board = BoardUtility.readBoard(incompleteFile.getPath());
        return new Game(board);
    }

    // Called whenever a move is made - We do NOT overwrite incomplete.csv
    // The state is persisted purely via the log file.
    public void saveCurrentGame(Game game) {
        // No-op for incomplete.csv to preserve the "Two Files" rule strictness.
        // Logs are written immediately by LogActions.
    }

    // Called when game is solved/finished
    public void deleteCurrentGame() {
        cleanCurrentFolder();
    }

    private void cleanCurrentFolder() {
        File folder = new File(CURRENT_PATH);
        if (folder.exists() && folder.isDirectory()) {
            File[] files = folder.listFiles();
            if (files != null) {
                for (File f : files) {
                    if (f.isFile()) {
                        f.delete();
                    }
                }
            }
        } else {
            folder.mkdirs();
        }
    }

    private void applyLogsToBoard(int[][] board) {
        File logFile = new File(LOG_FILE);
        if (!logFile.exists()) return;

        try (BufferedReader reader = new BufferedReader(new FileReader(logFile))) {
            String line;
            while ((line = reader.readLine()) != null) {
                // Format: x,y,val,prev
                if (line.trim().isEmpty()) continue;
                String[] parts = line.split(",");
                if (parts.length >= 3) {
                    try {
                        int row = Integer.parseInt(parts[0].trim());
                        int col = Integer.parseInt(parts[1].trim());
                        int val = Integer.parseInt(parts[2].trim());
                        board[row][col] = val;
                    } catch (NumberFormatException e) { /* ignore */ }
                }
            }
        } catch (IOException e) {
            System.err.println("Failed to read logs: " + e.getMessage());
        }
    }

    // ... (Other helper methods remain unchanged)
    public void driveGames(Game sourceGame) throws SolutionInvalidException {
        SudokuIntVerifier verifier = new SudokuIntVerifier(sourceGame.board);
        Result<Integer> result = verifier.verify();
        if (!result.isValid()) throw new SolutionInvalidException("Provided board is not a valid solution");

        Integer[][] integerBoard = convertToIntegerArray(sourceGame.board);
        SodokuBoard<Integer> board;
        try{
            board = new SodokuBoard<>(9, integerBoard);
        } catch (gameExceptions.InvalidGame e) {
            throw new SolutionInvalidException(e.getMessage());
        }
        String filename = "generated_" + System.currentTimeMillis() + ".csv";
        CreateBoardGames.creatingBoardGames(board, filename);
    }

    private boolean checkIncompleteGameExists() {
        return new File(INCOMPLETE_FILE).exists();
    }
    private boolean checkAllDifficultiesExist() {
        for (DifficultyEnum diff : DifficultyEnum.values()) {
            String path = APPDATA_PATH + diff.name() + "/";
            File folder = new File(path);
            if (!folder.exists() || !folder.isDirectory() || folder.listFiles((dir, name) -> name.endsWith(".csv")).length == 0) return false;
        }
        return true;
    }
    private Integer[][] convertToIntegerArray(int[][] board) {
        Integer[][] result = new Integer[board.length][board[0].length];
        for (int i = 0; i < board.length; i++) {
            for (int j = 0; j < board[i].length; j++) {
                result[i][j] = board[i][j];
            }
        }
        return result;
    }
    private void saveBoardToCsv(int[][] board, String path) throws IOException {
        StringBuilder sb = new StringBuilder();
        for (int[] row : board) {
            for (int i = 0; i < row.length; i++) {
                sb.append(row[i]);
                if (i < row.length - 1) sb.append(",");
            }
            sb.append("\n");
        }
        File file = new File(path);
        file.getParentFile().mkdirs();
        try (FileWriter writer = new FileWriter(file)) {
            writer.write(sb.toString());
        }
    }
}