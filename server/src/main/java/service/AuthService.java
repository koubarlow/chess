package service;

import dataaccess.AuthDAO;
import dataaccess.DataAccessException;
import dataaccess.UserDAO;
import model.*;

import java.util.UUID;

public class AuthService {

    private final AuthDAO authDAO;

    public AuthService(AuthDAO authDAO) {
        this.authDAO = authDAO;
    }

    public AuthData login(LoginRequest loginRequest) throws DataAccessException {
        return this.authDAO.login(loginRequest);
    }

    public void logout(LogoutRequest logoutRequest) {
        throw new UnsupportedOperationException("Not implemented yet");
    }
}
