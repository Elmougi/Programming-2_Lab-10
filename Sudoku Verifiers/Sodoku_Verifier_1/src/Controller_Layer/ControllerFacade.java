package Controller_Layer;

import Controller_Layer.CreatingBoards.DifficultyEnum;
import Controller_Layer.Logging.LogActions;
import Facade_Interfaces.Viewable;
import gameExceptions.InvalidGame;
import gameExceptions.NotFoundException;
import gameExceptions.SolutionInvalidException;

import java.io.FileWriter;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * Main Controller Facade implementing Viewable interface.
 * FACADE DESIGN PATTERN: Delegates to sub-controllers.
 * Follows SOLID: Open/Closed Principle, Single Responsibility.
 */
public class ControllerFacade implements Viewable {

    // Sub-controllers (Dependency Injection possible)
    private final GameManager gameManager;
    private final GameVerifier gameVerifier;
    private LogActions logger;
    private Game currentGame;

    public ControllerFacade() {
        this.gameManager = new GameManager();
        this.gameVerifier = new GameVerifier();
        this.logger = new LogActions(currentGame);
    }

    // Constructor for dependency injection (if needed for testing)
    public ControllerFacade(GameManager gameManager, GameVerifier gameVerifier) {
        this.gameManager = gameManager;
        this.gameVerifier = gameVerifier;
        this.logger = new LogActions(currentGame);
    }

    @Override
    public Catalog getCatalog() {
        return gameManager.getCatalog();
    }

    @Override
    public Game getGame(DifficultyEnum level) throws NotFoundException {
        return gameManager.getGame(level);
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
        
    }

    @Override
    public boolean[][] getValidityMap(Game game) {
        return gameVerifier.getValidityMap(game);
    }

    @Override
    public int[][] getSolvedBoard(Game game) throws InvalidGame {
        return gameVerifier.getSolvedBoard(game);
    }

    public Game getCurrentGame() {
        return currentGame;
    }
}