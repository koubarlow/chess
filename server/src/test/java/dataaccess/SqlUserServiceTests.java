package dataaccess;

import dataaccess.exceptions.AlreadyTakenException;
import dataaccess.exceptions.BadRequestException;
import dataaccess.user.MySqlUserDAO;
import model.AuthData;
import model.RegisterRequest;
import org.junit.jupiter.api.*;

public class SqlUserServiceTests {

    private static SqlServerTests sqlServerTests;

    @BeforeAll
    public static void init() {

        try {
            sqlServerTests = new SqlServerTests(new MySqlUserDAO());
            sqlServerTests.testAuthService.clearAuth();
            sqlServerTests.testGameService.clearGames();
            sqlServerTests.testUserService.clearUsers();
        } catch (Exception e) {
            throw new RuntimeException(e.getMessage());
        }
    }

    @Test
    @Order(1)
    @DisplayName("Register success")
    public void sqlCreateUserSuccess() throws Exception {
        //submit register request
        AuthData auth = sqlServerTests.testUserService.register(new RegisterRequest("b", "b!", "b@b"));
        Assertions.assertEquals("b", sqlServerTests.testUserService.getUser(auth.username()).username(),
                "username did not match register user");
    }

    @Test
    @Order(2)
    @DisplayName("Register fail already taken")
    public void sqlCreateUserFail() throws Exception {
        //submit register request
        sqlServerTests.testUserService.register(new RegisterRequest("d", "d!", "d@d"));
        RegisterRequest existingUser = new RegisterRequest("d", "d!", "d@d");
        Assertions.assertThrows(AlreadyTakenException.class, () -> sqlServerTests.testUserService.register(existingUser));
    }

    @Test
    @Order(3)
    @DisplayName("GetUser success")
    public void sqlGetUserSuccess() throws Exception {
        RegisterRequest testRegisterRequest = new RegisterRequest("c", "c!", "c@c");
        AuthData newUser = sqlServerTests.testUserService.register(testRegisterRequest);

        Assertions.assertEquals("c", sqlServerTests.testUserService.getUser(newUser.username()).username(),
                "get user not success");
    }

    @Test
    @Order(4)
    @DisplayName("GetUser fail no username")
    public void sqlGetUserFail() {
        Assertions.assertThrows(BadRequestException.class, () -> sqlServerTests.testUserService.getUser(null));
    }
}
