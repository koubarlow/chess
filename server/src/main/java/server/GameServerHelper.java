package server;

import com.google.gson.Gson;
import dataaccess.exceptions.AlreadyTakenException;
import dataaccess.exceptions.BadRequestException;
import dataaccess.exceptions.DataAccessException;
import dataaccess.exceptions.UnauthorizedException;
import io.javalin.http.Context;
import model.*;
import server.websocket.WebSocketHandler;
import service.GameService;

public class GameServerHelper {

    private final GameService gameService;
    private final WebSocketHandler webSocketHandler;

    public GameServerHelper(GameService gameService, WebSocketHandler webSocketHandler) {
        this.gameService = gameService;
        this.webSocketHandler = webSocketHandler;
    }

    public void createGame(Context context) throws Exception {
        try {
            String authToken = context.header(Server.authTokenHeader);
            CreateGameRequest createGameRequest = new Gson().fromJson(context.body(), CreateGameRequest.class);
            GameData game = gameService.createGame(authToken, createGameRequest);
            context.json(new Gson().toJson(game));
        } catch (BadRequestException e) {
            context.json(e.toJson());
            context.status(400);
        } catch (UnauthorizedException e) {
            context.json(e.toJson());
            context.status(401);
        } catch (DataAccessException e) {
            context.json(e.toJson());
            context.status(500);
        }
    }

    public void listGames(Context context) throws Exception {
        try {
            String authToken = context.header(Server.authTokenHeader);
            GameList games = gameService.listGames(authToken);
            context.json(games.toJson(), GameList.class);
        } catch (UnauthorizedException e) {
            context.json(e.toJson());
            context.status(401);
        } catch (DataAccessException e) {
            context.json(e.toJson());
            context.status(500);
        }
    }

    public void updateGame(Context context) throws Exception {
        try {
            String authToken = context.header(Server.authTokenHeader);
            UpdateGameRequest updateGameRequest = new Gson().fromJson(context.body(), UpdateGameRequest.class);
            gameService.updateGame(authToken, updateGameRequest);
            if (updateGameRequest.gameData() != null) {

            }
        } catch (UnauthorizedException e) {
            context.json(e.toJson());
            context.status(401);
        } catch (BadRequestException e) {
            context.json(e.toJson());
            context.status(400);
        } catch (AlreadyTakenException e) {
            context.json(e.toJson());
            context.status(403);
        } catch (DataAccessException e) {
            context.json(e.toJson());
            context.status(500);
        }
    }
}
