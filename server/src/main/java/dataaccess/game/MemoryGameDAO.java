package dataaccess.game;

import chess.ChessGame;
import dataaccess.exceptions.AlreadyTakenException;
import dataaccess.exceptions.BadRequestException;
import dataaccess.exceptions.DataAccessException;
import model.CreateGameRequest;
import model.GameData;
import model.GameList;
import model.JoinGameRequest;

import java.util.HashMap;

public class MemoryGameDAO implements GameDAO {

    private HashMap<Integer, GameData> games = new HashMap<>();
    int nextGameId = 1;

    public MemoryGameDAO() {}

    public MemoryGameDAO(HashMap<Integer, GameData> games) {
        this.games = games;
    }

    public GameData createGame(CreateGameRequest createGameRequest) throws Exception {
        GameData game = new GameData(nextGameId++, null, null, createGameRequest.gameName(), new ChessGame());
        games.put(game.gameID(), game);
        return game;
    }

    public GameList listGames() throws Exception {
        return new GameList(games.values());
    }

    public void joinGame(JoinGameRequest joinGameRequest, String username) throws Exception {

        if (joinGameRequest.gameID() == null) { throw new BadRequestException("Error: bad request"); }
        int gameId = joinGameRequest.gameID();
        ChessGame.TeamColor teamColor = joinGameRequest.playerColor();
        if (teamColor == null) { throw new BadRequestException("Error: bad request"); }
        GameData game = games.get(gameId);
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

        GameData updatedGame = new GameData(gameId, whiteUsername, blackUsername, gameName, chessGame);
        games.put(gameId, updatedGame);
    }

    public void clearGames() {
        this.games.clear();
    }
}
