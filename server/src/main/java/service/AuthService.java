package service;

import dataaccess.auth.AuthDAO;
import dataaccess.exceptions.BadRequestException;
import dataaccess.user.UserDAO;
import model.*;

public class AuthService {

    private final UserDAO userDAO;
    private final AuthDAO authDAO;

    public AuthService(UserDAO userDAO, AuthDAO authDAO) {
        this.userDAO = userDAO;
        this.authDAO = authDAO;
    }

    public AuthData login(LoginRequest loginRequest) throws Exception {
        if (loginRequest.username() == null || loginRequest.password() == null) {
            throw new BadRequestException("Error: bad request");
        }
        UserData existingUser = userDAO.getUser(loginRequest.username());
        return this.authDAO.login(loginRequest, existingUser);
    }

    public void logout(LogoutRequest logoutRequest) throws Exception {
        this.authDAO.logout(logoutRequest.authToken());
    }
}
