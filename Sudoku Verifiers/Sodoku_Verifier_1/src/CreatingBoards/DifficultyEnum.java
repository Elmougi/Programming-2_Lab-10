package CreatingBoards;

public enum DifficultyEnum {
    hard(25),
    medium(20),
    easy(10);

    private final int value;

    private DifficultyEnum(int value) {
        this.value = value;
    }

    public int getValue() {
        return value;
    }
}