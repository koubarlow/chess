package server;

import com.google.gson.Gson;
import dataaccess.exceptions.AlreadyTakenException;
import dataaccess.exceptions.BadRequestException;
import io.javalin.http.Context;
import model.AuthData;
import model.RegisterRequest;

import service.UserService;

public class UserServerHelper {

    private final UserService userService;

    public UserServerHelper(UserService userService) {
        this.userService = userService;
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
        }
    }
}
