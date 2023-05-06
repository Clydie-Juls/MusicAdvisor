package advisor.state;

import advisor.controllers.ProgramController;

public class StateManager {

    /**
     * Handles all possible command of the user to the program.
     * @param currentState - Current state input of the client.
     * @param isAuthorized - Checks if the client is authorized.
     * @param moveState - Checks if the client chooses to go the previous or next page, or none.
     * @param args - additional arguments inputted by the client.
     * @return - result of the program state(almost returns the same state expect during authentication process).
     * @throws Exception - When failure occurs(except disagreed authorization) during authentication.
     */
    public static ProgramState handleState(ProgramState currentState, boolean isAuthorized,
                                           MoveState moveState, String ... args) throws Exception {
            switch (currentState) {
                case AUTHENTICATION -> isAuthorized = ProgramController.authUser();
                case NEW_RELEASES -> handleAccess(currentState,
                        () -> ProgramController.showNewReleases(moveState), isAuthorized);
                case FEATURED ->  handleAccess(currentState,
                        () -> ProgramController.showFeatured(moveState), isAuthorized);
                case CATEGORIES ->  handleAccess(currentState,
                        () -> ProgramController.showCategories(moveState), isAuthorized);
                case PLAYLISTS ->  handleAccess(currentState,
                        () -> ProgramController.showMoodPlaylists(args[0], moveState), isAuthorized);
                case EXIT ->  handleAccess(currentState, ProgramController::showExit, isAuthorized);
                default -> System.out.println("Error!!! command not recognized");
            }

            if(currentState == ProgramState.AUTHENTICATION) {
                currentState = checkAuthStatus(isAuthorized);
            }
            return currentState;
    }

    /**
     * Responds to the client with regard to the authentication results.
     * @param isAuthorized - result of the authentication process.
     * @return - Program state of whether the auth process succeeds or not.
     */
    private static ProgramState checkAuthStatus(boolean isAuthorized) {
        if(isAuthorized) {
            return ProgramState.AUTHENTICATION_SUCCESS;
        }
        return ProgramState.AUTHENTICATION_FAILED;
    }

    /**
     * Manages authorization from the client command. If a feature needs authentication, the client must proceed
     * to authenticate first. Otherwise, they can access regardless.
     * @param stateReq - Checks if the client needs authentication.
     * @param stateFunction - function that will run if the client has access.
     * @param isAuthorized - Checks if client has authorization.
     */
    private static void handleAccess(ProgramState stateReq, Runnable stateFunction, boolean isAuthorized) {
        if (!stateReq.isAuthNeeded() || isAuthorized) {
            stateFunction.run();
        } else {
            System.out.println("Please, provide access for application.");
        }
    }
}
