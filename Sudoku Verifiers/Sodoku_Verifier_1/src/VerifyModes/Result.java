package Verifier1.VerifyModes;
import java.util.ArrayList;
import java.util.List;

import Verifier1.Checker.Violation;

public class Result {
    private boolean isValid;
    private List<Violation> rowViolations = new ArrayList<>();
    private List<Violation> colViolations = new ArrayList<>();
    private List<Violation> boxViolations = new ArrayList<>();
    
    public Result(List<Violation> rowViolations, List<Violation> colViolations, List<Violation> boxViolations) {
        this.rowViolations = rowViolations;
        this.colViolations = colViolations;
        this.boxViolations = boxViolations;
        this.isValid = rowViolations.isEmpty() && colViolations.isEmpty() && boxViolations.isEmpty();
    }

    public boolean isValid() {
        return isValid;
    }

    @Override
    public String toString(){ // so I can use it in sout directly :]
        StringBuilder s = new StringBuilder();
        if(isValid) {
            s.append("VALID.\n");
        } else {
            s.append("INVALID.\n\n");
            for(Violation r: rowViolations){
                s.append("Row " + r.getIndex() + ", #" + r.getNumber() + ", [" + r.getPositionsString() + "]\n");
            }
            s.append("----------------------------------------------\n");
            for(Violation c: colViolations){
                s.append("Col " + c.getIndex() + ", #" + c.getNumber() + ", [" + c.getPositionsString() + "]\n");
            }
            s.append("----------------------------------------------\n");
            for(Violation b: boxViolations){
                s.append("Box " + b.getIndex() + ", #" + b.getNumber() + ", [" + b.getPositionsString() + "]\n");
            }
        }
        return s.toString();
    }
}
