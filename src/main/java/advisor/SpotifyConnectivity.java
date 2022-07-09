package advisor;

import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import com.sun.net.httpserver.HttpServer;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.util.Base64;
import java.util.Objects;


public class SpotifyConnectivity {

    final String clientID = "7217f2b1d3dc44cb8222bbb9c4525d83";
    final String clientSecret = "b81ddadac8644181b9381f03dc5389a0";

    public void setReceivedCode(String receivedCode) {
        this.receivedCode = receivedCode;
    }

    public void setCodeReceived(boolean codeReceived) {
        isCodeReceived = codeReceived;
    }

    String receivedCode = "empty";
    String queryy = "empty";
    volatile boolean isCodeReceived;

    public SpotifyConnectivity() {
        this.receivedCode = null;
        this.isCodeReceived = false;
    }


    /**
     * Spotify auhorization code flow implementation
     * @return true if token obtained properly
     * @throws IOException
     * @throws InterruptedException
     */
    boolean authorize() throws IOException, InterruptedException {
        System.out.println("use this link to request the access code:");
        System.out.println(Config.SpotifyAccessServerPoint + "/authorize?" +
                "client_id=" + Config.clientID +
                "&redirect_uri=" + Config.redirectURI +
                "&response_type=code");
        receiveCode();
        receiveToken();

        return this.isCodeReceived;
    }

    private void receiveCode() throws IOException, InterruptedException {
        try {
            HttpServer server = HttpServer.create();
            server.bind(new InetSocketAddress(8080), 0);
            server.createContext("/",
                    httpExchange ->  {
                        String query = httpExchange.getRequestURI().getQuery();
                        String responseToServer;
                        if (query!= null && query.startsWith("code=")) {
                            receivedCode = query.replaceFirst("code=", "");
                            System.out.println("code received");
                            responseToServer = "Got the code. Return back to your program.";
                            SpotifyConnectivity.this.setCodeReceived(true);
                        } else {
                            responseToServer = "Authorization code not found. Try again.";
                            SpotifyConnectivity.this.setCodeReceived(false);
                        }
                        httpExchange.sendResponseHeaders(200, responseToServer.length());
                        httpExchange.getResponseBody().write(responseToServer.getBytes());
                        httpExchange.getResponseBody().close();
                    }
            );

            System.out.println("waiting for code...");

            server.start();
            while (receivedCode == null) {
                System.out.println("recivedCode: " + receivedCode);
                System.out.println("queryy: " + queryy);
                Thread.sleep(1000L);
            }

            server.stop(1);
        } catch (IOException e) {
            e.printStackTrace();
        }

    }

    private void receiveToken() {
        String clientCred = Config.clientID + ":" + Config.clientSecret;
        String clientCredEncode = Base64.getEncoder().encodeToString(clientCred.getBytes());

        HttpClient client = HttpClient.newBuilder().build();
        HttpRequest request = HttpRequest.newBuilder()
                .header("Content-Type", "application/x-www-form-urlencoded")
                .header("Authorization", String.format("Basic %s", clientCredEncode))
                .uri(URI.create(Config.SpotifyAccessServerPoint + "/api/token"))
                .POST(HttpRequest.BodyPublishers.ofString("grant_type=authorization_code" +
                        "&code=" + this.receivedCode +
                        "&redirect_uri=" + Config.redirectURI))
                .build();

        try {
            System.out.println("making http request for access_token...");
            HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());
            System.out.println("response:");
            System.out.println(response.body());
            Config.setToken(getTokenFromJSON(response.body()));
        } catch (InterruptedException | IOException e) {
            e.printStackTrace();
        }
    }

    private String getTokenFromJSON(String body) {
        JsonElement jsonElement = JsonParser.parseString(body);
        JsonObject jsonObject = jsonElement.getAsJsonObject();
        return jsonObject.get("access_token").getAsString();
    }


}