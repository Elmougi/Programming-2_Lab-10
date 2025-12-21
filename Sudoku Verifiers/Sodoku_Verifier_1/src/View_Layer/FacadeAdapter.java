package View_Layer;

import Controller_Layer.Board.BoardUtility;
import Controller_Layer.ControllerFacade;
import Controller_Layer.Game;
import Controller_Layer.Catalog;
import Controller_Layer.CreatingBoards.DifficultyEnum;
import Facade_Interfaces.Controllable;
import Facade_Interfaces.Viewable;
import gameExceptions.*;

import java.io.IOException;

public class FacadeAdapter implements Controllable {

    private final ControllerFacade controller;

    // Allow passing an existing controller (Dependency Injection)
    public FacadeAdapter(Viewable controller) {
        if (controller instanceof ControllerFacade) {
            this.controller = (ControllerFacade) controller;
        } else {
            this.controller = new ControllerFacade();
        }
    }

    // Default constructor creates the single ControllerFacade instance
    public FacadeAdapter() {
        this.controller = new ControllerFacade();
    }

    @Override
    public boolean[] getCatalog() {
        Catalog catalog = controller.getCatalog();
        return new boolean[] { catalog.hasCurrent(), catalog.hasAllModesExist() };
    }

    @Override
    public int[][] getGame(char level) throws NotFoundException {
        // 'I' = Incomplete Game (Current State with User Input)
        if (level == 'I' || level == 'i') {
            Game game = controller.getIncompleteGame();
            return game.board;
        }

        // 'O' = Original Incomplete Game (Clean State with Zeros)
        if (level == 'O' || level == 'o') {
            Game game = controller.getOriginalIncompleteGame();
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
        return controller.getValidityMap(gameObj);
    }

    @Override
    public int[][] solveGame(int[][] game) throws InvalidGame {
        Game gameObj = new Game(game);
        return controller.getSolvedBoard(gameObj);
    }

    @Override
    public void logUserAction(UserAction userAction) throws IOException {
        String actionString = userAction.toString();
        controller.logUserAction(actionString);
    }

    @Override
    public String undo() throws IOException {
        return controller.undo();
    }

    public void gameSolved() {
        controller.deleteCurrentGame();
    }

    private DifficultyEnum convertCharToDifficulty(char level) throws NotFoundException {
        switch (level) {
            case 'E': case 'e': return DifficultyEnum.easy;
            case 'M': case 'm': return DifficultyEnum.medium;
            case 'H': case 'h': return DifficultyEnum.hard;
            default: throw new NotFoundException("Invalid difficulty: " + level);
        }
    }
}