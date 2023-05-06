package advisor;

import advisor.client.Client;
import advisor.server.Config;

public class Main {
    public static void main(String[] args) {
        // Set server configurations from the given command-line arguments.
        Config.setAltConfigArgs(args);
        // Creates a new client that will interact with the controller and allows input.
        new Client();
    }
}
