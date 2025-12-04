package service;

import chess.ChessGame;
import dataaccess.auth.MemoryAuthDAO;
import dataaccess.exceptions.AlreadyTakenException;
import dataaccess.exceptions.BadRequestException;
import dataaccess.game.MemoryGameDAO;
import dataaccess.user.MemoryUserDAO;
import model.*;
import org.junit.jupiter.api.*;

import java.util.HashMap;

public class GameServiceTests {

    private static TestMemoryServer testMemoryServer;

    @BeforeAll
    public static void init() {

        HashMap<String, UserData> users = new HashMap<>();
        users.put("john", new UserData("john", "pass!", "john@test.com"));

        HashMap<String, String> sessions = new HashMap<>();
        sessions.put("123456789", "john");
        sessions.put("123459876", "kip");

        HashMap<Integer, GameData> games = new HashMap<>();
        GameData fakeGameData = new GameData(299, null, null, "playWithMe", new ChessGame());
        games.put(299, fakeGameData);

        GameData fakeGameData2 = new GameData(300, null, null, "playWithMee", new ChessGame());
        games.put(300, fakeGameData2);

        testMemoryServer = new TestMemoryServer(new MemoryUserDAO(users), new MemoryAuthDAO(sessions), new MemoryGameDAO(games));
    }

    // create game
    @Test
    @Order(1)
    @DisplayName("Create Game Success")
    public void createGameSuccess() {
        CreateGameRequest createGameRequest = new CreateGameRequest("yay");
        Assertions.assertDoesNotThrow(() -> testMemoryServer.fakeGameService.createGame("123456789", createGameRequest),
                "encountered an error while create game");
    }

    @Test
    @Order(2)
    @DisplayName("Create Game Failure")
    public void createGameFailure() {
        CreateGameRequest createGameRequest = new CreateGameRequest(null);
        Assertions.assertThrows(BadRequestException.class, () -> testMemoryServer.fakeGameService.createGame("123456789", createGameRequest),
                "did not encounter an error while create game");
    }

    // list game
    @Test
    @Order(3)
    @DisplayName("List Game Success")
    public void listCreatedGameSuccess() throws Exception {
        CreateGameRequest createGameRequest = new CreateGameRequest("yay2");
        GameData newGame = testMemoryServer.fakeGameService.createGame("123456789", createGameRequest);
        GameList games = testMemoryServer.fakeGameService.listGames("123456789");
        Assertions.assertTrue(games.contains(newGame));
    }

    @Test
    @Order(4)
    @DisplayName("List Game Not Found")
    public void listGameFailure() throws Exception {
        GameData gameThatDoesntExist = new GameData(298, null, null, "playyy", new ChessGame());
        GameList games = testMemoryServer.fakeGameService.listGames("123456789");
        Assertions.assertFalse(games.contains(gameThatDoesntExist));
    }

    // join game
    @Test
    @Order(5)
    @DisplayName("Join Game Success")
    public void joinGameSuccess() throws Exception {
        UpdateGameRequest updateGameRequest = new UpdateGameRequest(ChessGame.TeamColor.WHITE, 300, "john", null);
        testMemoryServer.fakeGameService.updateGame("123456789", updateGameRequest);
        GameList games = testMemoryServer.fakeGameService.listGames("123456789");
        GameData expected = new GameData(300, "john", null, "playWithMee", new ChessGame());
        Assertions.assertEquals(expected, games.getLast());
    }

    @Test
    @Order(6)
    @DisplayName("Join Game Failure")
    public void joinGameAlreadyTaken() throws Exception {
        UpdateGameRequest updateGameRequest = new UpdateGameRequest(ChessGame.TeamColor.WHITE, 299, "john", null);
        testMemoryServer.fakeGameService.updateGame("123456789", updateGameRequest);
        UpdateGameRequest updateGameRequest2 = new UpdateGameRequest(ChessGame.TeamColor.WHITE, 299, "kip", null);
        Assertions.assertThrows(AlreadyTakenException.class, () -> testMemoryServer.fakeGameService.updateGame("123459876", updateGameRequest2));
    }
}
