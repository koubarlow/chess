package server;

import com.google.gson.Gson;
import dataaccess.DataAccessException;
import io.javalin.http.Context;
import model.*;
import service.AuthService;

public class SessionServerHelper {

    private final AuthService authService;

    public SessionServerHelper(AuthService authService) {
        this.authService = authService;
    }

    public void login(Context context) throws DataAccessException {
        LoginRequest loginRequest = new Gson().fromJson(context.body(), LoginRequest.class);
        AuthData authenticatedUser = authService.login(loginRequest);
        context.header(Server.AUTH_TOKEN_HEADER, authenticatedUser.authToken());
        context.json(new Gson().toJson(authenticatedUser));
    }

    // Need to just pass things via header
    public void logout(Context context) throws DataAccessException {
        String authToken = context.header(Server.AUTH_TOKEN_HEADER);
        LogoutRequest logoutRequest = new LogoutRequest(authToken);
        authService.logout(logoutRequest);
    }
}
