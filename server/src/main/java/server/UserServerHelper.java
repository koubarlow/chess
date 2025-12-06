package server;

import com.google.gson.Gson;
import dataaccess.exceptions.AlreadyTakenException;
import dataaccess.exceptions.BadRequestException;
import dataaccess.exceptions.DataAccessException;
import io.javalin.http.Context;
import model.AuthData;
import model.RegisterRequest;

import server.websocket.WebSocketHandler;
import service.UserService;

public class UserServerHelper {

    private final UserService userService;
    private final WebSocketHandler webSocketHandler;

    public UserServerHelper(UserService userService, WebSocketHandler webSocketHandler) {
        this.userService = userService;
        this.webSocketHandler = webSocketHandler;
    }

    public void register(Context context) throws Exception {

        try {
            RegisterRequest registerRequest = new Gson().fromJson(context.body(), RegisterRequest.class);
            AuthData authData = userService.register(registerRequest);
            context.json(new Gson().toJson(authData));
            context.status(200);
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
