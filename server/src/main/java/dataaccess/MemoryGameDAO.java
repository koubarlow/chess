package dataaccess;

import model.CreateGameRequest;
import model.GameData;
import model.GameList;
import model.JoinGameRequest;

import java.util.HashMap;

public class MemoryGameDAO implements GameDAO {

    final private HashMap<String, GameData> games = new HashMap<>();

    public MemoryGameDAO() {}

    public GameData createGame(CreateGameRequest createGameRequest) throws DataAccessException {
        return null;
    }

    public GameList listGames() throws DataAccessException {
        return null;
    }

    public void joinGame(JoinGameRequest joinGameRequest) throws DataAccessException {

    }
}
