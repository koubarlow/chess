package dataaccess;

import dataaccess.auth.AuthDAO;
import dataaccess.auth.MemoryAuthDAO;
import dataaccess.auth.MySqlAuthDAO;

public class AuthDAOTest {

    private AuthDAO getDataAccess(Class<? extends AuthDAO> databaseClass) throws Exception {
        AuthDAO db;
        if (databaseClass.equals(MySqlAuthDAO.class)) {
            db = new MySqlAuthDAO();
        } else {
            db = new MemoryAuthDAO();
        }
        db.clearAuth();
        return db;
    }
}
