package dataaccess;

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
        this.authDAO.clearSessions();
    }
}
