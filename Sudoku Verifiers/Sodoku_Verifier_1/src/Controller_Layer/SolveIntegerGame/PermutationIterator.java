package Controller_Layer.SolveIntegerGame;

import java.util.Iterator;
import java.util.NoSuchElementException;

public class PermutationIterator implements Iterator<int[]> {
    private final int size;
    private final int missingCount;
    private final long totalPermutations;
    private long currentIndex;

    public PermutationIterator(int size, int missingCount) {
        this.size = size;
        this.missingCount = missingCount;
        this.totalPermutations = (long) Math.pow(size, missingCount);
        this.currentIndex = 0;
    }

    @Override
    public boolean hasNext() {
        return currentIndex < totalPermutations;
    }

    @Override
    public int[] next() { // will keep iterating for the totalPermutations value
        if (!hasNext()) {
            throw new NoSuchElementException("No more permutations");
        }

        int[] permutation = new int[missingCount]; // this creation is not redundent -> will be used inside the Flyweight Context
        long temp = currentIndex;

        for (int i = 0; i < missingCount; i++) {
            permutation[i] = (int) (temp % size) + 1; // Values from 1 to SIZE
            temp /= size;
        }

        currentIndex++;
        return permutation;
    }

    /*
        Explainantion:
        Loops = number of all possible permutations
            for each index of this loop (0 to number of all possible permutations -1):
                currentIndex % size + 1 -> will iterate from 1 to size for total of Loops/size times
                currentIndex / size -> after each loop of 1 to size this digit will increase by 1 because currentIndex will double  (if x/y = z, then 2x/y = 2z))

                ex: size = 3, missingCount = 2
                sequence of permutations:
                1,1
                2,1
                3,1
                1,2
                2,2
                3,2
                1,3
                2,3
                3,3

                got the idea?
    */
}