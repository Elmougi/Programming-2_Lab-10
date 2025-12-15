package Board;

public class ColumnIterator_2D<T> implements BoardIterator<T> {
    private final T[][] board;
    private int currentColumn; // index
    private int currentElement; // relative to the column

    public ColumnIterator_2D(T[][] board) {
        this.board = board;
        this.currentColumn = 0;
        this.currentElement = 0;
    }

    @Override
    public boolean hasNextList() {
        return currentColumn < board[0].length;
    }

    @Override
    public boolean hasNextElement() {
        return currentElement < board.length;
    }

    @Override
    public int[] nextListIndex() {
        if (!hasNextList()) {
            throw new IllegalStateException("No more columns available.");
        }
        currentElement = 0; 
        return new int[]{0, currentColumn++};
    }

    @Override
    public T nextElement() {
        if (!hasNextElement()) {
            return null;
        }
        return board[currentElement++][currentColumn];
    }

    @Override
    public void reset() {
        this.currentColumn = 0;
        this.currentElement = 0;
    }

}
