package Checker;
import java.util.Set;

public class Violation {
    private char type; // r->row, c->column, b->box
    private int index; // 0 to 8; we have 9 of each
    private int number; // number in violation
    private Set<Integer> positions; // set to have them arranged for easiness
    
    Violation(char type, int index, int number, Set<Integer> positions) {
        setType(type);
        setIndex(index);
        setNumber(number);
        setPositions(positions);
    }
    public void setType(char type) {
        if(type == '\0') {
            throw new IllegalArgumentException("Type cannot be empty");
        }
        if(type != 'r' && type != 'c' && type != 'b') {
            throw new IllegalArgumentException("Type must be 'r', 'c' or 'b'");
        }

        this.type = type;
    }
    public void setIndex(int index) {
        if(index < 0 || index > 8) {
            throw new IndexOutOfBoundsException("Index must be between 0 and 8");
        }
        
        this.index = index;
    }
    public void setNumber(int number) {
        if(number < 1 || number > 9) {
            throw new IllegalArgumentException("Number must be between 1 and 9");
        }
        
        this.number = number;
    }
    public void setPositions(Set<Integer> positions) {
        if(positions == null || positions.isEmpty()) {
            throw new IllegalArgumentException("Positions cannot be null or empty");
        }
        for(int pos : positions) {
            if(pos < 0 || pos > 8) {
                throw new IndexOutOfBoundsException("Position must be between 0 and 8");
            }
        }
        
        this.positions = positions;
    }
}
