package dataaccess.auth;

import dataaccess.BaseDAO;
import dataaccess.exceptions.UnauthorizedException;
import model.AuthData;
import model.LoginRequest;
import model.UserData;

import java.util.Objects;

public interface AuthDAO extends BaseDAO {

    AuthData createAuth(LoginRequest loginRequest, UserData existingUser) throws Exception;
    default boolean authenticateUser(LoginRequest request, UserData existingUser) throws Exception {
        String username = request.username();
        String password = request.password();

        if (existingUser == null) { throw new UnauthorizedException("Error: unauthorized"); }
        return Objects.equals(existingUser.username(), username) && Objects.equals(existingUser.password(), password);
    }
    boolean sessionExistsForAuthToken(String authToken) throws Exception;
    String getUsername(String authToken) throws Exception;
    void logout(String authToken) throws Exception;
    void clearAuth() throws Exception;
}
