package dataaccess.user;

import dataaccess.DatabaseManager;
import dataaccess.exceptions.DataAccessException;
import model.UserData;

import java.sql.Connection;
import java.sql.SQLException;

public class MySqlUserDAO implements UserDAO {

    public MySqlUserDAO() throws DataAccessException {
        configureDatabase();
    }

    public UserData getUser(String username) throws Exception {
        return new UserData("", "", "");
    }

    public UserData createUser(UserData userData) throws Exception {
        return new UserData("", "", "");
    }

    public void clearUsers() throws Exception {

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
