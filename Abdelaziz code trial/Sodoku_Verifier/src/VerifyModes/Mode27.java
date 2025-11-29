package VerifyModes;

import Checker.*;

public class Mode27 extends Verifier {
    private Mode27(int[][] board) {
        super(board);
    }

    public static Verifier getInstance(int[][] board) {
        if (instance == null) {
            instance = new Mode27(board);
        }
        return instance;
    }

    @Override
    public Result verify() {
        

        return Checker.getResult();
    }
}
