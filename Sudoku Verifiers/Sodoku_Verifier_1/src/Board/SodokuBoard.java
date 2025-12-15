package Board;

public class SodokuBoard<T> implements Board<T> {
    private final T[][] board;

    public SodokuBoard(T[][] board) {
        this.board = board;
    }

    @Override
    public BoardIterator<T> rowIterator() {
        return new RowIterator_2D<>(board);
    }

    @Override
    public BoardIterator<T> columnIterator() {
        return new ColumnIterator_2D<>(board);
    }

    @Override
    public BoardIterator<T> oxIterator() {
        return new BoxIterator_2D<>(board);
    }

}
