package Controller_Layer;

public class Catalog {
    // True if there is a game in progress, False otherwise.
    public boolean current;

    // True if there is at least one game available
    // for each difficulty, False otherwise.
    public boolean allModesExist;

    public Catalog(boolean current, boolean allModesExist) {
        this.current = current;
        this.allModesExist = allModesExist;
    }

    public boolean hasCurrent() {
        return current;
    }

    public boolean hasAllModesExist() {
        return allModesExist;
    }

    public void setCurrent(boolean current) {
        this.current = current;
    }

    public void setAllModesExist(boolean allModesExist) {
        this.allModesExist = allModesExist;
    }
}