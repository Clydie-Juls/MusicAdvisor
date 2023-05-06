package advisor.state;

import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

public enum ProgramState {
    //Programmed as enum methods for command string comparison
    AUTHENTICATION("auth", false, 0),
    AUTHENTICATION_SUCCESS("", false, 0),
    AUTHENTICATION_FAILED("", false, 0),
    NEW_RELEASES("new", true, 0),
    FEATURED("featured", true, 0),
    CATEGORIES("categories", true, 0),
    PLAYLISTS("playlists", true, 1),
    EXIT("exit", false, 0),
    NONE("", false, 0); // Enum State solely used as placeholder

    //String to compare to command input
    private final String command;
    private final boolean authNeeded;
    private final int noOfArgs;
    private String[] args;
    private MoveState moveState;

    /**
     *
     * @param command - Appropriate command string for user input.
     * @param authNeeded - Checks if state is needs authorization.
     * @param noOfArgs - number of arguments the command accepts.
     */
    ProgramState(String command, boolean authNeeded, int noOfArgs) {
        this.command = command;
        this.authNeeded = authNeeded;
        this.noOfArgs = noOfArgs;
    }

    /**
     * Inputs state command
     * @param currState - Current state of the program.
     *                  Will return the same state if move state is inputted instead of program state
     * @return - Program state enum of user input.
     */
    public static ProgramState inputCommand(ProgramState currState) {
        Scanner scanner = new Scanner(System.in);
        ProgramState newState = ProgramState.NONE;
        MoveState newMoveState = MoveState.NONE;

        // Scans for input until valid state is found.
        while (newState == ProgramState.NONE) {
            System.out.print("> ");
            List<String> inputArgs = new ArrayList<>(List.of(scanner.nextLine().split("\\s+")));
            // Loops through all Program state enum values.
            for (ProgramState state : ProgramState.values()) {
                if(state.getCommand().equals(inputArgs.get(0).toLowerCase()) ||
                        inputArgs.get(0).length() == 0) {
                    newState = state;
                    inputArgs.remove(0);
                    newState.setArgs(String.join(" ", inputArgs));
                    break;
                }
            }
            // Checks if user input is actually a move state
            if(newState == ProgramState.NONE) {
                newMoveState = MoveState.inputMoveState(inputArgs.get(0));
                // If user input is unrecognizable.
                if(newMoveState == MoveState.NONE) {
                    System.out.println("state command not recognized.");
                } else {
                    newState = currState;
                }
            }
        }

        newState.setMoveState(newMoveState);
        return newState;
    }

    // Getters
    public String getCommand() {
        return command;
    }

    public boolean isAuthNeeded() {
        return authNeeded;
    }

    public int getNoOfArgs() {
        return noOfArgs;
    }

    public String[] getArgs() {
        return args;
    }

    public MoveState getMoveState() {
        return moveState;
    }

    //Setters
    public void setArgs(String... args) {
        this.args = args;
    }

    public void setMoveState(MoveState moveState) {
        this.moveState = moveState;
    }
}
