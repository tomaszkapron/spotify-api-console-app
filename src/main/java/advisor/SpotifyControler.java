package advisor;

import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;

import java.io.IOException;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.util.ArrayList;
import java.util.Objects;

public class SpotifyControler {

    HttpClient client;

    public SpotifyControler() {
        this.client = HttpClient.newBuilder().build();
    }

    public void get(String path, State state) {
        HttpRequest request = HttpRequest.newBuilder()
                .header("Authorization", "Bearer " + Config.Token)
                .uri(URI.create(Config.SpotifyApiServerPath + path))
                .GET()
                .build();
        try {
            HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());

            switch (state) {
                case CATEGORIES:
                    View.getVieverInstance(response, state).parseAndPrintCategories();
                    break;
                case PLAYLISTS: case FEATURED:
                    View.getVieverInstance(response, state).parseAndPrintPlaylistOrFetured();
                    break;
                case NEW:
                    View.getVieverInstance(response, state).parseAndPrintNew();
                    break;
                default:

            }
        } catch (IOException | InterruptedException e) {
            e.printStackTrace();
        }
    }


    public String getCategoryIDfromName(String category) {
        HttpRequest request = HttpRequest.newBuilder()
                .header("Authorization", "Bearer " + Config.Token)
                .uri(URI.create(Config.SpotifyApiServerPath + "/v1/browse/categories"))
                .GET()
                .build();
        try {
            HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());
            JsonObject jo = JsonParser.parseString(response.body()).getAsJsonObject();
            JsonObject categories = jo.getAsJsonObject("categories");
            for (JsonElement je: categories.getAsJsonArray("items")) {
                JsonObject item = je.getAsJsonObject();
                String name = item.get("name").getAsString();
                if (Objects.equals(name, category)) {
                    return item.get("id").getAsString();
                }
            }

        } catch (IOException | InterruptedException e) {
            e.printStackTrace();
        }
        return "cat_dosnt_exists";
    }
}