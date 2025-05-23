package dataaccess;

import dataaccess.exceptions.ServerErrorException;
import model.AuthData;
import org.junit.jupiter.api.*;
import server.Server;
import java.sql.Connection;

class SQLAuthDataAccessTest {

    static Server server;
    static AuthDataAccess dataAccess;

    @BeforeAll
    static void setUp() {
        server = new Server();
        server.run(0);
        dataAccess = new SQLAuthDataAccess();
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
        dataAccess.addAuth(new AuthData("username", "authToken"));
        dataAccess.clear();
        try (Connection connection = DatabaseManager.getConnection()) {
            try (var statement = connection.prepareStatement("SELECT authtoken FROM authorizations WHERE username =username")) {
                try (var result = statement.executeQuery()) {
                    Assertions.assertFalse(result.next());
                }
            }
        }
    }

    @Test
    void addAuthSuccess() throws Exception {
        dataAccess.addAuth(new AuthData("username", "authToken"));
        try (Connection connection = DatabaseManager.getConnection()) {
            try (var statement = connection.prepareStatement("SELECT username, authToken FROM authorizations WHERE username =username")) {
                try (var result = statement.executeQuery()) {
                    if (result.next()) {
                        Assertions.assertEquals("username", result.getString("username"));
                        Assertions.assertEquals("authToken", result.getString("authToken"));
                    }
                }
            }
        }
    }

    @Test
    void addAuthFail() {
        Assertions.assertThrows(DataAccessException.class, () -> dataAccess.addAuth(new AuthData("username", null)));
        Assertions.assertThrows(DataAccessException.class, () -> dataAccess.addAuth(new AuthData(null, "authToken")));
    }

    @Test
    void findAuthSuccess() throws Exception {
        dataAccess.addAuth(new AuthData("username", "authToken"));
        AuthData authData = dataAccess.findAuth("authToken");
        Assertions.assertNotNull(authData);
        Assertions.assertEquals("authToken", authData.authToken());
    }

    @Test
    void findAuthFail() throws Exception {
        Assertions.assertNull(dataAccess.findAuth("authToken"));
        dataAccess.addAuth(new AuthData("username", "authToken"));
        Assertions.assertNull(dataAccess.findAuth(null));
        Assertions.assertNull(dataAccess.findAuth("wrongAuthToken"));
    }

    @Test
    void deleteAuthSuccess() throws Exception {
        dataAccess.addAuth(new AuthData("username", "authToken"));
        dataAccess.deleteAuth(null);
        Assertions.assertNotNull(dataAccess.findAuth("authToken"));
        dataAccess.deleteAuth("authToken");
        Assertions.assertNull(dataAccess.findAuth("authToken"));
    }

    @Test
    void deleteAuthFail() throws Exception {
        dataAccess.addAuth(new AuthData("username", "authToken"));
        dataAccess.deleteAuth("badAuthToken");
        Assertions.assertNotNull(dataAccess.findAuth("authToken"));

    }
}