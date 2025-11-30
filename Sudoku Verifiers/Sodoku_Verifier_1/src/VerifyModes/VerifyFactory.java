package Verifier1.VerifyModes;
public class VerifyFactory {
    public static Verifier createVerifier(int mode, int[][] board) {
        switch (mode) {
            case 0:
                return Mode0.getInstance(board);
            case 3:
                return Mode3.getInstance(board);
            case 27:
                return Mode27.getInstance(board);
            default:
                throw new IllegalArgumentException("Invalid mode: " + mode);
        }
    }
}
