package service;

import dataaccess.exceptions.BadRequestException;
import model.AuthData;
import model.RegisterRequest;
import org.junit.jupiter.api.*;

public class UserServiceTests {

    private static TestMemoryServer testMemoryServer;

    @BeforeAll
    public static void init() {
        testMemoryServer = new TestMemoryServer();
    }

    @Test
    @Order(1)
    @DisplayName("Register success")
    public void createUserSuccess() throws Exception {
        //submit register request
        RegisterRequest fakeRegisterRequest = new RegisterRequest("kou", "12345", "kou@test.com");
        AuthData newUser = testMemoryServer.fakeUserService.register(fakeRegisterRequest);
        Assertions.assertEquals("kou", testMemoryServer.fakeUserService.getUser(newUser.username()).username(),
                "username did not match register user");
    }

    @Test
    @Order(2)
    @DisplayName("Register fail no username")
    public void createUserFail() {
        //submit register request
        RegisterRequest fakeRegisterRequest = new RegisterRequest(null, "12345", "kou@test.com");
        Assertions.assertThrows(BadRequestException.class, () -> testMemoryServer.fakeUserService.register(fakeRegisterRequest));
    }

    @Test
    @Order(3)
    @DisplayName("GetUser success")
    public void getUserSuccess() throws Exception {
        RegisterRequest fakeRegisterRequest = new RegisterRequest("ben", "54321", "ben@test.com");
        AuthData newUser = testMemoryServer.fakeUserService.register(fakeRegisterRequest);

        Assertions.assertEquals("ben", testMemoryServer.fakeUserService.getUser(newUser.username()).username(),
                "get user not success");
    }

    @Test
    @Order(4)
    @DisplayName("GetUser fail no username")
    public void getUserFail() {
        Assertions.assertThrows(BadRequestException.class, () -> testMemoryServer.fakeUserService.getUser(null));
    }
}
