package server;

import model.*;

import java.net.http.HttpClient;

public class ServerFacade {

    private final HttpClient client = HttpClient.newHttpClient();
    private final String serverUrl;

    public ServerFacade(String url) {
        serverUrl = url;
    }

    public void register(RegisterRequest) throws Exception {
        throw new UnsupportedOperationException("Not Implemented");
    }

    public void login(LoginRequest) throws Exception {
        throw new UnsupportedOperationException("Not Implemented");
    }

    public void logout(LogoutRequest) throws Exception {
        throw new UnsupportedOperationException("Not Implemented");
    }

    public void createGame(CreateGameRequest) throws Exception {
        throw new UnsupportedOperationException("Not Implemented");
    }

    public GameList listGames(String authToken) throws Exception {
        throw new UnsupportedOperationException("Not Implemented");
    }

    public void joinGame(JoinGameRequest) throws Exception {
        throw new UnsupportedOperationException("Not Implemented");
    }

    public void clearApplication(String authToken) throws Exception {
        throw new UnsupportedOperationException("Not Implemented");
    }
}
