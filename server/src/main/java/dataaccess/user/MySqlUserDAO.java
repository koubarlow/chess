package dataaccess.user;

import dataaccess.DatabaseManager;
import dataaccess.exceptions.DataAccessException;
import model.UserData;
import org.mindrot.jbcrypt.BCrypt;

import java.sql.*;

public class MySqlUserDAO implements UserDAO {

    public MySqlUserDAO() throws DataAccessException {
        configureDatabase();
    }

    public UserData getUser(String username) throws Exception {
        try (Connection conn = DatabaseManager.getConnection()) {
            var statement = "SELECT username, password, email FROM user WHERE username=?";
            try (PreparedStatement ps = conn.prepareStatement(statement)) {
                ps.setString(1, username);
                try (ResultSet rs = ps.executeQuery()) {
                    if (rs.next()) {
                        return readUserData(rs);
                    }
                }
            }
        } catch (Exception e) {
            throw new DataAccessException(String.format("Unable to read data: %s", e.getMessage()));
        }

        return null;
    }

    public UserData createUser(UserData userData) throws Exception {
        var statement = "INSERT INTO user (username, password, email) VALUES (?, ?, ?)";
        String hashedPassword = BCrypt.hashpw(userData.password(), BCrypt.gensalt());
        String username = executeUpdate(statement, userData.username(), hashedPassword, userData.email());
        return new UserData(username, userData.password(), userData.email());
    }

    public void clearUsers() throws Exception {
        var statement = "TRUNCATE user";
        executeUpdate(statement);
    }

    private UserData readUserData(ResultSet rs) throws SQLException {
        var username = rs.getString("username");
        var password = rs.getString("password");
        var email = rs.getString("email");
        return new UserData(username, password, email);
    }

    private String executeUpdate(String statement, Object... params) throws DataAccessException {
        try (Connection conn = DatabaseManager.getConnection()) {
            try (PreparedStatement ps = conn.prepareStatement(statement, Statement.RETURN_GENERATED_KEYS)) {
                for (int i = 0; i < params.length; i++) {
                    Object param = params[i];
                    if (param instanceof String p) ps.setString(i+1, p);
                }
                ps.executeUpdate();

                ResultSet rs = ps.getGeneratedKeys();
                if (rs.next()) {
                    return rs.getString(1);
                }

                return "";
            }
        } catch (SQLException e) {
            throw new DataAccessException(String.format("unable to update database: %s, %s", statement, e.getMessage()));
        }
    }

    private final String[] createStatements = {
            """
            CREATE TABLE IF NOT EXISTS  user (
              `username` varchar(256) NOT NULL,
              `password` varchar(256) NOT NULL,
              `email` varchar(256) NOT NULL,
              PRIMARY KEY (`username`),
              INDEX(email)
            ) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci
            """
    };

    private void configureDatabase() throws DataAccessException {
        DatabaseManager.createDatabase();
        try (Connection conn = DatabaseManager.getConnection()) {
            for (String statement: createStatements) {
                try (var preparedStatement = conn.prepareStatement(statement)) {
                    preparedStatement.executeUpdate();
                }
            }
        } catch (SQLException ex) {
            throw new DataAccessException(String.format("Unable to configure database: %s", ex.getMessage()));
        }
    }
}
