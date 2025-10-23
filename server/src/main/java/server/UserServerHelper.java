package server;

import com.google.gson.Gson;
import dataaccess.DataAccessException;
import io.javalin.http.Context;
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

        RegisterRequest registerRequest = new Gson().fromJson(context.body(), RegisterRequest.class);

        UserData existingUser = userService.getUser(registerRequest.username());
        if (existingUser != null) {
//            ErrorResponse error = new ErrorResponse("Error: already taken");
//            context.json("");
            context.status(403);
        }

        if (registerRequest.email() == null || registerRequest.username() == null || registerRequest.password() == null) {
            context.status(400);
        }

        UserData user = userService.register(registerRequest);
        context.json(new Gson().toJson(user));
        context.status(200);
    }

//    public void getUser(Context context) throws DataAccessException {
//        GetUserRequest getUserRequest = new Gson().fromJson(context.body(), GetUserRequest.class);
//        context.json(userService.getUser(getUserRequest.username()));
//    }
}
