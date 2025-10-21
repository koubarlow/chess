package server;

import dataaccess.MemoryUserDAO;
import io.javalin.*;
import service.UserService;

public class Server {

    private final Javalin javalin;

    public Server() {
        UserServerHelper userServerHelper = new UserServerHelper(new UserService(new MemoryUserDAO()));

        javalin = Javalin.create(config -> config.staticFiles.add("web"))
                .post("/user", userServerHelper::register);
    }

    public int run(int desiredPort) {
        javalin.start(desiredPort);
        return javalin.port();
    }

    public void stop() {
        javalin.stop();
    }
}
