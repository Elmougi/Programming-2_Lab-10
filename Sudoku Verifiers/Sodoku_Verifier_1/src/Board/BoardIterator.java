package Board;

public interface BoardIterator<T> {
    public boolean hasNextElement();

    public boolean hasNextList();

    public T nextElement();

    public int[] currentElementIndex();

    public int nextList(); // return index of next list

    public int getElementNum();

    public void reset();

    public void resetList();
}
