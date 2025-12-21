package View_Layer;

import Controller_Layer.Board.BoardUtility;
import Controller_Layer.ControllerFacade;
import Controller_Layer.Game;
import Controller_Layer.GameManager;
import Controller_Layer.Catalog;
import Controller_Layer.CreatingBoards.DifficultyEnum;
import Facade_Interfaces.Controllable;
import Facade_Interfaces.Viewable;
import gameExceptions.*;

import java.io.IOException;

/**
 * FACADE ADAPTER - Adapter Design Pattern
 * Bridges between View's Controllable interface and Controller's Viewable interface.
 * Converts data types and formats between the two layers.
 */
public class ViewFacade implements Controllable {

    private final Viewable controller;

    /**
     * Constructor with dependency injection.
     */
    public ViewFacade(Viewable controller) {
        this.controller = controller;
    }

    /**
     * Default constructor - creates ControllerFacade.
     */
    public ViewFacade() {
        this.controller = new ControllerFacade();
    }

    @Override
    public boolean[] getCatalog() {
        Catalog catalog = controller.getCatalog();
        return new boolean[] { catalog.hasCurrent(), catalog.hasAllModesExist() };
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
        // Use the new getValidityMap method from Viewable interface
        return controller.getValidityMap(gameObj);
    }

    @Override
    public int[][] solveGame(int[][] game) throws InvalidGame {
        Game gameObj = new Game(game);
        // Use the new getSolvedBoard method from Viewable interface
        return controller.getSolvedBoard(gameObj);
    }

    @Override
    public void logUserAction(UserAction userAction) throws IOException {
        String actionString = userAction.toString();
        controller.logUserAction(actionString);
    }

    /**
     * Helper method to convert char difficulty to DifficultyEnum.
     */
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
}