package server;

import com.google.gson.Gson;
import dataaccess.DataAccessException;
import io.javalin.http.Context;
import model.LoginRequest;
import model.RegisterRequest;
import model.UserData;
import service.UserService;

public class SessionServerHelper {

    private final UserService userService;

    public SessionServerHelper(UserService userService) {
        this.userService = userService;
    }

    public void login(Context context) throws DataAccessException {
        LoginRequest loginRequest = new Gson().fromJson(context.body(), LoginRequest.class);
        UserData user = .register(registerRequest);
        context.json(new Gson().toJson(user));
    }

//    public void getUser(Context context) throws DataAccessException {
//        GetUserRequest getUserRequest = new Gson().fromJson(context.body(), GetUserRequest.class);
//        context.json(userService.getUser(getUserRequest.username()));
//    }
}
