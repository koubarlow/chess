package dataaccess;

import model.*;

public interface GameDAO extends BaseDAO {

    GameList listGames() throws Exception;
    GameData createGame(CreateGameRequest createGameRequest) throws Exception;
    void joinGame(JoinGameRequest joinGameRequest, String username) throws Exception;
    void clearGames() throws Exception;
}
