package Controller_Layer.Board;

public interface Board<T> {
    public BoardIterator<T> rowIterator();

    public BoardIterator<T> columnIterator();

    public BoardIterator<T> boxIterator();
}
