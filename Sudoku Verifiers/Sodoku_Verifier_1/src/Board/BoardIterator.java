package Board;

public interface BoardIterator<T> {
    public boolean hasNextElement();

    public boolean hasNextList();

    public T nextElement();

    public int[] nextListIndex();

    public void reset();
}
