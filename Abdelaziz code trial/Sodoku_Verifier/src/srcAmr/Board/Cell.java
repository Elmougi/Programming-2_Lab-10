
package srcAmr.Board;

public class Cell {

    private final int row;
    private final int col;
    private final int value;

    public Cell(int row, int col, int value) {
        this.row = row;
        this.col = col;
        this.value = value;
    }

    public int getRow() {
        return row;
    }

    public int getCol() {
        return col;
    }

    public int getValue() {
        return value;
    }

    public int getBoxNumber() {
        return (row / 3) * 3 + (col / 3);
    }

    @Override
    public String toString() {
        return "Cell[row=" + row + ", col=" + col + ", value=" + value +
                ", box=" + getBoxNumber() + "]";
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj) return true;
        if (obj == null || getClass() != obj.getClass()) return false;

        Cell other = (Cell) obj;
        return row == other.row && col == other.col && value == other.value;
    }

    @Override
    public int hashCode() {
        int result = row;
        result = 31 * result + col;
        result = 31 * result + value;
        return result;
    }
}