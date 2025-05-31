package client;

import chess.ChessGame;
import dataaccess.exceptions.ServerErrorException;
import model.GameData;
import org.junit.jupiter.api.*;
import server.Server;
import service.ClearService;
import ui.ResponseException;
import ui.ServerFacade;

import java.util.ArrayList;

class ServerFacadeTests {

    private static Server server;
    static ServerFacade facade;
    static ClearService clear = new ClearService();
    String existingAuth;

    @BeforeAll
    public static void init() {
        server = new Server();
        var port = server.run(0);
        System.out.println("Started test HTTP server on " + port);
        facade = new ServerFacade(port);
    }

    @BeforeEach
    public void reset() throws ServerErrorException {
        clear.clearApplication();
        existingAuth = facade.register("existingUser", "existingPassword", "existingEmail").authToken();
    }

    @AfterAll
    static void stopServer() {
        server.stop();
    }

    @Test
    void registerSuccess() {
        String auth2 = facade.register("user2", "pass2", "email").authToken();
        Assertions.assertNotEquals(existingAuth, auth2);
    }

    @Test
    void registerFail() {
        Assertions.assertThrows(ResponseException.class, () -> facade.register("existingUser", "newpassword", "newemail"));
    }

    @Test
    void logoutSuccess() {
        facade.logout(existingAuth);
        Assertions.assertThrows(ResponseException.class, () -> facade.createGame(existingAuth, "name"));
    }

    @Test
    void logoutFailure() {
        facade.logout(existingAuth);
        Assertions.assertThrows(ResponseException.class, () -> facade.logout(existingAuth));
    }

    @Test
    void loginSuccess() {
        facade.logout(existingAuth);
        String newAuth = facade.login("existingUser", "existingPassword").authToken();
        Assertions.assertDoesNotThrow(() -> facade.createGame(newAuth, "newGame"));
    }

    @Test
    void loginFail() {
        Assertions.assertThrows(ResponseException.class, () -> facade.login("existingUser", "wrongPassword"));
    }

    @Test
    void listGamesSuccess() {
        Assertions.assertEquals(new ArrayList<GameData>(), facade.listGames(existingAuth).games());
    }

    @Test
    void listGamesFail() {
        Assertions.assertThrows(ResponseException.class, () -> facade.listGames("badAuth"));
    }

    @Test
    void createGameSuccess() {
        facade.createGame(existingAuth, "newGame");
        Assertions.assertEquals("newGame", facade.listGames(existingAuth).games().getFirst().gameName());
    }

    @Test
    void createGameFail() {
        Assertions.assertThrows(ResponseException.class, () -> facade.createGame(existingAuth, null));
    }

    @Test
    void joinGameSuccess() {
        facade.createGame(existingAuth, "newGame");
        int gameID = facade.listGames(existingAuth).games().getFirst().gameID();
        facade.joinGame(ChessGame.TeamColor.WHITE, gameID, existingAuth);
        Assertions.assertNotNull(facade.listGames(existingAuth).games().getFirst().whiteUsername());
    }

    @Test
    void joinGameFail() {
        Assertions.assertThrows(ResponseException.class, () -> facade.joinGame(null, 5, existingAuth));
    }
}