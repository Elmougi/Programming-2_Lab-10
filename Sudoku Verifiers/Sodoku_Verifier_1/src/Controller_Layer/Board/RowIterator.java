package Controller_Layer.Board;

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
        return currentRow + 1 < board.length;
    }

    @Override
    public boolean hasNextElement() {
        //System.out.println("Current Row: " + currentRow + ", Current Element: " + currentElement);
        return currentElement < board[currentRow].length;
    }

    @Override
    public int nextList() {
        if (!hasNextList()) {
            throw new IllegalStateException("No more rows available.");
        }
        currentElement = 0;
        return ++currentRow;
    }

    @Override
    public T nextElement() {
        //System.out.println("Current Row: " + currentRow + ", Current Element: " + currentElement);
        //System.out.println(hasNextElement());
        if (!hasNextElement()) {
            //System.out.println("NO MORE ELEMENTS");
            return null;
        }
        return board[currentRow][currentElement++];
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
    
    @Override
    public void resetList() {
        this.currentElement = 0;
    }
}
