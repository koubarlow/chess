package server;

import dataaccess.auth.AuthDAO;
import dataaccess.auth.MemoryAuthDAO;
import dataaccess.clearapplication.ClearApplicationDAO;
import dataaccess.clearapplication.MemoryClearApplicationDAO;
import dataaccess.game.GameDAO;
import dataaccess.game.MemoryGameDAO;
import dataaccess.user.MemoryUserDAO;
import dataaccess.user.UserDAO;
import io.javalin.*;
import service.AuthService;
import service.ClearApplicationService;
import service.GameService;
import service.UserService;

public class Server {

    private final Javalin javalin;
    public static String authTokenHeader = "authorization";

    public Server() {
        UserDAO memoryUserDAO = new MemoryUserDAO();
        AuthDAO memoryAuthDAO = new MemoryAuthDAO();
        GameDAO memoryGameDAO = new MemoryGameDAO();
        ClearApplicationDAO clearApplicationDAO = new MemoryClearApplicationDAO(memoryGameDAO, memoryUserDAO, memoryAuthDAO);

        UserService userService = new UserService(memoryUserDAO, memoryAuthDAO);
        AuthService authService = new AuthService(memoryUserDAO, memoryAuthDAO);
        GameService gameService = new GameService(memoryGameDAO, memoryAuthDAO);
        ClearApplicationService clearApplicationService = new ClearApplicationService(clearApplicationDAO);

        UserServerHelper userServerHelper = new UserServerHelper(userService);
        SessionServerHelper sessionServerHelper = new SessionServerHelper(authService);
        GameServerHelper gameServerHelper = new GameServerHelper(gameService);
        ClearApplicationServerHelper clearApplicationServerHelper = new ClearApplicationServerHelper(clearApplicationService);

        javalin = Javalin.create(config -> config.staticFiles.add("web"))
                .post("/user", userServerHelper::register)
                .post("/session", sessionServerHelper::login)
                .delete("/session", sessionServerHelper::logout)
                .post("/game", gameServerHelper::createGame)
                .get("/game", gameServerHelper::listGames)
                .put("/game", gameServerHelper::joinGame)
                .delete("/db", clearApplicationServerHelper::clearApplication);
    }

    public int run(int desiredPort) {
        javalin.start(desiredPort);
        return javalin.port();
    }

    public void stop() {
        javalin.stop();
    }
}
