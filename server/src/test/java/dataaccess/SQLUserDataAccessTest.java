package dataaccess;

import dataaccess.exceptions.ServerErrorException;
import model.UserData;
import org.junit.jupiter.api.*;
import org.mindrot.jbcrypt.BCrypt;
import server.Server;
import java.sql.Connection;

class SQLUserDataAccessTest {

    static Server server;
    static UserDataAccess dataAccess;

    @BeforeAll
    static void setUp() {
        server = new Server();
        server.run(0);
        dataAccess = new SQLUserDataAccess();
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
        dataAccess.addUser(new UserData("username", "password", "email"));
        dataAccess.clear();
        try (Connection connection = DatabaseManager.getConnection()) {
            try (var statement = connection.prepareStatement("SELECT email FROM users WHERE username =username")) {
                try (var result = statement.executeQuery()) {
                    Assertions.assertFalse(result.next());
                }
            }
        }
    }

    @Test
    void findUserSuccess() throws Exception {
        dataAccess.addUser(new UserData("username", "password", "email"));
        UserData userData = dataAccess.findUser("username");
        Assertions.assertNotNull(userData);
        Assertions.assertEquals("username", userData.username());
    }

    @Test
    void findUserFail() throws Exception {
        Assertions.assertNull(dataAccess.findUser("username"));
        dataAccess.addUser(new UserData("username", "password", "email"));
        Assertions.assertNull(dataAccess.findUser(null));
        Assertions.assertNull(dataAccess.findUser("wrongUsername"));
    }

    @Test
    void addUserSuccess() throws Exception {
        dataAccess.addUser(new UserData("username", "password", "email"));
        try (Connection connection = DatabaseManager.getConnection()) {
            try (var statement = connection.prepareStatement("SELECT username, password, email FROM users WHERE username =username")) {
                try (var result = statement.executeQuery()) {
                    if (result.next()) {
                        Assertions.assertEquals("username", result.getString("username"));
                        Assertions.assertTrue(BCrypt.checkpw("password", result.getString("password")));
                        Assertions.assertEquals("email", result.getString("email"));
                    }
                }
            }
        }
    }

    @Test
    void addUserFail() {
        Assertions.assertThrows(DataAccessException.class, () -> dataAccess.addUser(new UserData("username", null, null)));
        Assertions.assertThrows(DataAccessException.class, () -> dataAccess.addUser(new UserData(null, "password", null)));
        Assertions.assertThrows(DataAccessException.class, () -> dataAccess.addUser(new UserData(null, null,"email")));
    }
}