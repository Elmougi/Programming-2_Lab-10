package Controller_Layer.Checker;

import java.util.ArrayList;
import java.util.List;
import java.util.Collections;

public class Result<T> {
    protected boolean isValid;
    protected List<Violation<T>> rowViolations = new ArrayList<>();
    protected List<Violation<T>> colViolations = new ArrayList<>();
    protected List<Violation<T>> boxViolations = new ArrayList<>();

    public Result(List<Violation<T>> rowViolations, List<Violation<T>> colViolations,
            List<Violation<T>> boxViolations) {

        if (rowViolations == null || colViolations == null || boxViolations == null) {
            throw new IllegalArgumentException("Violation lists cannot be null");
        }

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
                s.append("Row " + r.getPos() + ", #" + r.getValue() + ", [" + r.getPositions() + "]\n");
            }
            s.append("----------------------------------------------\n");
            for (Violation<T> c : colViolations) {
                s.append("Col " + c.getPos() + ", #" + c.getValue() + ", [" + c.getPositions() + "]\n");
            }
            s.append("----------------------------------------------\n");
            for (Violation<T> b : boxViolations) {
                s.append("Box " + b.getPos() + ", #" + b.getValue() + ", [" + b.getPositions() + "]\n");
            }
        }
        return s.toString();
    }
}
