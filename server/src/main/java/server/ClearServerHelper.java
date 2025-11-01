package server;

import dataaccess.exceptions.DataAccessException;
import io.javalin.http.Context;
import service.AuthService;
import service.GameService;
import service.UserService;

public class ClearServerHelper {

    private final GameService gameService;
    private final AuthService authService;
    private final UserService userService;

    public ClearServerHelper(GameService gameService, AuthService authService, UserService userService) {
        this.gameService = gameService;
        this.authService = authService;
        this.userService = userService;
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
