package Checkers;

import java.util.List;

public class DuplicateInfo {
    private int checkerId;           
    private int value;               
    private List<Integer> positions; 

    public DuplicateInfo(int checkerId, int value, List<Integer> positions) {
        this.checkerId = checkerId;
        this.value = value;
        this.positions = positions;
    }

  
    public int getCheckerId() {
        return checkerId;
    }

    public int getValue() {
        return value;
    }

    public List<Integer> getPositions() {
        return positions;
    }
    
    public int getCount() {
        return positions.size();
    }
}
