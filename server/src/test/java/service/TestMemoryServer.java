package service;

import dataaccess.auth.AuthDAO;
import dataaccess.auth.MemoryAuthDAO;
import dataaccess.game.GameDAO;
import dataaccess.game.MemoryGameDAO;
import dataaccess.user.MemoryUserDAO;
import dataaccess.user.UserDAO;

public class TestMemoryServer {

    UserService fakeUserService;
    AuthService fakeAuthService;
    GameService fakeGameService;

    public TestMemoryServer() {
        UserDAO fakeMemoryUserDAO = new MemoryUserDAO();
        AuthDAO fakeMemoryAuthDAO = new MemoryAuthDAO();
        GameDAO fakeMemoryGameDAO = new MemoryGameDAO();

        this.fakeAuthService = new AuthService(fakeMemoryUserDAO, fakeMemoryAuthDAO);
        this.fakeUserService = new UserService(fakeMemoryUserDAO, fakeMemoryAuthDAO);
        this.fakeGameService = new GameService(fakeMemoryGameDAO, fakeMemoryAuthDAO);
    }

    public TestMemoryServer(UserDAO userDAO) {
        AuthDAO fakeMemoryAuthDAO = new MemoryAuthDAO();
        GameDAO fakeMemoryGameDAO = new MemoryGameDAO();

        this.fakeAuthService = new AuthService(userDAO, fakeMemoryAuthDAO);
        this.fakeUserService = new UserService(userDAO, fakeMemoryAuthDAO);
        this.fakeGameService = new GameService(fakeMemoryGameDAO, fakeMemoryAuthDAO);
    }

    public TestMemoryServer(UserDAO userDAO, AuthDAO authDAO, GameDAO gameDAO) {
        this.fakeAuthService = new AuthService(userDAO, authDAO);
        this.fakeUserService = new UserService(userDAO, authDAO);
        this.fakeGameService = new GameService(gameDAO, authDAO);
    }
}
