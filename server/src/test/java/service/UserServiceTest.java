package service;

import dataaccess.DataAccessException;
import dataaccess.SQLUserDataAccess;
import dataaccess.UserDataAccess;
import dataaccess.exceptions.BadRequestException;
import dataaccess.exceptions.ServerErrorException;
import dataaccess.exceptions.UnauthorizedException;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import model.requestandresult.LoginRequest;
import model.requestandresult.LogoutRequest;
import model.requestandresult.RegisterRequest;

import java.util.UUID;

class UserServiceTest {

    static UserService userService;
    UserDataAccess userDataAccess = new SQLUserDataAccess();

    @BeforeEach
    void setUp() throws ServerErrorException {
        userService = new UserService();
        userDataAccess.clear();
    }

    @Test
    void registerSuccess() throws DataAccessException {
        Assertions.assertNotEquals(UUID.randomUUID().toString(), userService.register(new RegisterRequest
                ("name", "word", "mail")).authToken());
    }

    @Test
    void registerFail() {
        Assertions.assertThrows(BadRequestException.class, () -> userService.register(new RegisterRequest(null, null, null)));
    }

    @Test
    void loginSuccess() throws DataAccessException {
        userService.register(new RegisterRequest("username", "password", "email"));
        Assertions.assertEquals(String.class, userService.login(new LoginRequest("username", "password")).authToken().getClass());
    }

    @Test
    void loginFail() throws DataAccessException {
        userService.register(new RegisterRequest("username", "password", "email"));
        Assertions.assertThrows(UnauthorizedException.class, () -> userService.login
                (new LoginRequest("username", "wrongpassword")));
    }

    @Test
    void logoutSuccess() throws DataAccessException {
        String authToken = userService.register(new RegisterRequest
                ("username", "password", "email")).authToken();
        Assertions.assertDoesNotThrow(() -> userService.logout(new LogoutRequest(authToken)));
    }

    @Test
    void logoutFail() {
        Assertions.assertThrows(UnauthorizedException.class, () -> userService.logout(new LogoutRequest("invalid authToken")));
    }
}