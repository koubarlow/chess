package server;

import dataaccess.auth.AuthDAO;
import dataaccess.auth.MemoryAuthDAO;
import dataaccess.auth.MySqlAuthDAO;
import dataaccess.game.GameDAO;
import dataaccess.game.MemoryGameDAO;
import dataaccess.game.MySqlGameDAO;
import dataaccess.user.MemoryUserDAO;
import dataaccess.user.MySqlUserDAO;
import dataaccess.user.UserDAO;
import exception.ResponseException;
import io.javalin.*;
import service.AuthService;
import service.GameService;
import service.UserService;

public class Server {

    private final Javalin javalin;
    public static String authTokenHeader = "authorization";

    public Server() {
        UserDAO userDAO = new MemoryUserDAO();
        AuthDAO authDAO = new MemoryAuthDAO();
        GameDAO gameDAO = new MemoryGameDAO();

        try {
            userDAO = new MySqlUserDAO();
            authDAO = new MySqlAuthDAO();
            gameDAO = new MySqlGameDAO();
        } catch (Exception e) {
            throw new RuntimeException(e);
        }

        UserService userService = new UserService(userDAO, authDAO);
        AuthService authService = new AuthService(userDAO, authDAO);
        GameService gameService = new GameService(gameDAO, authDAO);

        UserServerHelper userServerHelper = new UserServerHelper(userService);
        SessionServerHelper sessionServerHelper = new SessionServerHelper(authService);
        GameServerHelper gameServerHelper = new GameServerHelper(gameService);
        ClearServerHelper clearServerHelper = new ClearServerHelper(gameService, authService, userService);

        javalin = Javalin.create(config -> config.staticFiles.add("web"))
                .post("/user", userServerHelper::register)
                .post("/session", sessionServerHelper::login)
                .delete("/session", sessionServerHelper::logout)
                .post("/game", gameServerHelper::createGame)
                .get("/game", gameServerHelper::listGames)
                .put("/game", gameServerHelper::updateGame)
                .delete("/db", clearServerHelper::clearApplication)
                .ws("/ws", ws -> {
                   ws.onConnect(webSocketHandler);
                   ws.onMessage(webSocketHandler);
                   ws.onClose(webSocketHandler);
                });
    }

    public int run(int desiredPort) {
        javalin.start(desiredPort);
        return javalin.port();
    }

    public void stop() {
        javalin.stop();
    }
}
