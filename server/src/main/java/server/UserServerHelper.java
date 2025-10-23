package server;

import com.google.gson.Gson;
import dataaccess.AlreadyTakenException;
import dataaccess.BadRequestException;
import dataaccess.DataAccessException;
import dataaccess.UnauthorizedException;
import io.javalin.http.Context;
import model.AuthData;
import model.RegisterRequest;

import model.UserData;
import service.UserService;

import java.util.Objects;

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

//    public void getUser(Context context) throws DataAccessException {
//        GetUserRequest getUserRequest = new Gson().fromJson(context.body(), GetUserRequest.class);
//        context.json(userService.getUser(getUserRequest.username()));
//    }
}
