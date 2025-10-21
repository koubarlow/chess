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
        UserData userData = new UserData(registerRequest.username(), registerRequest.password(), registerRequest.email());
        return this.userDAO.createUser(userData);
    }

    public UserData getUser(String username) throws DataAccessException {
        return this.userDAO.getUser(username);
    }

    public AuthData login(LoginRequest loginRequest) {
        throw new UnsupportedOperationException("Not implemented yet");
    }
    public void logout(LogoutRequest logoutRequest) {
        throw new UnsupportedOperationException("Not implemented yet");
    }
}
