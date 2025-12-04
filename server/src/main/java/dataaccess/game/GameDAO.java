package dataaccess.game;

import chess.ChessGame;
import dataaccess.BaseDAO;
import dataaccess.exceptions.AlreadyTakenException;
import dataaccess.exceptions.DataAccessException;
import model.*;

public interface GameDAO extends BaseDAO {

    GameList listGames() throws Exception;
    GameData createGame(CreateGameRequest createGameRequest) throws Exception;
    void updateGame(UpdateGameRequest updateGameRequest, String username) throws Exception;
    void clearGames() throws Exception;

    default GameData updateGame(GameData game, int gameId, ChessGame.TeamColor teamColor, String username, boolean makeMove) throws Exception {
        if (game == null) { throw new DataAccessException("Error: game does not exist"); }

        String gameName = game.gameName();
        String whiteUsername = game.whiteUsername();
        String blackUsername = game.blackUsername();
        ChessGame chessGame = game.game();

        if (!makeMove) {
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
        }

        return new GameData(gameId, whiteUsername, blackUsername, gameName, chessGame);
    }
}
