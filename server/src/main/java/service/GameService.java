package service;

import dataaccess.AuthDAO;
import dataaccess.DataAccessException;
import dataaccess.GameDAO;
import model.CreateGameRequest;
import model.GameData;
import model.GameList;
import model.JoinGameRequest;

public class GameService {

    private final GameDAO gameDAO;
    private final AuthDAO authDAO;

    public GameService(GameDAO gameDAO, AuthDAO authDAO) {
        this.authDAO = authDAO;
        this.gameDAO = gameDAO;
    }

    public GameData createGame(String authToken, CreateGameRequest createGameRequest) throws Exception {
        if (authDAO.sessionExistsForAuthToken(authToken)) {
            return this.gameDAO.createGame(createGameRequest);
        }
        return null;
    }

    public GameList listGames(String authToken) throws Exception {
        if (authDAO.sessionExistsForAuthToken(authToken)) {
            return this.gameDAO.listGames();
        }
        return null;
    }

    public void joinGame(String authToken, JoinGameRequest joinGameRequest) throws Exception {
        if (authDAO.sessionExistsForAuthToken(authToken)) {
            this.gameDAO.joinGame(joinGameRequest);
        } else {
            throw new DataAccessException("Unauthorized");
        }
    }

}
