package dataaccess;

import chess.ChessGame;
import model.CreateGameRequest;
import model.GameData;
import model.GameList;
import model.JoinGameRequest;

import java.util.HashMap;

public class MemoryGameDAO implements GameDAO {

    final private HashMap<Integer, GameData> games = new HashMap<>();
    int nextId = 0;

    public MemoryGameDAO() {}

    public GameData createGame(CreateGameRequest createGameRequest) throws Exception {
        GameData game = new GameData(nextId++, "", "", createGameRequest.gameName(), new ChessGame());
        games.put(game.gameID(), game);
        return game;
    }

    public GameList listGames() throws Exception {
        return new GameList(games.values());
    }

    public void joinGame(JoinGameRequest joinGameRequest) throws Exception {

        int gameId = joinGameRequest.gameID();
        ChessGame.TeamColor teamColor = joinGameRequest.playerColor();
        GameData game = games.get(gameId);

        String gameName = game.gameName();
        String whiteUsername = game.whiteUsername();
        String blackUsername = game.blackUsername();
        ChessGame chessGame = game.game();

        if (teamColor == ChessGame.TeamColor.WHITE && whiteUsername != null) {
            whiteUsername = joinGameRequest.username();
        } else if (teamColor == ChessGame.TeamColor.BLACK && blackUsername != null) {
            blackUsername = joinGameRequest.username();
        } else {
            throw new DataAccessException("Already taken");
        }

        GameData updatedGame = new GameData(joinGameRequest.gameID(), whiteUsername, blackUsername, gameName, chessGame);
        games.put(gameId, updatedGame);
    }

    public void clearGames() {
        this.games.clear();
    }
}
