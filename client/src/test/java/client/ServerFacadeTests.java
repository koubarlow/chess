package client;

import model.CreateGameRequest;
import model.GamesWrapper;
import model.LoginRequest;
import model.RegisterRequest;
import org.junit.jupiter.api.*;
import server.Server;
import server.ServerFacade;

import static org.junit.jupiter.api.Assertions.*;


public class ServerFacadeTests {

    private static Server server;
    static ServerFacade facade;

    @BeforeAll
    public static void init() {
        server = new Server();
        var port = server.run(0);
        facade = new ServerFacade("http://localhost:8080");
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
    void loginSuccess() throws Exception {
        var authTokenRegister = facade.register(new RegisterRequest("a", "a", "a"));
        facade.logout(authTokenRegister.authToken());
        var authDataLogin = facade.login(new LoginRequest("a", "a"));
        assertTrue(authDataLogin.authToken().length() > 10);
    }

    @Test
    void logoutSuccess() throws Exception {
        var authTokenRegister = facade.register(new RegisterRequest("b", "b", "b"));
        assertDoesNotThrow(() -> facade.logout(authTokenRegister.authToken()));
    }

    @Test
    void createGameSuccess() throws Exception {
        var authTokenRegister = facade.register(new RegisterRequest("c", "c", "c"));
        var createGameRequest = new CreateGameRequest("game1a");
        assertDoesNotThrow(() -> facade.createGame(createGameRequest, authTokenRegister.authToken()));
    }

    @Test
    void getGameByIdSuccess() throws Exception {
        var authTokenRegister = facade.register(new RegisterRequest("d", "d", "d"));
        var createGameRequest = new CreateGameRequest("game1a");
        facade.createGame(createGameRequest, authTokenRegister.authToken());
        assertEquals(1, facade.getGameById(authTokenRegister.authToken(), 1).gameID());
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
    void joinGameSuccess() throws Exception {
        var authTokenRegister = facade.register(new RegisterRequest("f", "f", "f"));
    }

    @BeforeEach
    void clearApplication() throws Exception {
        var authData = facade.register(new RegisterRequest("clear", "clear", "clear"));
        assertDoesNotThrow(() -> facade.clearApplication(authData.authToken()));
    }
}
