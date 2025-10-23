package service;

import dataaccess.AuthDAO;
import dataaccess.BadRequestException;
import dataaccess.DataAccessException;
import model.*;

public class AuthService {

    private final AuthDAO authDAO;

    public AuthService(AuthDAO authDAO) {
        this.authDAO = authDAO;
    }

    public AuthData login(LoginRequest loginRequest) throws Exception {
        if (loginRequest.username() == null || loginRequest.password() == null) {
            throw new BadRequestException("Error: bad request");
        }
        return this.authDAO.login(loginRequest);
    }

    public void logout(LogoutRequest logoutRequest) throws Exception {
        this.authDAO.logout(logoutRequest.authToken());
    }
}
