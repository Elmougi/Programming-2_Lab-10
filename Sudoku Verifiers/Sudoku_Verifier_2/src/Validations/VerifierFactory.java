package Verifier2.Validations;

import Verifier2.Board.SgBoard;

public class VerifierFactory {

    public static SgVerifier createVerifier(int mode, SgBoard board) {
        switch (mode) {
            case 0:
                return new Mode0Verifier(board);
            case 3:
                return new Mode3Verifier(board);
            case 27:
                return new Mode27Verifier(board);
            default:
                throw new IllegalArgumentException("Invalid mode: " + mode + ". Valid modes: 0, 3, 27");
        }
    }
}
