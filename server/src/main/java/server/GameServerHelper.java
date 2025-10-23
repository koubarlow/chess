package server;

import com.google.gson.Gson;
import dataaccess.AlreadyTakenException;
import dataaccess.BadRequestException;
import dataaccess.DataAccessException;
import dataaccess.UnauthorizedException;
import io.javalin.http.Context;
import model.*;
import service.GameService;

public class GameServerHelper {

    private final GameService gameService;

    public GameServerHelper(GameService gameService) { this.gameService = gameService; }

    public void createGame(Context context) throws Exception {
        try {
            String authToken = context.header(Server.AUTH_TOKEN_HEADER);
            CreateGameRequest createGameRequest = new Gson().fromJson(context.body(), CreateGameRequest.class);
            GameData game = gameService.createGame(authToken, createGameRequest);
            context.json(new Gson().toJson(game));
        } catch (BadRequestException e) {
            context.json(e.toJson());
            context.status(400);
        } catch (UnauthorizedException e) {
            context.json(e.toJson());
            context.status(401);
        }
    }

    public void listGames(Context context) throws Exception {
        try {
            String authToken = context.header(Server.AUTH_TOKEN_HEADER);
            GameList games = gameService.listGames(authToken);
            context.json(games.toJson(), GameList.class);
        } catch (UnauthorizedException e) {
            context.json(e.toJson());
            context.status(401);
        }
    }

    public void joinGame(Context context) throws Exception {
        try {
            String authToken = context.header(Server.AUTH_TOKEN_HEADER);
            JoinGameRequest joinGameRequest = new Gson().fromJson(context.body(), JoinGameRequest.class);
            gameService.joinGame(authToken, joinGameRequest);
        } catch (UnauthorizedException e) {
            context.json(e.toJson());
            context.status(401);
        } catch (BadRequestException e) {
            context.json(e.toJson());
            context.status(400);
        } catch (AlreadyTakenException e) {
            context.json(e.toJson());
            context.status(403);
        }
    }
}
