package server;

import dataaccess.exceptions.DataAccessException;
import io.javalin.http.Context;
import server.websocket.WebSocketHandler;
import service.AuthService;
import service.GameService;
import service.UserService;

public class ClearServerHelper {

    private final GameService gameService;
    private final AuthService authService;
    private final UserService userService;
    private final WebSocketHandler webSocketHandler;

    public ClearServerHelper(GameService gameService, AuthService authService, UserService userService, WebSocketHandler webSocketHandler) {
        this.gameService = gameService;
        this.authService = authService;
        this.userService = userService;
        this.webSocketHandler = webSocketHandler;
    }

    public void clearApplication(Context context) throws Exception {
        try {
            this.gameService.clearGames();
            this.authService.clearAuth();
            this.userService.clearUsers();
        } catch (DataAccessException e) {
            context.json(e.toJson());
            context.status(500);
        }
    }
}
