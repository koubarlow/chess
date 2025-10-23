package service;

import dataaccess.BadRequestException;
import model.AuthData;
import model.RegisterRequest;
import org.junit.jupiter.api.*;
import passoff.model.TestAuthResult;

public class UserServiceTests {

    private static FakeServer fakeServer;

    @BeforeAll
    public static void init() {
        fakeServer = new FakeServer();
    }

    @Test
    @Order(1)
    @DisplayName("Success UserService.register")
    public void createUserSuccess() throws Exception {
        //submit register request
        RegisterRequest fakeRegisterRequest = new RegisterRequest("kou", "12345", "kou@test.com");
        AuthData newUser = fakeServer.fakeUserService.register(fakeRegisterRequest);
        Assertions.assertEquals("kou", fakeServer.fakeUserService.getUser(newUser.username()).username(),
                "username did not match register user");
    }

    @Test
    @Order(2)
    @DisplayName("Fail UserService.register")
    public void createUserFail() {
        //submit register request
        RegisterRequest fakeRegisterRequest = new RegisterRequest(null, "12345", "kou@test.com");
        Assertions.assertThrows(BadRequestException.class, () -> fakeServer.fakeUserService.register(fakeRegisterRequest));
    }

    @Test
    @Order(2)
    @DisplayName("Success UserService.getUser")
    public void getUserSuccess() throws Exception {
        RegisterRequest fakeRegisterRequest = new RegisterRequest("ben", "54321", "ben@test.com");
        AuthData newUser = fakeServer.fakeUserService.register(fakeRegisterRequest);

        Assertions.assertEquals("ben", fakeServer.fakeUserService.getUser(newUser.username()).username(),
                "get user not success");
    }

    @Test
    @Order(1)
    @DisplayName("Failure UserService.getUser")
    public void getUserFail() {
        Assertions.assertThrows(BadRequestException.class, () -> fakeServer.fakeUserService.getUser(null));
    }
}
