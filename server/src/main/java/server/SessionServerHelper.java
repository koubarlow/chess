package server;

import com.google.gson.Gson;
import dataaccess.BadRequestException;
import dataaccess.DataAccessException;
import dataaccess.UnauthorizedException;
import io.javalin.http.Context;
import model.*;
import service.AuthService;
import service.UserService;

public class SessionServerHelper {

    private final AuthService authService;

    public SessionServerHelper(AuthService authService) {
        this.authService = authService;
    }

    public void login(Context context) throws Exception {
        try {
            LoginRequest loginRequest = new Gson().fromJson(context.body(), LoginRequest.class);
            AuthData authenticatedUser = authService.login(loginRequest);
            context.header(Server.AUTH_TOKEN_HEADER, authenticatedUser.authToken());
            context.json(new Gson().toJson(authenticatedUser));
            context.status(200);
        } catch (BadRequestException e) {
            context.json(e.toJson());
            context.status(400);
        } catch (UnauthorizedException e) {
            context.json(e.toJson());
            context.status(401);
        }
    }

    // Need to just pass things via header
    public void logout(Context context) throws Exception {
        try {
            String authToken = context.header(Server.AUTH_TOKEN_HEADER);
            LogoutRequest logoutRequest = new LogoutRequest(authToken);
            authService.logout(logoutRequest);
        } catch (UnauthorizedException e) {
            context.json(e.toJson());
            context.status(401);
        }
    }
}
