package advisor.server;

import com.sun.net.httpserver.HttpServer;
import advisor.utils.JsonAnalyzer;

import java.io.IOException;

import java.net.InetSocketAddress;
import java.net.URI;
import java.net.URISyntaxException;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;

public class Server {
    private static HttpServer server;

    /**
     * Creates the server and sets the context.
     * @throws IOException - If it fails to create a server.
     */
    public static void create() throws IOException {
        server = HttpServer.create(new InetSocketAddress(8080), 0);
        authConfirmationContext();
        server.start();
    }

    /**
     * Handles client response to authentication confirmation.
     * Gets the code but only sends the response to the server.
     */
    private static void authConfirmationContext() {
        server.createContext("/", exchange -> {
            String query = exchange.getRequestURI().getQuery();
            String response = "Got the code. Return back to your program.";
            int stateCode = 200;
            if(query != null && query.contains("code")) {
                Config.CODE.setInfo(query.substring(5));
            }

            if (Config.CODE.getInfo().length() == 0) {
                response = "Authorization code not found. Try again.";
                stateCode = 404;
            }

            exchange.sendResponseHeaders(stateCode, response.length());
            exchange.getResponseBody().write(response.getBytes());
            exchange.getResponseBody().close();
        });
    }

    /**
     * Waits for the client to respond to the auth confirmation.
     * @return - The response of the player to allow access or not.
     * @throws InterruptedException - if the thread is interrupted.
     * @throws URISyntaxException - when the constructed URI could not be parsed due to syntax problems.
     */
    public static boolean waitForAuthConfirmation() throws InterruptedException, URISyntaxException {
        System.out.println("waiting for code...");
        // Loops and waits until the code is received.
        while (Config.CODE.getInfo().length() == 0) {
            Thread.sleep(10);
        }
        System.out.println("code received");
        // Get the response of the client
        boolean authAccepted = getAuthResponse();
        // Finish using server
        server.stop(1);
        return authAccepted;
    }

    /**
     * Request data from the client's auth confirmation.
     * @return - The response of the player to allow access or not.
     * @throws URISyntaxException - when the constructed URI could not be parsed due to syntax problems.
     */
    private static boolean getAuthResponse() throws URISyntaxException {
        HttpClient client = HttpClient.newBuilder().build();
        HttpRequest request = HttpRequest.newBuilder().uri(new URI(Config.REDIRECT_URI.getInfo())).GET().build();
        try {
            HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());
            return response.statusCode() >= 200 && response.statusCode() <= 299 ;
        } catch (IOException | InterruptedException e) {
            throw new RuntimeException(e);
        }
    }

    /**
     * Request Info from the API
     * @param uri - absolute path of the resource.
     * @return - data requested from Spotify API.
     * @throws Exception - When there is a URISyntaxException or when failed tto get the response.
     */
    public static String apiGetRequest(String uri) throws Exception {
        HttpClient client = HttpClient.newBuilder().build();
        HttpRequest request = HttpRequest.newBuilder()
                .header("Authorization", "Bearer " + Config.ACCESS_TOKEN.getInfo())
                .uri(URI.create(uri)).GET().build();
        try {
            HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());
            return response.body();
        } catch (IOException | InterruptedException e) {
            System.out.println();
            throw new Exception();
        }
    }

    /**
     * Request for the access token to allow authorization.
     * @return - true If the client is authorized. Otherwise, no.
     */
    public static boolean requestAccessToken() {
        //Set parameter for the POST request.
        String parameters = String.format("code=%s" +
                        "&redirect_uri=%s" +
                        "&grant_type=%s" +
                        "&client_id=%s" +
                        "&client_secret=%s",
                Config.CODE.getInfo(), Config.REDIRECT_URI.getInfo(), Config.RESPONSE_TYPE.getInfo(),
                Config.CLIENT_ID.getInfo(), Config.SECRET.getInfo());

        HttpClient client = HttpClient.newBuilder().build();

        // Content type is a key=value format
        HttpRequest request = HttpRequest.newBuilder()
                .header("Content-Type", "application/x-www-form-urlencoded")
                .uri(URI.create(Config.ACCESS.getInfo() + "/api/token"))
                .POST(HttpRequest.BodyPublishers.ofString(parameters))
                .build();

        System.out.println("making http request for access_token...");
        try {
            HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());
            Config.ACCESS_TOKEN.setInfo(JsonAnalyzer.getElementString(response.body(), "access_token"));
            return true;
        } catch (IOException | InterruptedException e) {
            return false;
        }
    }

    /**
     * Formats the URI for request.
     * @return - URI for the Spotify API request.
     */
    public static String spotifyApiUri() {
        return String.format("%s?client_id=%s&redirect_uri=%s&response_type=%s",
                Config.ACCESS.getInfo() + "/authorize", Config.CLIENT_ID.getInfo(),
                Config.REDIRECT_URI.getInfo(), Config.GRANT_TYPE.getInfo());
    }
}
