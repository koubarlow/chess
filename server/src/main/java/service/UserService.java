package service;

import dataaccess.*;
import io.javalin.http.Context;
import model.*;

public class UserService {

    private final UserDAO userDAO;
    private final AuthDAO authDAO;

    public UserService(UserDAO userDAO, AuthDAO authDAO) {
        this.userDAO = userDAO;
        this.authDAO = authDAO;
    }

    public AuthData register(RegisterRequest registerRequest) throws Exception {

        if (getUser(registerRequest.username()) != null) { throw new AlreadyTakenException("Error: already taken"); }
        if (registerRequest.username() == null || registerRequest.password() == null || registerRequest.email() == null) {
            throw new BadRequestException("error: bad request");
        }
        UserData user = new UserData(registerRequest.username(), registerRequest.password(), registerRequest.email());
        this.userDAO.createUser(user);
        return this.authDAO.login(new LoginRequest(registerRequest.username(), registerRequest.password()));
    }

    public UserData getUser(String username) throws Exception {
        if (username == null) { throw new BadRequestException("Error: bad request"); }
        return this.userDAO.getUser(username);
    }
}
