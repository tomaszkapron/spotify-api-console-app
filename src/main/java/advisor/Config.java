package advisor;

public class Config {
    static final String clientID = "7217f2b1d3dc44cb8222bbb9c4525d83";
    static final String clientSecret = "b81ddadac8644181b9381f03dc5389a0";
    static final String redirectURI = "http://localhost:8080";

    static String SpotifyApiServerPath;
    static String SpotifyAccessServerPoint;
    static int resultsNumber;

    static String Token;

    public static void setSpotifyAccessServerPoint(String spotifyAccessServerPoint) {
        SpotifyAccessServerPoint = spotifyAccessServerPoint;
    }

    public static void setSpotifyApiServerPath(String spotifyApiServerPath) {
        SpotifyApiServerPath = spotifyApiServerPath;
    }

    public static void setToken(String token) {
        Token = token;
    }

    public static void setResultsNumber(int resultsNum) {
        resultsNumber = resultsNum;
    }
}