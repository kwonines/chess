package dataaccess;

import chess.ChessGame;
import dataaccess.exceptions.ServerErrorException;
import model.GameData;
import org.junit.jupiter.api.*;
import server.Server;
import java.sql.Connection;

class SQLGameDataAccessTest {

    static Server server;
    static GameDataAccess dataAccess;

    @BeforeAll
    static void setUp() {
        server = new Server();
        server.run(0);
        dataAccess = new SQLGameDataAccess();
    }

    @BeforeEach
    void reset() throws ServerErrorException {
        dataAccess.clear();
    }

    @AfterAll
    static void takeDown() throws ServerErrorException {
        dataAccess.clear();
        server.stop();
    }

    @Test
    void clear() throws Exception {
        dataAccess.addGame(new GameData(5, null, null, "name", new ChessGame()));
        dataAccess.clear();
        try (Connection connection = DatabaseManager.getConnection()) {
            try (var statement = connection.prepareStatement("SELECT gameID FROM games")) {
                try (var result = statement.executeQuery()) {
                    Assertions.assertFalse(result.next());
                }
            }
        }
    }

    @Test
    void addGameSuccess() throws Exception {
        dataAccess.addGame(new GameData(5, null, null, "name", new ChessGame()));
        try (Connection connection = DatabaseManager.getConnection()) {
            try (var statement = connection.prepareStatement("SELECT gameID FROM games")) {
                try (var result = statement.executeQuery()) {
                    Assertions.assertTrue(result.next());
                }
            }
        }
    }

    @Test
    void addGameFail() {
        Assertions.assertThrows(DataAccessException.class, () -> dataAccess.addGame(new GameData(5, null, null, null, null)));
    }

    @Test
    void findGameSuccess() throws Exception {
        dataAccess.addGame(new GameData(1, null, null, "name", new ChessGame()));
        Assertions.assertNotNull(dataAccess.findGame(1));
    }

    @Test
    void findGameFail() throws Exception {
        Assertions.assertNull(dataAccess.findGame(1));
        dataAccess.addGame(new GameData(1, null, null, "name", new ChessGame()));
        Assertions.assertNull(dataAccess.findGame(42));
    }

    @Test
    void listGamesSuccess() throws Exception {
        dataAccess.addGame(new GameData(1, null, null, "name", new ChessGame()));
        dataAccess.addGame(new GameData(2, null, null, "name", new ChessGame()));
        dataAccess.addGame(new GameData(3, null, null, "name", new ChessGame()));
        Assertions.assertEquals(3, dataAccess.listGames().size());
    }

    @Test
    void listGamesFail() throws Exception {
        Assertions.assertEquals(0, dataAccess.listGames().size());
    }

    @Test
    void updateGameSuccess() throws Exception {
        dataAccess.addGame(new GameData(1, null, null, "name", new ChessGame()));
        GameData newGame = new GameData(1, "myUsername", null, "name", new ChessGame());
        dataAccess.updateGame(1, newGame);
        Assertions.assertEquals(newGame, dataAccess.findGame(1));
        newGame = new GameData(1, "myUsername", "anotherUser", "name", new ChessGame());
        dataAccess.updateGame(1, newGame);
        Assertions.assertEquals(newGame, dataAccess.findGame(1));
    }

    @Test
    void updateGameFail() throws Exception {
        GameData newGame = new GameData(1, "myUsername", null, "name", new ChessGame());
        dataAccess.updateGame(1, newGame);
        Assertions.assertNull(dataAccess.findGame(1));
    }
}