package dataaccess;

import dataaccess.exceptions.DataAccessException;
import dataaccess.exceptions.UnauthorizedException;
import dataaccess.user.MySqlUserDAO;
import model.*;
import org.junit.jupiter.api.*;

public class SqlAuthServiceTests {

    private static SqlServerTests sqlServerTests;

    @BeforeAll
    public static void init() {

        try {
            sqlServerTests = new SqlServerTests(new MySqlUserDAO());
            sqlServerTests.testAuthService.clearAuth();
            sqlServerTests.testGameService.clearGames();
            sqlServerTests.testUserService.clearUsers();
            AuthData auth = sqlServerTests.testUserService.register(new RegisterRequest("a", "a!", "a@a"));
            sqlServerTests.testAuthService.logout(new LogoutRequest(auth.authToken()));
        } catch (Exception e) {
            throw new RuntimeException(e.getMessage());
        }
    }

    @Test
    @Order(1)
    @DisplayName("Login Success")
    public void sqlLoginSuccess() throws Exception {
        LoginRequest testLoginRequest = new LoginRequest("a", "a!");
        AuthData authenticatedUser = sqlServerTests.testAuthService.login(testLoginRequest);
        Assertions.assertEquals("a", sqlServerTests.testUserService.getUser(authenticatedUser.username()).username(),
                "username did not match logged-in user");
    }

    @Test
    @Order(2)
    @DisplayName("Login wrong password")
    public void sqlLoginFailure() {
        LoginRequest testLoginRequest = new LoginRequest("a", "this is an incorrect password");
        Assertions.assertThrows(UnauthorizedException.class, () -> sqlServerTests.testAuthService.login(testLoginRequest));
    }

    @Test
    @Order(3)
    @DisplayName("Clear games")
    public void sqlClearAuthSuccess() {
        try {
            sqlServerTests.testUserService.register(new RegisterRequest("e", "e!", "e@e"));
            sqlServerTests.testUserService.register(new RegisterRequest("f", "f!", "f@f"));
            sqlServerTests.testUserService.register(new RegisterRequest("g", "g!", "g@g"));
            Assertions.assertDoesNotThrow(() -> sqlServerTests.testAuthService.clearAuth());
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }
}
