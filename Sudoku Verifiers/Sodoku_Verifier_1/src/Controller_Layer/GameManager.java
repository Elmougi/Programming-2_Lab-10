package Controller_Layer;

import Controller_Layer.Board.BoardUtility;
import Controller_Layer.Board.SodokuBoard;
import Controller_Layer.Checker.Result;
import Controller_Layer.Checker.SudokuIntVerifier;
import Controller_Layer.CreatingBoards.CreateBoardGames;
import Controller_Layer.CreatingBoards.DifficultyEnum;
import gameExceptions.NotFoundException;
import gameExceptions.SolutionInvalidException;

import java.io.File;
import java.util.Random;

/**
 * Responsible for game management operations.
 * Single Responsibility: Game loading, generation, and catalog management.
 */
public class GameManager {
    private static final String APPDATA_PATH = "AppData/";
    private static final String INCOMPLETE_FILE = "incomplete.csv";

    /**
     * Get catalog of available games.
     */
    public Catalog getCatalog() {
        boolean hasCurrent = checkIncompleteGameExists();
        boolean hasAllModes = checkAllDifficultiesExist();
        return new Catalog(hasCurrent, hasAllModes);
    }

    /**
     * Get a random game of specified difficulty.
     */
    public Game getGame(DifficultyEnum level) throws NotFoundException {
        String difficultyPath = APPDATA_PATH + level.name() + "/";
        File folder = new File(difficultyPath);

        if (!folder.exists() || !folder.isDirectory()) {
            throw new NotFoundException("No games found for difficulty: " + level.name());
        }

        File[] files = folder.listFiles((dir, name) -> name.endsWith(".csv"));
        if (files == null || files.length == 0) {
            throw new NotFoundException("No games found for difficulty: " + level.name());
        }

        // Pick random game
        Random rand = new Random();
        File gameFile = files[rand.nextInt(files.length)];

        int[][] board = BoardUtility.readBoard(gameFile.getPath());
        if (board == null) {
            throw new NotFoundException("Could not load game from: " + gameFile.getPath());
        }

        return new Game(board, level);
    }

    /**
     * Get the incomplete game if it exists.
     */
    public Game getIncompleteGame() throws NotFoundException {
        File incompleteFile = new File(APPDATA_PATH + INCOMPLETE_FILE);

        if (!incompleteFile.exists()) {
            throw new NotFoundException("No incomplete game found");
        }

        int[][] board = BoardUtility.readBoard(incompleteFile.getPath());
        if (board == null) {
            throw new NotFoundException("Could not load incomplete game");
        }

        return new Game(board);
    }

    /**
     * Generate games from a source solution.
     * REUSES: CreateBoardGames, SudokuIntVerifier
     */
    public void driveGames(Game sourceGame) throws SolutionInvalidException {
        // Verify source is valid using EXISTING SudokuIntVerifier
        SudokuIntVerifier verifier = new SudokuIntVerifier(sourceGame.board);
        Result<Integer> result = verifier.verify();

        if (!result.isValid()) {
            throw new SolutionInvalidException("Provided board is not a valid solution:\n" + result.toString());
        }

        // Convert to SodokuBoard for CreateBoardGames
        Integer[][] integerBoard = convertToIntegerArray(sourceGame.board);
        SodokuBoard<Integer> board = new SodokuBoard<>(9, integerBoard);

        // Generate using EXISTING CreateBoardGames
        String filename = "generated_" + System.currentTimeMillis() + ".csv";
        CreateBoardGames.creatingBoardGames(board, filename);
    }

    // Helper methods
    private boolean checkIncompleteGameExists() {
        File incompleteFile = new File(APPDATA_PATH + INCOMPLETE_FILE);
        return incompleteFile.exists();
    }

    private boolean checkAllDifficultiesExist() {
        for (DifficultyEnum diff : DifficultyEnum.values()) {
            String path = APPDATA_PATH + diff.name() + "/";
            File folder = new File(path);

            if (!folder.exists() || !folder.isDirectory()) {
                return false;
            }

            File[] files = folder.listFiles((dir, name) -> name.endsWith(".csv"));
            if (files == null || files.length == 0) {
                return false;
            }
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
}