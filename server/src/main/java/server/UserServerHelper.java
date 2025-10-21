package server;

import com.google.gson.Gson;
import dataaccess.DataAccessException;
import io.javalin.http.Context;
import model.RegisterRequest;

import model.UserData;
import service.UserService;

public class UserServerHelper {

    private final UserService userService;

    public UserServerHelper(UserService userService) {
        this.userService = userService;
    }

    public void register(Context context) throws DataAccessException {
        RegisterRequest registerRequest = new Gson().fromJson(context.body(), RegisterRequest.class);
        UserData user = userService.register(registerRequest);
        context.json(new Gson().toJson(user));
    }

//    public void getUser(Context context) throws DataAccessException {
//        GetUserRequest getUserRequest = new Gson().fromJson(context.body(), GetUserRequest.class);
//        context.json(userService.getUser(getUserRequest.username()));
//    }
}
