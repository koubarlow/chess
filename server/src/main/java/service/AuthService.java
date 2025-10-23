package service;

import dataaccess.AuthDAO;
import dataaccess.DataAccessException;
import model.*;

public class AuthService {

    private final AuthDAO authDAO;

    public AuthService(AuthDAO authDAO) {
        this.authDAO = authDAO;
    }

    public AuthData login(LoginRequest loginRequest) throws Exception {
        return this.authDAO.login(loginRequest);
    }

    public void logout(LogoutRequest logoutRequest) throws Exception {
        this.authDAO.logout(logoutRequest.authToken());
    }
}
