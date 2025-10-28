package dataaccess.user;

import dataaccess.DatabaseManager;
import dataaccess.exceptions.DataAccessException;
import model.UserData;

import java.sql.*;

public class MySqlUserDAO implements UserDAO {

    public MySqlUserDAO() throws DataAccessException {
        configureDatabase();
    }

    public UserData getUser(String username) throws Exception {
        return new UserData("", "", "");
    }

    public UserData createUser(UserData userData) throws Exception {
        var statement = "INSERT INTO user (username, password, email) VALUES (?, ?, ?)";

        return new UserData("", "", "");
    }

    public void clearUsers() throws Exception {
        var statement = "TRUNCATE user";
        executeUpdate(statement);
    }

    private int executeUpdate(String statement, Object... params) throws DataAccessException {
        try (Connection conn = DatabaseManager.getConnection()) {
            try (PreparedStatement ps = conn.prepareStatement(statement, Statement.RETURN_GENERATED_KEYS)) {
                for (int i = 0; i < params.length; i++) {
                    Object param = params[i];
                    if (param instanceof String p) ps.setString(i+1, p);
                    else if (param instanceof Integer p) ps.setInt(i+1, p);
                }
                ps.executeUpdate();

                ResultSet rs = ps.getGeneratedKeys();
                if (rs.next()) {
                    return rs.getInt(1);
                }

                return 0;
            }
        } catch (SQLException e) {
            throw new DataAccessException(String.format("unable to update database: %s, %s", statement, e.getMessage()));
        }
    }

    private final String[] createStatements = {
            """
            CREATE TABLE IF NOT EXISTS user (
                'id' int NOT NULL AUTO_INCREMENT,
                'username' varchar(256) NOT NULL,
                'password' varchar(256) NOT NULL,
                'email' varchar(256) NOT NULL,
                PRIMARY KEY ('id'),
                INDEX(username),
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
