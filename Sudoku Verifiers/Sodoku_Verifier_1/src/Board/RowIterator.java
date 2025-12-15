package Board;

public class RowIterator<T> implements BoardIterator<T> {
    private final T[][] board;
    private int currentRow; // index
    private int currentElement; // relative to the row

    public RowIterator(T[][] board) {
        this.board = board;
        this.currentRow = 0;
        this.currentElement = 0;
    }

    @Override
    public boolean hasNextList() {
        return currentRow < board.length;
    }

    @Override
    public boolean hasNextElement() {
        return currentElement < board[currentRow].length;
    }

    @Override
    public int[] nextList() {
        if (!hasNextList()) {
            throw new IllegalStateException("No more rows available.");
        }
        currentElement = 0;
        return new int[] { currentRow++, 0 };
    }

    @Override
    public T nextElement() {
        if (!hasNextElement()) {
            return null;
        }
        return board[currentRow][++currentElement];
    }

    @Override
    public int[] currentElementIndex() {
        return new int[] { currentRow, currentElement };
    }

    @Override
    public void reset() {
        this.currentRow = 0;
        this.currentElement = 0;
    }

    @Override
    public int getElementNum() {
        return currentElement;
    }
}
