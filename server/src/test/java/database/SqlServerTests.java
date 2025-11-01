package database;

import dataaccess.auth.AuthDAO;
import dataaccess.auth.MySqlAuthDAO;
import dataaccess.game.GameDAO;
import dataaccess.game.MySqlGameDAO;
import dataaccess.user.MySqlUserDAO;
import dataaccess.user.UserDAO;
import service.AuthService;
import service.GameService;
import service.UserService;

public class SqlServerTests {

    UserService testUserService;
    AuthService testAuthService;
    GameService testGameService;

    public SqlServerTests() {
        try {
            UserDAO sqlUserDao = new MySqlUserDAO();
            AuthDAO sqlAuthDao = new MySqlAuthDAO();
            GameDAO sqlGameDao = new MySqlGameDAO();

            this.testAuthService = new AuthService(sqlUserDao, sqlAuthDao);
            this.testUserService = new UserService(sqlUserDao, sqlAuthDao);
            this.testGameService = new GameService(sqlGameDao, sqlAuthDao);
        } catch (Exception e) {
            throw new RuntimeException(e.getMessage());
        }
    }

    public SqlServerTests(UserDAO userDAO) {
        try {
            AuthDAO sqlAuthDao = new MySqlAuthDAO();
            GameDAO sqlGameDao = new MySqlGameDAO();

            this.testAuthService = new AuthService(userDAO, sqlAuthDao);
            this.testUserService = new UserService(userDAO, sqlAuthDao);
            this.testGameService = new GameService(sqlGameDao, sqlAuthDao);
        } catch (Exception e) {
            throw new RuntimeException(e.getMessage());
        }
    }

    public SqlServerTests(UserDAO userDAO, AuthDAO authDAO, GameDAO gameDAO) {
        this.testAuthService = new AuthService(userDAO, authDAO);
        this.testUserService = new UserService(userDAO, authDAO);
        this.testGameService = new GameService(gameDAO, authDAO);
    }
}
