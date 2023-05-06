package advisor.state;


public enum MoveState {
    NEXT,
    NONE,
    PREV;

    /**
     * inputs move state
     * @param input - Input string of the user.
     * @return - Move state enum of the input.
     */
    static MoveState inputMoveState(String input) {
        for (MoveState value : MoveState.values()) {
            if (value.name().equalsIgnoreCase(input)) {
                return value;
            }
        }
        return NONE;
    }
}
