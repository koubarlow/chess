package server;

import com.google.gson.Gson;
import dataaccess.DataAccessException;
import io.javalin.http.Context;
import model.*;
import service.GameService;

public class GameServerHelper extends Server{

    private final GameService gameService;

    public GameServerHelper(GameService gameService) { this.gameService = gameService; }

    public void createGame(Context context) throws DataAccessException {
        String authToken = context.header(AUTH_TOKEN_HEADER);
        CreateGameRequest createGameRequest = new Gson().fromJson(context.body(), CreateGameRequest.class);
        GameData game = gameService.createGame(authToken, createGameRequest);
        context.json(new Gson().toJson(game));
    }

    public void listGames(Context context) throws DataAccessException {
        String authToken = context.header(AUTH_TOKEN_HEADER);
        GameList games = gameService.listGames(authToken);
        context.json(new Gson().toJson(games));
    }

    public void joinGame(Context context) throws DataAccessException {
        String authToken = context.header(AUTH_TOKEN_HEADER);
        JoinGameRequest joinGameRequest = new Gson().fromJson(context.body(), JoinGameRequest.class);
        gameService.joinGame(authToken, joinGameRequest);
    }
}
