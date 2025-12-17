package Facade_Interfaces;

import java.io.IOException;

import View_Layer.UserAction;
import gameExceptions.*;

public interface Controllable {
    boolean[] getCatalog();

    int[][] getGame(char level) throws NotFoundException;

    void driveGames(String sourcePath) throws SolutionInvalidException;

    // A boolean array which says if a specifc cell is correct or invalid
    boolean[][] verifyGame(int[][] game);

    // contains the cell x, y and solution for each missing cell
    int[][] solveGame(int[][] game) throws InvalidGame;

    // Logs the user action
    void logUserAction(UserAction userAction) throws IOException;
}
