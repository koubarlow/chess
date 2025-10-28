package dataaccess.user;

import dataaccess.BaseDAO;
import model.UserData;

public interface UserDAO extends BaseDAO {

    UserData getUser(String username) throws Exception;
    UserData createUser(UserData userData) throws Exception;
    void clearUsers() throws Exception;
}
