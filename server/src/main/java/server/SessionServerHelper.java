package server;

import com.google.gson.Gson;
import dataaccess.AuthDAO;
import dataaccess.DataAccessException;
import io.javalin.http.Context;
import model.*;
import service.AuthService;
import service.UserService;

public class SessionServerHelper {

    private final AuthService authService;
    private final String AUTH_TOKEN_HEADER = "authorization";

    public SessionServerHelper(AuthService authService) {
        this.authService = authService;
    }

    public void login(Context context) throws DataAccessException {
        LoginRequest loginRequest = new Gson().fromJson(context.body(), LoginRequest.class);
        AuthData authenticatedUser = authService.login(loginRequest);
        context.header(AUTH_TOKEN_HEADER, authenticatedUser.authToken());
        context.json(new Gson().toJson(authenticatedUser));
    }

    // Need to just pass things via header
    public void logout(Context context) throws DataAccessException {
        String authToken = context.header(AUTH_TOKEN_HEADER);
        LogoutRequest logoutRequest = new LogoutRequest(authToken);
        authService.logout(logoutRequest);
    }
}
