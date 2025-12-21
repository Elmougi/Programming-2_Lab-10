package View_Layer;

import Adapter_Layer.FacadeAdapter;
import Facade_Interfaces.Controllable;
import gameExceptions.*;

import java.io.IOException;

/**
 * VIEW LAYER FACADE: Implements Controllable interface.
 * ONLY delegates to adapter - NO business logic, NO logging helpers.
 * Clean separation of concerns.
 */
public class ViewFacade implements Controllable {

    private final Controllable adapter;

    public ViewFacade(Controllable adapter) {
        this.adapter = adapter;
    }

    public ViewFacade() {
        this.adapter = new FacadeAdapter();
    }

    @Override
    public boolean[] getCatalog() {
        return adapter.getCatalog();
    }

    @Override
    public int[][] getGame(char level) throws NotFoundException {
        return adapter.getGame(level);
    }

    @Override
    public void driveGames(String sourcePath) throws SolutionInvalidException {
        adapter.driveGames(sourcePath);
    }

    @Override
    public boolean[][] verifyGame(int[][] game) {
        return adapter.verifyGame(game);
    }

    @Override
    public int[][] solveGame(int[][] game) throws InvalidGame {
        return adapter.solveGame(game);
    }

    @Override
    public void logUserAction(UserAction userAction) {
        try{
            adapter.logUserAction(userAction);
        } catch (IOException e) {
            System.out.println("Error logging user action: " + e.getMessage());
        } catch (Exception e){
            System.out.println("Unexpected error logging user action: " + e.getMessage());
        }
    }
}