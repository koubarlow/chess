package service;

import dataaccess.DataAccessException;
import dataaccess.UserDAO;
import io.javalin.http.Context;
import model.*;

public class UserService {

    private final UserDAO userDAO;

    public UserService(UserDAO userDAO) {
        this.userDAO = userDAO;
    }

    public UserData register(RegisterRequest registerRequest) throws Exception {

        UserData user = new UserData(registerRequest.username(), registerRequest.password(), registerRequest.email());
        return this.userDAO.createUser(user);
    }

    public UserData getUser(String username) throws Exception {
        return this.userDAO.getUser(username);
    }

    private void validateRegisterRequestParameters(RegisterRequest req) throws Exception {
        if (req.username() == null || req.password() == null || req.email() == null) {
            throw new DataAccessException("bad request");
        }
    }
}
