package advisor;

import java.util.Objects;

import static java.lang.Integer.parseInt;

public class Main {
    public static void main(String[] args) {
        String accessServerPoint = "https://accounts.spotify.com";
        String apiServerPath = "https://api.spotify.com";
        int resultsNum = 5;
        if (args.length == 6) {
            if (Objects.equals(args[0], "-access")) {
                accessServerPoint = args[1];
            }
            if (Objects.equals(args[2], "-resource")) {
                apiServerPath = args[3];
            }
            if (Objects.equals(args[4], "-page")) {
                resultsNum = parseInt(args[5]);
            }
        }

        Config.setSpotifyAccessServerPoint(accessServerPoint);
        Config.setSpotifyApiServerPath(apiServerPath);
        System.out.println("resultsNum: " + resultsNum);
        Config.setResultsNumber(resultsNum);

        AdvisorInterface advisorInterface = new AdvisorInterface();
        advisorInterface.runAdvisor();
    }
}