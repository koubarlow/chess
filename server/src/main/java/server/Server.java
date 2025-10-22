package server;

import dataaccess.*;
import io.javalin.*;
import service.AuthService;
import service.ClearApplicationService;
import service.GameService;
import service.UserService;

public class Server {

    private final Javalin javalin;
    public static String AUTH_TOKEN_HEADER = "authorization";

    public Server() {
        UserDAO memoryUserDAO = new MemoryUserDAO();
        AuthDAO memoryAuthDAO = new MemoryAuthDAO(memoryUserDAO);
        GameDAO memoryGameDAO = new MemoryGameDAO();
        ClearApplicationDAO clearApplicationDAO = new MemoryClearApplicationDAO(memoryGameDAO, memoryUserDAO, memoryAuthDAO);

        UserService userService = new UserService(memoryUserDAO);
        AuthService authService = new AuthService(memoryAuthDAO);
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
