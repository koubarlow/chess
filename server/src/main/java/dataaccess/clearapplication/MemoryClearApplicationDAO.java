package dataaccess.clearapplication;

import dataaccess.auth.AuthDAO;
import dataaccess.game.GameDAO;
import dataaccess.user.UserDAO;

public class MemoryClearApplicationDAO implements ClearApplicationDAO {

    GameDAO gameDAO;
    UserDAO userDAO;
    AuthDAO authDAO;

    public MemoryClearApplicationDAO(GameDAO gameDAO, UserDAO userDAO, AuthDAO authDAO) {
        this.gameDAO = gameDAO;
        this.userDAO = userDAO;
        this.authDAO = authDAO;
    }

    public void clearApplication() throws Exception {
        this.gameDAO.clearGames();
        this.userDAO.clearUsers();
        this.authDAO.clearAuth();
    }
}
