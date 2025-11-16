package client;

import chess.ChessGame;
import exception.ResponseException;
import model.*;
import org.junit.jupiter.api.*;
import server.Server;
import server.ServerFacade;

import java.rmi.UnexpectedException;

import static org.junit.jupiter.api.Assertions.*;


public class ServerFacadeTests {

    private static Server server;
    static ServerFacade facade;

    @BeforeAll
    public static void init() {
        server = new Server();
        var port = server.run(0);
        facade = new ServerFacade("http://localhost:" + port);
        System.out.println("Started test HTTP server on " + port);
    }

    @AfterAll
    static void stopServer() {
        server.stop();
    }

    @Test
    public void sampleTest() {
        Assertions.assertTrue(true);
    }

    @Test
    void registerSuccess() throws Exception {
        var authData = facade.register(new RegisterRequest("player1", "password", "email@player.com"));
        assertTrue(authData.authToken().length() > 10);
    }

    @Test
    void registerFailure() throws Exception {
        facade.register(new RegisterRequest("player1", "password", "email@player.com"));
        assertThrows(Exception.class, () -> facade.register(new RegisterRequest("player1", "password", "email@player.com")));
    }

    @Test
    void loginSuccess() throws Exception {
        var authTokenRegister = facade.register(new RegisterRequest("a", "a", "a"));
        facade.logout(authTokenRegister.authToken());
        var authDataLogin = facade.login(new LoginRequest("a", "a"));
        assertTrue(authDataLogin.authToken().length() > 10);
    }

    @Test
    void loginFailure() throws Exception {
        var authTokenRegister = facade.register(new RegisterRequest("a", "a", "a"));
        facade.logout(authTokenRegister.authToken());
        assertThrows(ResponseException.class, () -> facade.login(new LoginRequest("a", "b")));
    }

    @Test
    void logoutSuccess() throws Exception {
        var authTokenRegister = facade.register(new RegisterRequest("b", "b", "b"));
        assertDoesNotThrow(() -> facade.logout(authTokenRegister.authToken()));
    }

    @Test
    void logoutFailure() throws Exception {
        assertThrows(ResponseException.class, () -> facade.logout(""));
    }

    @Test
    void createGameSuccess() throws Exception {
        var authTokenRegister = facade.register(new RegisterRequest("c", "c", "c"));
        var createGameRequest = new CreateGameRequest("game1a");
        assertDoesNotThrow(() -> facade.createGame(createGameRequest, authTokenRegister.authToken()));
    }

    @Test
    void createGameFailure() throws Exception {
        var createGameRequest = new CreateGameRequest("game1a");
        assertThrows(ResponseException.class, () -> facade.createGame(createGameRequest, ""));
    }

    @Test
    void getGameByIdSuccess() throws Exception {
        var authTokenRegister = facade.register(new RegisterRequest("d", "d", "d"));
        var createGameRequest = new CreateGameRequest("game1a");
        facade.createGame(createGameRequest, authTokenRegister.authToken());
        assertEquals(1, facade.getGameById(authTokenRegister.authToken(), 1).gameID());
    }

    @Test
    void getGameByIdFailure() throws Exception {
        var authTokenRegister = facade.register(new RegisterRequest("d", "d", "d"));
        var createGameRequest = new CreateGameRequest("game1a");
        facade.createGame(createGameRequest, authTokenRegister.authToken());
        assertThrows(ResponseException.class, () -> facade.getGameById("", 2));
    }

    @Test
    void listGamesSuccess() throws Exception {
        var authTokenRegister = facade.register(new RegisterRequest("e", "e", "e"));
        facade.createGame(new CreateGameRequest("game1b"), authTokenRegister.authToken());
        facade.createGame(new CreateGameRequest("game2c"), authTokenRegister.authToken());
        facade.createGame(new CreateGameRequest("game3d"), authTokenRegister.authToken());
        facade.createGame(new CreateGameRequest("game4e"), authTokenRegister.authToken());
        GamesWrapper gameList = facade.listGames(authTokenRegister.authToken());
        assertEquals(4, gameList.games().toArray().length);
    }

    @Test
    void listGamesFailure() throws Exception {
        var authTokenRegister = facade.register(new RegisterRequest("e", "e", "e"));
        facade.createGame(new CreateGameRequest("game1b"), authTokenRegister.authToken());
        facade.createGame(new CreateGameRequest("game2c"), authTokenRegister.authToken());
        facade.createGame(new CreateGameRequest("game3d"), authTokenRegister.authToken());
        facade.createGame(new CreateGameRequest("game4e"), authTokenRegister.authToken());
        assertThrows(ResponseException.class, () -> facade.listGames(""));
    }

    @Test
    void joinGameSuccess() throws Exception {
        var authTokenRegister = facade.register(new RegisterRequest("f", "f", "f"));
        facade.createGame(new CreateGameRequest("game5f"), authTokenRegister.authToken());
        facade.joinGame(new JoinGameRequest(ChessGame.TeamColor.WHITE, 1, "f"), authTokenRegister.authToken());
        GameData game = facade.getGameById(authTokenRegister.authToken(), 1);
        assertEquals("f", game.whiteUsername());
    }

    @Test
    void joinGameFailure() throws Exception {
        var authTokenRegister = facade.register(new RegisterRequest("f", "f", "f"));
        facade.createGame(new CreateGameRequest("game5f"), authTokenRegister.authToken());
        JoinGameRequest request = new JoinGameRequest(ChessGame.TeamColor.WHITE, 2, "f")
        assertThrows(ResponseException.class, () -> facade.joinGame(request, authTokenRegister.authToken()));
    }

    @Test
    void clearApplicationSuccess() throws Exception {
        var authData = facade.register(new RegisterRequest("h", "h", "h"));
        assertDoesNotThrow(() -> facade.clearApplication(authData.authToken()));
    }

    @BeforeEach
    void clearApplication() throws Exception {
        var authData = facade.register(new RegisterRequest("clear", "clear", "clear"));
        assertDoesNotThrow(() -> facade.clearApplication(authData.authToken()));
    }
}
