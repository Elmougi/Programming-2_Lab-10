package Controller_Layer.Board;

public class ColumnIterator<T> implements BoardIterator<T> {
    private final T[][] board;
    private int currentColumn; // index
    private int currentElement; // relative to the column

    public ColumnIterator(T[][] board) {
        this.board = board;
        this.currentColumn = 0;
        this.currentElement = 0;
    }

    @Override
    public boolean hasNextList() {
        return currentColumn + 1 < board[0].length;
    }

    @Override
    public boolean hasNextElement() {
        return currentElement < board.length;
    }

    @Override
    public int nextList() {
        if (!hasNextList()) {
            throw new IllegalStateException("No more columns available.");
        }
        currentElement = 0; 
        return ++currentColumn;
    }

    @Override
    public T nextElement() {
        if (!hasNextElement()) {
            return null;
        }
        return board[currentElement++][currentColumn];
    }

    @Override
    public int[] currentElementIndex() {
        return new int[]{currentElement, currentColumn};
    }

    @Override
    public void reset() {
        this.currentColumn = 0;
        this.currentElement = 0;
    }

    @Override
    public int getElementNum() {
        return currentElement;
    }

    @Override
    public void resetList() {
        this.currentElement = 0;
    }
}
