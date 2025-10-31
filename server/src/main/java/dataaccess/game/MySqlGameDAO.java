package dataaccess.game;

import chess.ChessGame;
import com.google.gson.Gson;
import dataaccess.DatabaseManager;
import dataaccess.exceptions.AlreadyTakenException;
import dataaccess.exceptions.BadRequestException;
import dataaccess.exceptions.DataAccessException;
import model.CreateGameRequest;
import model.GameData;
import model.GameList;
import model.JoinGameRequest;

import java.sql.*;
import java.util.ArrayList;
import java.util.Collection;

import static java.sql.Types.NULL;

public class MySqlGameDAO implements GameDAO {

    public MySqlGameDAO() throws DataAccessException {
        configureDatabase();
    }

    public GameData createGame(CreateGameRequest createGameRequest) throws Exception {
        var statement = "INSERT INTO game (whiteUsername, blackUsername, gameName, json) VALUES (?,?,?,?)";
        String jsonChessGame = new Gson().toJson(new ChessGame());
        int id = executeUpdate(statement, null, null, createGameRequest.gameName(), jsonChessGame);
        return new GameData(id, null, null, createGameRequest.gameName(), new ChessGame());
    }

    public GameData getGame(int id) throws Exception {
        try (Connection conn = DatabaseManager.getConnection()) {
            var statement = "SELECT id, whiteUsername, blackUsername, gameName, json FROM game where id=?";
            try (PreparedStatement ps = conn.prepareStatement(statement)) {
                ps.setInt(1, id);
                try (ResultSet rs = ps.executeQuery()) {
                    if (rs.next()) {
                        return readGame(rs);
                    }
                }
            }
        } catch (Exception e) {
            throw new DataAccessException(String.format("Unable to read data: %s", e.getMessage()));
        }
        return null;
    }

    public GameList listGames() throws Exception {
        Collection<GameData> result = new ArrayList<>();
        try (Connection conn = DatabaseManager.getConnection()) {
            var statement = "SELECT id, whiteUsername, blackUsername, gameName, json FROM game";
            try (PreparedStatement ps = conn.prepareStatement(statement)) {
                try (ResultSet rs = ps.executeQuery()) {
                    while (rs.next()) {
                        result.add(readGame(rs));
                    }
                }
            }
        } catch (Exception e) {
            throw new DataAccessException(String.format("Unable to read data: %s", e.getMessage()));
        }
        return new GameList(result);
    }

    public void joinGame(JoinGameRequest joinGameRequest, String username) throws Exception {
        if (joinGameRequest.gameID() == null) { throw new BadRequestException("Error: bad request"); }
        int gameId = joinGameRequest.gameID();
        ChessGame.TeamColor teamColor = joinGameRequest.playerColor();
        if (teamColor == null) { throw new BadRequestException("Error: bad request"); }
        GameData game = getGame(gameId);
        if (game == null) { throw new DataAccessException("Error: game does not exist"); }

        String gameName = game.gameName();
        String whiteUsername = game.whiteUsername();
        String blackUsername = game.blackUsername();
        ChessGame chessGame = game.game();

        if (teamColor == ChessGame.TeamColor.WHITE) {
            if (whiteUsername == null) {
                whiteUsername = username;
            } else {
                throw new AlreadyTakenException("Error: already taken");
            }
        } else {
            if (blackUsername == null) {
                blackUsername = username;
            } else {
                throw new AlreadyTakenException("Error: already taken");
            }
        }

        String jsonChessGame = new Gson().toJson(chessGame);

        var statement = "UPDATE game SET whiteUsername=?, blackUsername=?, gameName=?, json=?";
        executeUpdate(statement, whiteUsername, blackUsername, gameName, jsonChessGame);
    }

    public void clearGames() throws Exception {
        var statement = "TRUNCATE game";
        executeUpdate(statement);
    }

    private GameData readGame(ResultSet rs) throws Exception {
        var id = rs.getInt("id");
        var whiteUsername = rs.getString("whiteUsername");
        var blackUsername = rs.getString("blackUsername");
        var gameName = rs.getString("gameName");
        var json = rs.getString("json");
        ChessGame chessGame = new Gson().fromJson(json, ChessGame.class);
        return new GameData(id, whiteUsername, blackUsername, gameName, chessGame);
    }

    private int executeUpdate(String statement, Object... params) throws DataAccessException {
        try (Connection conn = DatabaseManager.getConnection()) {
            try (PreparedStatement ps = conn.prepareStatement(statement, Statement.RETURN_GENERATED_KEYS)) {
                for (int i = 0; i < params.length; i++) {
                    Object param = params[i];
                    if (param instanceof String p) ps.setString(i + 1, p);
                    else if (param instanceof Integer p) ps.setInt(i + 1, p);
                    else if (param == null) ps.setNull(i + 1, NULL);
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
            CREATE TABLE IF NOT EXISTS  game (
                `id` int NOT NULL AUTO_INCREMENT,
                `whiteUsername` varchar(256) DEFAULT NULL,
                `blackUsername` varchar(256) DEFAULT NULL,
                `gameName` varchar(256) NOT NULL,
                `json` TEXT NOT NULL,
                PRIMARY KEY (`id`),
                INDEX(gameName),
                INDEX(whiteUsername),
                INDEX(blackUsername)
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
