package service;

import dataaccess.BadRequestException;
import dataaccess.MemoryAuthDAO;
import dataaccess.MemoryUserDAO;
import dataaccess.UnauthorizedException;
import model.*;
import org.junit.jupiter.api.*;

import java.util.HashMap;

public class AuthServiceTests {

    private static FakeServer fakeServer;

    @BeforeAll
    public static void init() {

        HashMap<String, UserData> users = new HashMap<>();
        users.put("bill", new UserData("bill", "pass", "bill@test.com"));
        fakeServer = new FakeServer(new MemoryUserDAO(users));
    }

    @Test
    @Order(1)
    @DisplayName("Login Success")
    public void loginSuccess() throws Exception {
        LoginRequest fakeLoginRequest = new LoginRequest("bill", "pass");
        AuthData authenticatedUser = fakeServer.fakeAuthService.login(fakeLoginRequest);
        Assertions.assertEquals("bill", fakeServer.fakeUserService.getUser(authenticatedUser.username()).username(),
                "username did not match logged-in user");
    }

    @Test
    @Order(2)
    @DisplayName("Login wrong password")
    public void loginFailure() {
        LoginRequest fakeLoginRequest = new LoginRequest("bill", "pass!!!");
        Assertions.assertThrows(UnauthorizedException.class, () -> fakeServer.fakeAuthService.login(fakeLoginRequest));
    }

    @Test
    @Order(3)
    @DisplayName("Logout success")
    public void logoutSuccess() throws Exception {
        LoginRequest fakeLoginRequest = new LoginRequest("bill", "pass");
        AuthData authenticatedUser = fakeServer.fakeAuthService.login(fakeLoginRequest);
        LogoutRequest logoutRequest = new LogoutRequest(authenticatedUser.authToken());
        Assertions.assertDoesNotThrow(() -> fakeServer.fakeAuthService.logout(logoutRequest));
    }

    @Test
    @Order(2)
    @DisplayName("Logout unauthorized")
    public void logoutFailure() {
        LogoutRequest logoutRequest = new LogoutRequest("12345");
        Assertions.assertThrows(UnauthorizedException.class, () -> fakeServer.fakeAuthService.logout(logoutRequest));
    }
}
