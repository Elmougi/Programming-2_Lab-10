package CheckerFactory;

import Checkers.*;

public class CheckerFactory {

    public static final String ROW = "ROW";
    public static final String COLUMN = "COLUMN";
    public static final String BOX = "BOX";

    public static Checker createChecker(String type, int id, int[] values) {
        switch (type) {
            case ROW:
                return new RowChecker(id, values);
            case COLUMN:
                return new ColumnChecker(id, values);
            case BOX:
                return new BoxChecker(id, values);
            default:
                throw new IllegalArgumentException("Unknown type: " + type);
        }
    }
}