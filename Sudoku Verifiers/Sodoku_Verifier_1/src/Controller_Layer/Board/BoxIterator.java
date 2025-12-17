package Controller_Layer.Board;

public class BoxIterator<T> implements BoardIterator<T> {
    private final T[][] board;
    private final int boxSize;
    private int currentBox; // current box
    private int currentElement; // index within the current box

    public BoxIterator(T[][] board) {
        this.board = board;
        this.boxSize = (int) Math.sqrt(board.length);
        this.currentBox = 0;
        this.currentElement = 0;
    }

    @Override
    public boolean hasNextList() {
        return currentBox + 1 < boxSize * boxSize;
    }

    @Override
    public boolean hasNextElement() {
        return currentElement < boxSize * boxSize;
    }

    @Override
    public int nextList() {
        if (!hasNextList()) {
            throw new IllegalStateException("No more boxes available.");
        }

        currentElement = 0;
        return ++currentBox;
    }

    @Override
    public T nextElement() {
        if (!hasNextElement()) {
            return null;
        }
        int rowInBox = currentElement / boxSize; // the / floors the value so 0-3 is 0 , 4-6 is 1, 7-9 is 2 then *3
        int colInBox = currentElement % boxSize; // % for boxes: 4, 5, 6, 7, 8, 9

        int boxRow = currentBox / boxSize;
        int boxCol = currentBox % boxSize;

        T element = board[boxRow * boxSize + rowInBox][boxCol * boxSize + colInBox];
        currentElement++;

        return element;
    }

    @Override
    public int[] currentElementIndex() {
        int rowInBox = (currentElement - 1) / boxSize;
        int colInBox = (currentElement - 1) % boxSize;

        int boxCol = (currentBox - 1) % boxSize;
        int boxRow = (currentBox - 1) / boxSize;

        return new int[] { boxRow * boxSize + rowInBox, boxCol * boxSize + colInBox };
    }

    @Override
    public void reset() {
        this.currentBox = 0;
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

    @Override
    public void setToList(int listIndex) {
        if (listIndex < 0 || listIndex >= boxSize * boxSize) {
            throw new IllegalArgumentException("Invalid box index: " + listIndex);
        }
        this.currentBox = listIndex + 1;
        this.currentElement = 0;
    }
}
