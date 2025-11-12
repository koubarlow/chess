package client;

import server.ServerFacade;

import static client.EscapeSequences.LOGO;

public class ChessClient {

    private String visitorName = null;
    private final ServerFacade server;
    private State state = State.SIGNEDOUT;

    public ChessClient(String serverUrl) throws Exception {
        server = new ServerFacade(serverUrl);
    }

    public void run() {
        System.out.println(LOGO + "Welcome to chess. Sign in to start.");
        System.out.println(help());


    }

    public String help() {
        if (state == State.SIGNEDOUT) {
            return """
                    - login <username> <password>
                    - register <username> <password> <email>
                    - quit
                    """;
        }
        return """
                - create game
                - list games
                - join game
                - logout
                - quit
                """;
    }
}
