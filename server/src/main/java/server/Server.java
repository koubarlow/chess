package server;

import dataaccess.MemoryAuthDAO;
import dataaccess.MemoryUserDAO;
import dataaccess.UserDAO;
import io.javalin.*;
import service.AuthService;
import service.UserService;

public class Server {

    private final Javalin javalin;

    public Server() {
        UserDAO memoryUserDAO = new MemoryUserDAO();
        UserService userService = new UserService(memoryUserDAO);
        AuthService authService = new AuthService(new MemoryAuthDAO(memoryUserDAO));

        UserServerHelper userServerHelper = new UserServerHelper(userService);
        SessionServerHelper sessionServerHelper = new SessionServerHelper(authService);

        javalin = Javalin.create(config -> config.staticFiles.add("web"))
                .post("/user", userServerHelper::register)
                .post("/session", sessionServerHelper::login)
                .delete("/session", sessionServerHelper::logout);
    }

    public int run(int desiredPort) {
        javalin.start(desiredPort);
        return javalin.port();
    }

    public void stop() {
        javalin.stop();
    }
}
