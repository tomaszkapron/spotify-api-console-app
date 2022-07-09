package advisor;

import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;

import java.net.http.HttpResponse;
import java.util.ArrayList;

public class View {

    private static final View insctance = new View();
    private static int lowerBaund;
    private static int upperBaund;
    private static int currentPage;
    private static int numberOfItems;
    private static int pagesNumber;
    private static HttpResponse<String> response;
    private static State state;


    private View() { }

    public static View getVieverInstance(HttpResponse<String> resp, State _state) {
        currentPage = 1;
        response = resp;
        state = _state;
        findResultsNumber();
        calculatePagesNumber();
        calcuateBaunds();
        return insctance;
    }

    public static void parseAndPrintCategories() {
        JsonObject jo = JsonParser.parseString(response.body()).getAsJsonObject();
        JsonObject categories = jo.getAsJsonObject("categories");

        JsonArray ja = categories.getAsJsonArray("items");
        for (int i = lowerBaund; i < upperBaund; i++) {
            JsonElement je = ja.get(i);
            JsonObject item = je.getAsJsonObject();
            String name = item.get("name").getAsString();
            System.out.println(name);
        }
        printPagesStatus();
    }

    protected static void parseAndPrintNew(){
        JsonObject jo = JsonParser.parseString(response.body()).getAsJsonObject();
        JsonObject albumsObject = jo.getAsJsonObject("albums");

        String albumName;
        ArrayList<String> artists = new ArrayList<>();
        String URL;

        JsonArray ja = albumsObject.getAsJsonArray("items");
        for (int i = lowerBaund; i < upperBaund; i++) {
            JsonElement je = ja.get(i);
            JsonObject item = je.getAsJsonObject();
            //Get album name
            albumName = item.get("name").getAsString();
            //Get album URL
            JsonObject externalURLs = item.getAsJsonObject("external_urls");
            URL = externalURLs.get("spotify").getAsString();
            //Get album authors
            for (JsonElement artist: item.getAsJsonArray("artists")) {
                JsonObject artist1 = artist.getAsJsonObject();
                artists.add(artist1.get("name").getAsString());
            }

            //Print results
            System.out.println(albumName);
            System.out.println(artists);
            System.out.println(URL);
            System.out.println();

            //resetVariables
            artists.clear();
        }
        printPagesStatus();
    }

    public static void parseAndPrintPlaylistOrFetured(){
        JsonObject jo = JsonParser.parseString(response.body()).getAsJsonObject();

        if (response.body().contains("error")) {
            JsonObject json = JsonParser.parseString(response.body()).getAsJsonObject();
            JsonObject error = json.getAsJsonObject("error");
            System.out.println(error.get("message").getAsString());
            return;
        }

        try {
            JsonObject error = jo.get("error").getAsJsonObject();
            if (error != null) {
                System.out.println("Unknown category name.");
                return;
            }
        } catch (NullPointerException ignored) {

        }


        JsonObject playlistObject = jo.getAsJsonObject("playlists");

        String playlistName;
        String URL;

        JsonArray ja = playlistObject.getAsJsonArray("items");
        for (int i = lowerBaund; i < upperBaund; i++) {
            JsonElement je = ja.get(i);
            JsonObject item = je.getAsJsonObject();

            playlistName = item.get("name").getAsString();

            JsonObject externalURLs = item.getAsJsonObject("external_urls");
            URL = externalURLs.get("spotify").getAsString();

            System.out.println(playlistName);
            System.out.println(URL);
            System.out.println();
        }
        printPagesStatus();
    }

    private static void parseAndPrintDecizion() {
        switch (state) {
            case CATEGORIES:
                parseAndPrintCategories();
                break;
            case PLAYLISTS: case FEATURED:
                parseAndPrintPlaylistOrFetured();
                break;
            case NEW:
                parseAndPrintNew();
                break;
        }
    }

    public static void next() {
        if (currentPage >= pagesNumber) {
            System.out.println("No more pages.");
            return;
        }
        currentPage++;
        calcuateBaunds();
        parseAndPrintDecizion();
    }

    public static void prev() {
        if (currentPage <= 1) {
            System.out.println("No more pages.");
            return;
        }
        currentPage--;
        calcuateBaunds();
        parseAndPrintDecizion();
    }

    private static void printPagesStatus() {
        System.out.printf("---PAGE %d OF %d---\n", currentPage, pagesNumber);
    }

    private static void calcuateBaunds() {
        lowerBaund = (currentPage - 1) * Config.resultsNumber;//pagesNumber;
        upperBaund = Math.min(lowerBaund + Config.resultsNumber, numberOfItems ); // changed -1
        //TODO change
        System.out.println("lower: " + lowerBaund + " upper: " + upperBaund);
    }

    private static void calculatePagesNumber() {
        pagesNumber = numberOfItems / Config.resultsNumber;
        //System.out.println(numberOfItems);
        if (pagesNumber % Config.resultsNumber == 0) {
            //pagesNumber++;
        }
    }

    private static void findResultsNumber() {
        JsonObject jo = JsonParser.parseString(response.body()).getAsJsonObject();
        switch (state) {
            case CATEGORIES:
                JsonObject categories = jo.getAsJsonObject("categories");
                numberOfItems = categories.getAsJsonArray("items").size();
                break;
            case FEATURED: case PLAYLISTS:
                JsonObject playlistObject = jo.getAsJsonObject("playlists");
                numberOfItems = playlistObject.getAsJsonArray("items").size();
                break;
            case NEW:
                JsonObject albumsObject = jo.getAsJsonObject("albums");
                numberOfItems = albumsObject.getAsJsonArray("items").size();
                break;
        }
    }
}