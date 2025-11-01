package service;

import dataaccess.auth.AuthDAO;
import dataaccess.exceptions.BadRequestException;
import dataaccess.exceptions.UnauthorizedException;
import dataaccess.game.GameDAO;
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

        if (createGameRequest.gameName() == null) { throw new BadRequestException("Error: bad request"); }
        if (authDAO.sessionExistsForAuthToken(authToken)) {
            return this.gameDAO.createGame(createGameRequest);
        }
        throw new UnauthorizedException("Error: unauthorized");
    }

    public GameList listGames(String authToken) throws Exception {
        if (authDAO.sessionExistsForAuthToken(authToken)) {
            return this.gameDAO.listGames();
        }
        throw new UnauthorizedException("Error: unauthorized");
    }

    public void joinGame(String authToken, JoinGameRequest joinGameRequest) throws Exception {
        if (authDAO.sessionExistsForAuthToken(authToken)) {
            String username = this.authDAO.getUsername(authToken);
            this.gameDAO.joinGame(joinGameRequest, username);
        } else {
            throw new UnauthorizedException("Error: unauthorized");
        }
    }

    public void clearGames() throws Exception {
        this.gameDAO.clearGames();
    }
}
