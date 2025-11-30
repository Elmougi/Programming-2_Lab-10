package Checkers;

import java.util.*;

public abstract class Checker implements Runnable
{
    int id;
    int [] values;
    List <DuplicateInfo> duplicates;

    public Checker(int id ,int[] values)
    {
        this.id = id;
        this.values = values;
        this.duplicates = new ArrayList<>();
    }

    @Override
    public void run() {
        runCheck();
    }

    public abstract void runCheck();
    public void detectDuplicates() {
        int[] copy = Arrays.copyOf(values, 9);
        Arrays.sort(copy);

        for (int i = 0; i < 8; i++) {
            if (copy[i] == copy[i + 1]) {
                int duplicatedValue = copy[i];
                List<Integer> positions = new ArrayList<>();

                for (int j = 0; j < 9; j++) {
                    if (values[j] == duplicatedValue) {
                        positions.add(j + 1);
                    }
                }

                if (!duplicateAlreadyStored(duplicatedValue)) {
                    duplicates.add(new DuplicateInfo(id, duplicatedValue, positions));
                }
            }
        }
    }

    private boolean duplicateAlreadyStored(int value) {
        for (DuplicateInfo info : duplicates) {
            if (info.getValue() == value)
                 return true;
        }
        return false;
    }

    public List<DuplicateInfo> getDuplicates() {
        return duplicates;
    }

    public boolean isValid() {
        return duplicates.isEmpty();
    }

    public int getId() {
        return id;
    }
}