package advisor;

import java.io.IOException;
import java.util.Locale;
import java.util.Scanner;

enum State {
    FEATURED("featured"),
    NEW("new"),
    CATEGORIES("categories"),
    PLAYLISTS("playlists"),
    EXIT("exit"),
    AUTH("auth"),
    NEXT("next"),
    PREV("prev");

    public final String label;

    State(String label) {
        this.label = label;
    }
}

public class AdvisorInterface {
    private boolean EXIT;
    private boolean isAuthorised;
    private SpotifyControler spotifyControler;

    public void setCategory(String category) {
        this.category = this.spotifyControler.getCategoryIDfromName(category);
    }

    String category;

    public AdvisorInterface() {
        this.EXIT = false;
        this.isAuthorised = false;
        this.spotifyControler = new SpotifyControler();
    }

    public void runAdvisor() {
        while (!this.EXIT) {
            //main loop
            State state = takeInput();

            if(this.isAuthorised) {
                loggedInOptions(state);
            } else {
                loggedOutOptions(state);
            }

        }
    }

    private void authorize() {
        try {
            SpotifyConnectivity spotifyConectivity = new SpotifyConnectivity();
            if(spotifyConectivity.authorize()) {
                this.isAuthorised = true;
                System.out.println("---SUCCESS---");
            }
            //TODO: change after passing
            this.isAuthorised = true;
        } catch (IOException | InterruptedException ex) {
            System.out.println(ex.getMessage());
        }
    }

    private void loggedInOptions(State state) {
        switch (state) {
            case NEW:
                this.spotifyControler.get("/v1/browse/new-releases", State.NEW);
                break;

            case FEATURED:
                this.spotifyControler.get("/v1/browse/featured-playlists", State.FEATURED);
                break;

            case PLAYLISTS:
                System.out.println("category: " + this.category);
                this.spotifyControler.get("/v1/browse/categories/" + this.category + "/playlists", State.PLAYLISTS);
                break;

            case CATEGORIES:
                this.spotifyControler.get("/v1/browse/categories", State.CATEGORIES);
                break;

            case EXIT:
                System.out.println("---GOODBYE!---");
                this.EXIT = true;
                break;

            case NEXT:
                View.next();
                break;

            case PREV:
                View.prev();
                break;
        }
    }

    private void loggedOutOptions(State state) {
        switch (state) {
            case EXIT:
                System.out.println("---GOODBYE!---");
                this.EXIT = true;
                break;
            case AUTH:
                this.authorize();
                break;
            default:
                System.out.println("Please, provide access for application.");
                break;
        }
    }



    private State takeInput() {
        Scanner scanner = new Scanner(System.in);
        String request;
        String requestState;
        while(true) {
            request = scanner.nextLine();
            requestState = request.split(" ", 2)[0];
            for (State s : State.values()) {
                if (s.label.equals(requestState)) {
                    if (s == State.PLAYLISTS) {
                        String category = request
                                .replace("playlists ", "");

                        setCategory(category);
                    }
                    return s;
                }
            }
        }
    }
}