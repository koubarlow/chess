package dataaccess.auth;

import dataaccess.BaseDAO;
import dataaccess.DatabaseManager;
import dataaccess.exceptions.DataAccessException;
import dataaccess.exceptions.UnauthorizedException;
import model.AuthData;
import model.LoginRequest;
import model.UserData;

import java.sql.*;

public class MySqlAuthDAO implements AuthDAO {

    public MySqlAuthDAO() throws DataAccessException {
        String[] createStatements = {
                """
            CREATE TABLE IF NOT EXISTS  auth (
                `authToken` varchar(256) NOT NULL,
                `username` varchar(256) NOT NULL,
                PRIMARY KEY (`authToken`),
                INDEX(username)
            ) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci
            """
        };
        configureDatabase(createStatements);
    }

    public AuthData createAuth(LoginRequest loginRequest, UserData existingUser) throws Exception {
        if (authenticateUser(loginRequest, existingUser)) {
            AuthData authData = new AuthData(BaseDAO.generateId(), loginRequest.username());
            var statement = "INSERT INTO auth (authToken, username) VALUES (?, ?)";
            executeUpdate(statement, authData.authToken(), authData.username());
            return authData;
        }

        throw new UnauthorizedException("Error: unauthorized");
    }

    public boolean sessionExistsForAuthToken(String authToken) throws Exception {
        return getAuth(authToken) != null;
    }

    public String getUsername(String authToken) throws Exception {
        String username = getAuth(authToken).username();
        if (username == null) {
            throw new UnauthorizedException("Error: unauthorized");
        }
        return username;
    }

    public AuthData getAuth(String authToken) throws Exception {
        try (Connection conn = DatabaseManager.getConnection()) {
            var statement = "SELECT authToken, username FROM auth WHERE authToken=?";
            try (PreparedStatement ps = conn.prepareStatement(statement)) {
                ps.setString(1, authToken);
                try (ResultSet rs = ps.executeQuery()) {
                    if (rs.next()) {
                        return readAuthData(rs);
                    }
                }
            }
        } catch (Exception e) {
            throw new DataAccessException(String.format("Error: Unable to read data: %s", e.getMessage()));
        }

        return null;
    }

    public void logout(String authToken) throws Exception {
        if (getAuth(authToken) != null) {
            var statement = "DELETE FROM auth WHERE authToken=?";
            executeUpdate(statement, authToken);
            return;
        }
        throw new UnauthorizedException("Error: unauthorized");
    }

    public void clearAuth() throws Exception {
        var statement = "TRUNCATE auth";
        executeUpdate(statement);
    }

    private AuthData readAuthData(ResultSet rs) throws SQLException {
        var authToken = rs.getString("authToken");
        var username = rs.getString("username");
        return new AuthData(authToken, username);
    }

}
