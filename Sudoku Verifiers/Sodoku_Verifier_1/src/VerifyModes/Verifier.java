package Verifier1.VerifyModes;
public abstract class Verifier {
    protected static Verifier instance; // singelton applied to all modes
    protected int[][] board;

    protected Verifier(int[][] board) {
        this.board = board;
    }

    public abstract Result verify();
}
