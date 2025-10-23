package dataaccess;

import model.UserData;

public interface UserDAO extends BaseDAO {

    UserData getUser(String username) throws Exception;
    UserData createUser(UserData userData) throws Exception;
    void clearUsers() throws Exception;
}
