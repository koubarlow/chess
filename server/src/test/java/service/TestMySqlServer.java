package service;

import dataaccess.auth.AuthDAO;
import dataaccess.auth.MemoryAuthDAO;
import dataaccess.game.GameDAO;
import dataaccess.game.MemoryGameDAO;
import dataaccess.user.MySqlUserDAO;
import dataaccess.user.UserDAO;

public class TestMySqlServer {

    UserService testSqlUserDAO;
    AuthService testSqlAuthDAO;
    GameService testSqlGameDAO;

//    public TestMySqlServer() {
//        UserDAO testSqlUserDAO = new MySqlUserDAO();
//        AuthDAO testSqlAuthDAO = new MemoryAuthDAO();
//        GameDAO testSqlGameDAO = new MemoryGameDAO();
//
//        this.fakeAuthService = new AuthService(fakeMemoryUserDAO, fakeMemoryAuthDAO);
//        this.fakeUserService = new UserService(fakeMemoryUserDAO, fakeMemoryAuthDAO);
//        this.fakeGameService = new GameService(fakeMemoryGameDAO, fakeMemoryAuthDAO);
//    }
}
