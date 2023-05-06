package advisor.server;

import java.util.List;

public enum Config {

    CODE(""), // Authorization code
    ACCESS_TOKEN(""), // Access token of the client after successful auth.
    REDIRECT_URI("http://localhost:8080"), // Server URI
    CLIENT_ID("e159e20cd2274532a93e65e9c682d778"),
    SECRET("b6f5ff34e6a443e3bd1261ebd5b94f66"),
    GRANT_TYPE("code"),
    RESPONSE_TYPE("authorization_code"),
    RESOURCE("https://api.spotify.com"),
    ACCESS("https://accounts.spotify.com"),
    PAGE("5"); // Maximum number of entry per page


    private String info;

    Config(String info) {
        this.info = info;
    }

    //Getters
    public String getInfo() {
        return info;
    }

    //Setters
    public void setInfo(String info) {
        this.info = info;
    }

    /**
     * Set alternative configuration template(For testing purposes).
     * @param args - List of arguments from the command line arguments.
     */
    public static void setAltConfigArgs(String[] args) {
        List<String> argList = List.of(args);
        for (Config config : Config.values()) {
            int index = argList.indexOf("-" + config.name().toLowerCase());
            if(index != -1) {
                config.setInfo(argList.get(index + 1));
            }
        }
    }
}
