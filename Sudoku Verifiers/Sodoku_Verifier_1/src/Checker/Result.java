package Checker;

import java.util.ArrayList;
import java.util.List;
import java.util.Collections;

public class Result<T> {
    private boolean isValid;
    private List<Violation<T>> rowViolations = new ArrayList<>();
    private List<Violation<T>> colViolations = new ArrayList<>();
    private List<Violation<T>> boxViolations = new ArrayList<>();

    public Result(List<Violation<T>> rowViolations, List<Violation<T>> colViolations,
            List<Violation<T>> boxViolations) {
        this.rowViolations = rowViolations;
        this.colViolations = colViolations;
        this.boxViolations = boxViolations;
        this.isValid = rowViolations.isEmpty() && colViolations.isEmpty() && boxViolations.isEmpty();
    }

    public boolean isValid() {
        return isValid;
    }

    @Override
    public String toString() { // so I can use it in sout directly :]
        StringBuilder s = new StringBuilder();
        if (isValid) {
            s.append("VALID.\n");
        } else {
            Collections.sort(rowViolations);
            Collections.sort(colViolations);
            Collections.sort(boxViolations);

            s.append("INVALID.\n\n");
            for (Violation<T> r : rowViolations) {
                s.append("Row " + r.getIndex() + ", #" + r.getValue() + ", [" + r.getPositions() + "]\n");
            }
            s.append("----------------------------------------------\n");
            for (Violation<T> c : colViolations) {
                s.append("Col " + c.getIndex() + ", #" + c.getValue() + ", [" + c.getPositions() + "]\n");
            }
            s.append("----------------------------------------------\n");
            for (Violation<T> b : boxViolations) {
                s.append("Box " + b.getIndex() + ", #" + b.getValue() + ", [" + b.getPositions() + "]\n");
            }
        }
        return s.toString();
    }
}
