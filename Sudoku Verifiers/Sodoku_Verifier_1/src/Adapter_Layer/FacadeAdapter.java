package Adapter_Layer;

import Controller_Layer.Board.BoardUtility;
import Controller_Layer.ControllerFacade;      // ✅ Use ControllerFacade instead
import Controller_Layer.Game;
import Controller_Layer.GameManager;
import Controller_Layer.Catalog;
import Controller_Layer.CreatingBoards.DifficultyEnum;
import Facade_Interfaces.Controllable;
import Facade_Interfaces.Viewable;
import View_Layer.UserAction;
import gameExceptions.*;

import java.io.IOException;

/**
 * FACADE ADAPTER - Adapter Design Pattern
 */
public class FacadeAdapter implements Controllable {

    private final Viewable controller;

    /**
     * Constructor with dependency injection.
     */
    public FacadeAdapter(Viewable controller) {
        this.controller = controller;
    }

    /**
     * Default constructor - creates ControllerFacade.
     * ✅ Now using ControllerFacade (the correct class name)
     */
    public FacadeAdapter() {
        this.controller = new ControllerFacade(); // ✅ Fixed!
    }

    @Override
    public boolean[] getCatalog() {
        Catalog catalog = controller.getCatalog();
        return new boolean[] {
                catalog.hasCurrent(),
                catalog.hasAllModesExist()
        };
    }

    @Override
    public int[][] getGame(char level) throws NotFoundException {
        if (level == 'I' || level == 'i') {
            GameManager gameManager = new GameManager();
            Game game = gameManager.getIncompleteGame();
            return game.board;
        }

        DifficultyEnum difficulty = convertCharToDifficulty(level);
        Game game = controller.getGame(difficulty);
        return game.board;
    }

    @Override
    public void driveGames(String sourcePath) throws SolutionInvalidException {
        int[][] board = BoardUtility.readBoard(sourcePath);
        if (board == null) {
            throw new SolutionInvalidException("Could not read board from: " + sourcePath);
        }

        Game sourceGame = new Game(board);
        controller.driveGames(sourceGame);
    }

    @Override
    public boolean[][] verifyGame(int[][] game) {
        Game gameObj = new Game(game);
        controller.verifyGame(gameObj);

        // ✅ Cast to ControllerFacade to access getValidityMap
        if (controller instanceof ControllerFacade) {
            return ((ControllerFacade) controller).getValidityMap(gameObj);
        }

        // Fallback
        boolean[][] validity = new boolean[9][9];
        for (int i = 0; i < 9; i++) {
            for (int j = 0; j < 9; j++) {
                validity[i][j] = true;
            }
        }
        return validity;
    }

    @Override
    public int[][] solveGame(int[][] game) throws InvalidGame {
        Game gameObj = new Game(game);
        int[] solutionEncoded = controller.solveGame(gameObj);
        return decodeSolution(solutionEncoded, game);
    }

    @Override
    public void logUserAction(UserAction userAction) throws IOException {
        String actionString = userAction.toString();
        controller.logUserAction(actionString);
    }

    // Helper methods
    private DifficultyEnum convertCharToDifficulty(char level) throws NotFoundException {
        switch (level) {
            case 'E':
            case 'e':
                return DifficultyEnum.easy;
            case 'M':
            case 'm':
                return DifficultyEnum.medium;
            case 'H':
            case 'h':
                return DifficultyEnum.hard;
            default:
                throw new NotFoundException("Invalid difficulty: " + level);
        }
    }

    private int[][] decodeSolution(int[] encoded, int[][] originalBoard) {
        int[][] solved = new int[9][9];
        for (int i = 0; i < 9; i++) {
            for (int j = 0; j < 9; j++) {
                solved[i][j] = originalBoard[i][j];
            }
        }
        for (int i = 0; i < encoded.length; i += 3) {
            solved[encoded[i]][encoded[i + 1]] = encoded[i + 2];
        }
        return solved;
    }
}