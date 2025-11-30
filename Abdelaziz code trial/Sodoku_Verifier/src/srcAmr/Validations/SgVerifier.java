
package srcAmr.Validations;

import srcAmr.Board.*;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

public abstract class SgVerifier {

        protected SgBoard board;
        protected Map<String, Map<Integer, List<Integer>>> allDuplicates;
        // Key: "ROW 1", "COL 5", "BOX 3"
        // Value: Map of {duplicateValue -> [positions]}

        public SgVerifier(SgBoard board) {
            this.board = board;
            this.allDuplicates = new ConcurrentHashMap<>(); // Thread-safe
        }


        public abstract void verify();


    protected void validateCells(Cell[] cells, String type, int index) {
        Map<Integer, List<Integer>> duplicates = new HashMap<>();
        int[] firstPosition = new int[10];
        boolean[] seen = new boolean[10];

        for (int i = 0; i < 9; i++) {
            int value = cells[i].getValue();
            int position = i + 1;

            if (!seen[value]) {
                firstPosition[value] = position;
                seen[value] = true;
            } else {
                if (!duplicates.containsKey(value)) {
                    duplicates.put(value, new ArrayList<>());
                    duplicates.get(value).add(firstPosition[value]);
                    duplicates.get(value).add(position);
                } else {
                    duplicates.get(value).add(position);
                }
            }
        }
        if (!duplicates.isEmpty()) {
            String key = type + " " + index;
            allDuplicates.put(key, duplicates);
        }
    }

        public boolean isValid() {
            return allDuplicates.isEmpty();
        }

        public void printResults() {
            if (isValid()) {
                System.out.println("VALID");
            } else {
                System.out.println("INVALID");

                for (int i = 1; i <= 9; i++) {
                    String key = "ROW " + i;
                    if (allDuplicates.containsKey(key)) {
                        printDuplicates(key, allDuplicates.get(key));
                    }
                }
                System.out.println("------------------------------------------");

                for (int i = 1; i <= 9; i++) {
                    String key = "COL " + i;
                    if (allDuplicates.containsKey(key)) {
                        printDuplicates(key, allDuplicates.get(key));
                    }
                }
                System.out.println("------------------------------------------");

                for (int i = 1; i <= 9; i++) {
                    String key = "BOX " + i;
                    if (allDuplicates.containsKey(key)) {
                        printDuplicates(key, allDuplicates.get(key));
                    }
                }
            }
        }

        private void printDuplicates(String key, Map<Integer, List<Integer>> duplicates) {
            for (Map.Entry<Integer, List<Integer>> entry : duplicates.entrySet()) {
                System.out.println(key + ", #" + entry.getKey() + ", " + entry.getValue());
            }
        }
    }