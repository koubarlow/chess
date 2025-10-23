package service;

import dataaccess.*;
import server.ClearApplicationServerHelper;
import server.GameServerHelper;
import server.SessionServerHelper;
import server.UserServerHelper;

public class FakeServer {

    UserService fakeUserService;
    AuthService fakeAuthService;
    GameService fakeGameService;

    public FakeServer() {
        UserDAO fakeMemoryUserDAO = new MemoryUserDAO();
        AuthDAO fakeMemoryAuthDAO = new MemoryAuthDAO();
        GameDAO fakeMemoryGameDAO = new MemoryGameDAO();

        this.fakeAuthService = new AuthService(fakeMemoryUserDAO, fakeMemoryAuthDAO);
        this.fakeUserService = new UserService(fakeMemoryUserDAO, fakeMemoryAuthDAO);
        this.fakeGameService = new GameService(fakeMemoryGameDAO, fakeMemoryAuthDAO, fakeMemoryUserDAO);
    }

    public FakeServer(UserDAO userDAO) {
        AuthDAO fakeMemoryAuthDAO = new MemoryAuthDAO();
        GameDAO fakeMemoryGameDAO = new MemoryGameDAO();

        this.fakeAuthService = new AuthService(userDAO, fakeMemoryAuthDAO);
        this.fakeUserService = new UserService(userDAO, fakeMemoryAuthDAO);
        this.fakeGameService = new GameService(fakeMemoryGameDAO, fakeMemoryAuthDAO, userDAO);
    }
}
