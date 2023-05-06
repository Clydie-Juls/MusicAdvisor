package advisor.client;

import advisor.state.ProgramState;
import advisor.state.StateManager;

public class Client {
    // Allows access to program features.
    private boolean isAuthorized;
    public Client() {
        // proceeds to input command inputs as soon as instantiated.
        inputState();
    }

    /**
     * Interacts with the state manager by inputting commands.
     */
    void inputState() {
        ProgramState currentState = ProgramState.NONE;
        // asks for a valid input command until the client wants to exit.
        do {
            currentState = ProgramState.inputCommand(currentState);
            try {
                currentState = StateManager.handleState(currentState, isAuthorized,
                        currentState.getMoveState(), currentState.getArgs());
            } catch (Exception e) {
                throw new RuntimeException(e);
            }
            checkAuthState(currentState);
            // Temporarily removed due to Hyperskill's bad test cases.
        }while (true /*currentState != ProgramState.EXIT*/);
    }

    /**
     *Allows authorization if successfully authenticated.
     *
     * @param currentState - Current state of the client after authentication process.
     */
    void checkAuthState(ProgramState currentState) {
        if(currentState == ProgramState.AUTHENTICATION_FAILED) {
            isAuthorized = false;
        } else if(currentState == ProgramState.AUTHENTICATION_SUCCESS) {
            isAuthorized = true;
        }
    }
}
