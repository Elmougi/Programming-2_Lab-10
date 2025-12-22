package Controller_Layer;

import Controller_Layer.Board.SodokuBoard;
import Controller_Layer.Checker.BooleanMapAdapter;
import Controller_Layer.Checker.Result;
import Controller_Layer.Checker.SudokuIntVerifier;
import Controller_Layer.SolveIntegerGame.UnsolvedGameFlyweight;
import gameExceptions.InvalidGame;

public class GameVerifier {

    public String verifyGame(Game game) {
        SudokuIntVerifier verifier = new SudokuIntVerifier(game.board);
        Result<Integer> result = verifier.verify();
        return result.toString();
    }


    public boolean[][] getValidityMap(Game game) {
        SudokuIntVerifier verifier = new SudokuIntVerifier(game.board);
        Result<Integer> result = verifier.verify();

        // Convert to SudokuBoard
        Integer[][] integerBoard = convertToIntegerArray(game.board);
        SodokuBoard<Integer> board;
        try {
            board = new SodokuBoard<>(9, integerBoard);
        } catch (gameExceptions.InvalidGame e) {
            throw new IllegalStateException("Provided board is not a valid Sudoku board:\n" + e.getMessage());
        }


        BooleanMapAdapter<Integer> adapter = new BooleanMapAdapter<>(board, result);
        return adapter.resultMap();
    }


    public int[] solveGame(Game game) throws InvalidGame {
        Integer[][] integerBoard = convertToIntegerArray(game.board);

        UnsolvedGameFlyweight solver = new UnsolvedGameFlyweight(9, integerBoard);
        int[][] solved = solver.solve();

        if (solved == null || !solver.isSolved()) {
            throw new InvalidGame("No solution exists for this game");
        }

        return encodeMissingCells(game.board, solved);
    }


    public int[][] getSolvedBoard(Game game) throws InvalidGame {
        int[] encoded = solveGame(game);
        return decodeSolution(encoded, game.board);
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

    private Integer[][] convertToIntegerArray(int[][] board) {
        Integer[][] result = new Integer[board.length][board[0].length];
        for (int i = 0; i < board.length; i++) {
            for (int j = 0; j < board[i].length; j++) {
                result[i][j] = board[i][j];
            }
        }
        return result;
    }


    private int[] encodeMissingCells(int[][] original, int[][] solved) {
        int missingCount = 0;
        for (int i = 0; i < 9; i++) {
            for (int j = 0; j < 9; j++) {
                if (original[i][j] == 0) {
                    missingCount++;
                }
            }
        }

        int[] encoded = new int[missingCount * 3];
        int index = 0;

        for (int i = 0; i < 9; i++) {
            for (int j = 0; j < 9; j++) {
                if (original[i][j] == 0) {
                    encoded[index++] = i;
                    encoded[index++] = j;
                    encoded[index++] = solved[i][j];
                }
            }
        }

        return encoded;
    }
}