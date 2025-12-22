package Controller_Layer;

import Controller_Layer.CreatingBoards.DifficultyEnum;
import Controller_Layer.Logging.LogActions;
import Facade_Interfaces.Viewable;
import gameExceptions.InvalidGame;
import gameExceptions.NotFoundException;
import gameExceptions.SolutionInvalidException;

import java.io.IOException;

public class ControllerFacade implements Viewable {

    private final GameManager gameManager;
    private final GameVerifier gameVerifier;
    private LogActions logger;
    private Game currentGame;

    public ControllerFacade() {
        this.gameManager = new GameManager();
        this.gameVerifier = new GameVerifier();
        this.logger = new LogActions(currentGame);
    }

    @Override
    public Catalog getCatalog() {
        return gameManager.getCatalog();
    }

    @Override
    public Game getGame(DifficultyEnum level) throws NotFoundException {
        this.currentGame = gameManager.getGame(level);
        this.logger.setGame(this.currentGame);
        return this.currentGame;
    }

    public Game getIncompleteGame() throws NotFoundException {
        this.currentGame = gameManager.getIncompleteGame();
        this.logger.setGame(this.currentGame);
        return this.currentGame;
    }

    public Game getOriginalIncompleteGame() throws NotFoundException {
        return gameManager.getOriginalIncompleteGame();
    }

    @Override
    public void driveGames(Game sourceGame) throws SolutionInvalidException {
        gameManager.driveGames(sourceGame);
    }

    @Override
    public String verifyGame(Game game) {
        return gameVerifier.verifyGame(game);
    }

    @Override
    public int[] solveGame(Game game) throws InvalidGame {
        return gameVerifier.solveGame(game);
    }

    @Override
    public void logUserAction(String userAction) throws IOException {
        logger.doAction(userAction);
        if (currentGame != null) {
            gameManager.saveCurrentGame(currentGame);
        }
    }

    @Override
    public String undo() throws IOException {
        String action = logger.Undo();
        if (currentGame != null) {
            gameManager.saveCurrentGame(currentGame);
        }
        return action;
    }

    @Override
    public boolean[][] getValidityMap(Game game) {
        return gameVerifier.getValidityMap(game);
    }

    @Override
    public int[][] getSolvedBoard(Game game) throws InvalidGame {
        return gameVerifier.getSolvedBoard(game);
    }

    // Called when the user exits the game
    public void deleteCurrentGame() {
        gameManager.deleteCurrentGame(null);
        this.currentGame = null;
        this.logger.setGame(null);
    }


    public void gameSolved() {
        if (this.currentGame != null) {
            this.currentGame.setComplete(true);
            gameManager.deleteCurrentGame(this.currentGame);
            this.currentGame = null;
            this.logger.setGame(null);
        }
    }
}