package service;

import dataaccess.DataAccessException;
import dataaccess.UserDAO;
import model.*;

public class UserService {

    private final UserDAO userDAO;

    public UserService(UserDAO userDAO) {
        this.userDAO = userDAO;
    }

    public UserData register(RegisterRequest registerRequest) throws DataAccessException {
        UserData user = this.getUser(registerRequest.username());

        if (user == null) {
            user = new UserData(registerRequest.username(), registerRequest.password(), registerRequest.email());
            return this.userDAO.createUser(user);
        }
        throw new DataAccessException("Username already taken");
    }

    public UserData getUser(String username) throws DataAccessException {
        return this.userDAO.getUser(username);
    }
}
