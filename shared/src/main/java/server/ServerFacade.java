package server;

import com.google.gson.Gson;
import model.*;

import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpRequest.BodyPublisher;
import java.net.http.HttpRequest.BodyPublishers;
import java.net.http.HttpResponse;

public class ServerFacade {

    private final HttpClient client = HttpClient.newHttpClient();
    private final String serverUrl;

    public ServerFacade(String url) {
        serverUrl = url;
    }

    public void register(RegisterRequest registerRequest) throws Exception {
        var request = buildRequest("POST", "/user", registerRequest);
        var response = sendRequest(request);
        handleResponse(response, null);
    }

    public void login(LoginRequest loginRequest) throws Exception {
        var request = buildRequest("POST", "/session", loginRequest);
        var response = sendRequest(request);
        handleResponse(response, null);
    }

    public void logout() throws Exception {
        var request = buildRequest("DELETE", "/session", null);
        var response = sendRequest(request);
        handleResponse(response, null);
    }

    public void createGame(CreateGameRequest createGameRequest) throws Exception {
        var request = buildRequest("POST", "/game", createGameRequest);
        var response = sendRequest(request);
        handleResponse(response, null);
    }

    public GameList listGames() throws Exception {
        var request = buildRequest("GET", "/game", null);
        var response = sendRequest(request);
        return handleResponse(response, GameList.class);
    }

    public void joinGame(JoinGameRequest joinGameRequest) throws Exception {
        var request = buildRequest("PUT", "/game", joinGameRequest);
        var response = sendRequest(request);
        handleResponse(response, null);
    }

    public void clearApplication(String authToken) throws Exception {
        var request = buildRequest("DELETE", "/db", null);
        var response = sendRequest(request);
        handleResponse(response, null);
    }

    private HttpRequest buildRequest(String method, String path, Object body) {
        var request = HttpRequest.newBuilder()
                .uri(URI.create(serverUrl + path))
                .method(method, makeRequestBody(body));
        if (body != null) {
            request.setHeader("Content-Type", "application/json");
        }
        return request.build();
    }

    private BodyPublisher makeRequestBody(Object request) {
        if (request != null) {
            return BodyPublishers.ofString(new Gson().toJson(request));
        } else {
            return BodyPublishers.noBody();
        }
    }

    private HttpResponse<String> sendRequest(HttpRequest request) throws Exception {
        try {
            return client.send(request, HttpResponse.BodyHandlers.ofString());
        } catch (Exception ex) {
            throw new Exception(ex.getMessage());
        }
    }

    private <T> T handleResponse(HttpResponse<String> response, Class<T> responseClass) throws Exception {
        var status = response.statusCode();
        if (!isSuccessful(status)) {
            var body = response.body();
            if (body != null) {
                throw new Exception("Body is null");
            }

            throw new Exception("handle response, other failure: " + status);
        }

        if (responseClass != null) {
            return new Gson().fromJson(response.body(), responseClass);
        }

        return null;
    }

    private boolean isSuccessful(int status) { return status / 100 == 2; }
}
