package dataaccess;

import chess.ChessGame;
import dataaccess.auth.MySqlAuthDAO;
import dataaccess.exceptions.AlreadyTakenException;
import dataaccess.exceptions.BadRequestException;
import dataaccess.game.MySqlGameDAO;
import dataaccess.user.MySqlUserDAO;
import model.*;
import org.junit.jupiter.api.*;

public class SqlGameServiceTests {

    private static SqlServerTests sqlServerTests;
    private static String xAuthToken = "";
    private static String yAuthToken = "";

    @BeforeAll
    public static void init() {

        try {
            sqlServerTests = new SqlServerTests(new MySqlUserDAO(), new MySqlAuthDAO(), new MySqlGameDAO());
            sqlServerTests.testAuthService.clearAuth();
            sqlServerTests.testGameService.clearGames();
            sqlServerTests.testUserService.clearUsers();

            AuthData x = sqlServerTests.testUserService.register(new RegisterRequest("x", "x!", "x@x"));
            xAuthToken = x.authToken();
            AuthData y = sqlServerTests.testUserService.register(new RegisterRequest("y", "y!", "y@y"));
            yAuthToken = y.authToken();

            sqlServerTests.testGameService.createGame(xAuthToken, new CreateGameRequest("testGameId1"));
            sqlServerTests.testGameService.createGame(yAuthToken, new CreateGameRequest("testGameId2"));

        } catch (Exception e) {
            throw new RuntimeException(e.getMessage());
        }
    }

    @Test
    @Order(1)
    @DisplayName("Create Game Success")
    public void sqlCreateGameSuccess() {
        CreateGameRequest createGameRequest = new CreateGameRequest("sqlTestYay");
        Assertions.assertDoesNotThrow(() -> sqlServerTests.testGameService.createGame(xAuthToken, createGameRequest),
                "encountered an error while create game");
    }

    @Test
    @Order(2)
    @DisplayName("Create Game Failure (No name)")
    public void sqlCreateGameFailure() {
        CreateGameRequest createGameRequest = new CreateGameRequest(null);
        Assertions.assertThrows(BadRequestException.class, () -> sqlServerTests.testGameService.createGame(xAuthToken, createGameRequest),
                "did not encounter an error while create game");
    }

    @Test
    @Order(3)
    @DisplayName("List Game Success")
    public void sqlListCreatedGameSuccess() throws Exception {
        CreateGameRequest createGameRequest = new CreateGameRequest("listGamesSuccessGameNameTest");
        GameData newGame = sqlServerTests.testGameService.createGame(yAuthToken, createGameRequest);
        GameList games = sqlServerTests.testGameService.listGames(yAuthToken);
        Assertions.assertTrue(games.contains(newGame));
    }

    @Test
    @Order(4)
    @DisplayName("List Game Not Found")
    public void sqlListGameFailure() throws Exception {
        GameData gameThatDoesntExist = new GameData(298, null, null, "playyy", new ChessGame());
        GameList games = sqlServerTests.testGameService.listGames(yAuthToken);
        Assertions.assertFalse(games.contains(gameThatDoesntExist));
    }

    @Test
    @Order(5)
    @DisplayName("Join Game Success")
    public void sqlJoinGameSuccess() throws Exception {
        sqlServerTests.testGameService.createGame(xAuthToken, new CreateGameRequest("testGameId1"));
        sqlServerTests.testGameService.createGame(yAuthToken, new CreateGameRequest("testGameId2"));
        JoinGameRequest joinGameRequest = new JoinGameRequest(ChessGame.TeamColor.WHITE, 2, "y");
        sqlServerTests.testGameService.joinGame(yAuthToken, joinGameRequest);
        GameList games = sqlServerTests.testGameService.listGames(yAuthToken);
        GameData expected = new GameData(2, "y", null, "testGameId2", new ChessGame());
        Assertions.assertEquals(expected, games.get(1));
    }

    @Test
    @Order(6)
    @DisplayName("Join Game Failure")
    public void sqlJoinGameAlreadyTaken() throws Exception {
        JoinGameRequest joinGameRequest = new JoinGameRequest(ChessGame.TeamColor.WHITE, 1, "x");
        sqlServerTests.testGameService.joinGame(xAuthToken, joinGameRequest);
        JoinGameRequest joinGameRequest2 = new JoinGameRequest(ChessGame.TeamColor.WHITE, 1, "z");
        Assertions.assertThrows(AlreadyTakenException.class, () -> sqlServerTests.testGameService.joinGame(xAuthToken, joinGameRequest2));
    }

    @Test
    @Order(7)
    @DisplayName("Clear games")
    public void sqlClearGamesSuccess() {
        try {
            sqlServerTests.testGameService.clearGames();
            Assertions.assertEquals(new GameList(), sqlServerTests.testGameService.listGames(xAuthToken));
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }
}
