package dataaccess.auth;

import dataaccess.BaseDAO;
import dataaccess.exceptions.UnauthorizedException;
import model.AuthData;
import model.LoginRequest;
import model.UserData;
import org.mindrot.jbcrypt.BCrypt;

import java.util.Objects;

public interface AuthDAO extends BaseDAO {

    AuthData createAuth(LoginRequest loginRequest, UserData existingUser) throws Exception;
    default boolean authenticateUser(LoginRequest request, UserData existingUser) throws Exception {
        String username = request.username();
        String password = request.password();

        if (existingUser == null || ! Objects.equals(existingUser.username(), username)) { throw new UnauthorizedException("Error: unauthorized"); }
        return BCrypt.checkpw(password, existingUser.password());
    }
    boolean sessionExistsForAuthToken(String authToken) throws Exception;
    String getUsername(String authToken) throws Exception;
    void logout(String authToken) throws Exception;
    void clearAuth() throws Exception;
}
