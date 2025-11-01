package service;

import dataaccess.user.MemoryUserDAO;
import dataaccess.exceptions.UnauthorizedException;
import model.*;
import org.junit.jupiter.api.*;

import java.util.HashMap;

public class AuthServiceTests {

    private static TestMemoryServer testMemoryServer;

    @BeforeAll
    public static void init() {

        HashMap<String, UserData> users = new HashMap<>();
        testMemoryServer = new TestMemoryServer(new MemoryUserDAO(users));
        try {
            testMemoryServer.fakeUserService.register(new RegisterRequest("bill", "pass", "bill@test.com"));
            testMemoryServer.fakeAuthService.clearAuth();
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    @Test
    @Order(1)
    @DisplayName("Login Success")
    public void loginSuccess() throws Exception {
        LoginRequest fakeLoginRequest = new LoginRequest("bill", "pass");
        AuthData authenticatedUser = testMemoryServer.fakeAuthService.login(fakeLoginRequest);
        Assertions.assertEquals("bill", testMemoryServer.fakeUserService.getUser(authenticatedUser.username()).username(),
                "username did not match logged-in user");
    }

    @Test
    @Order(2)
    @DisplayName("Login wrong password")
    public void loginFailure() {
        LoginRequest fakeLoginRequest = new LoginRequest("bill", "pass!!!");
        Assertions.assertThrows(UnauthorizedException.class, () -> testMemoryServer.fakeAuthService.login(fakeLoginRequest));
    }

    @Test
    @Order(3)
    @DisplayName("Logout success")
    public void logoutSuccess() throws Exception {
        LoginRequest fakeLoginRequest = new LoginRequest("bill", "pass");
        AuthData authenticatedUser = testMemoryServer.fakeAuthService.login(fakeLoginRequest);
        LogoutRequest logoutRequest = new LogoutRequest(authenticatedUser.authToken());
        Assertions.assertDoesNotThrow(() -> testMemoryServer.fakeAuthService.logout(logoutRequest));
    }

    @Test
    @Order(2)
    @DisplayName("Logout unauthorized")
    public void logoutFailure() {
        LogoutRequest logoutRequest = new LogoutRequest("12345");
        Assertions.assertThrows(UnauthorizedException.class, () -> testMemoryServer.fakeAuthService.logout(logoutRequest));
    }
}
