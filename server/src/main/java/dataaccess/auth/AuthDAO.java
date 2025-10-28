package dataaccess.auth;

import dataaccess.BaseDAO;
import model.AuthData;
import model.LoginRequest;
import model.UserData;

public interface AuthDAO extends BaseDAO {

    AuthData login(LoginRequest loginRequest, UserData existingUser) throws Exception;
    boolean sessionExistsForAuthToken(String authToken);
    String getUsername(String authToken);
    boolean authenticateUser(LoginRequest loginRequest, UserData existingUser) throws Exception;
    void logout(String authToken) throws Exception;
    void clearSessions() throws Exception;
}
