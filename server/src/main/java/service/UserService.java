package service;

import dataaccess.auth.AuthDAO;
import dataaccess.exceptions.AlreadyTakenException;
import dataaccess.exceptions.BadRequestException;
import dataaccess.user.UserDAO;
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
        LoginRequest req = new LoginRequest(registerRequest.username(), registerRequest.password());
        return this.authDAO.login(req, this.userDAO.getUser(registerRequest.username()));
    }

    public UserData getUser(String username) throws Exception {
        if (username == null) { throw new BadRequestException("Error: bad request"); }
        return this.userDAO.getUser(username);
    }
}
