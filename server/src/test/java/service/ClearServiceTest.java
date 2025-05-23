package service;

import chess.ChessGame;
import dataaccess.*;
import model.AuthData;
import model.GameData;
import model.UserData;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;
class ClearServiceTest {

    @Test
    void clearApplication() throws DataAccessException {
        UserDataAccess userDataAccess = new SQLUserDataAccess();
        AuthDataAccess authDataAccess = new SQLAuthDataAccess();
        GameDataAccess gameDataAccess = new SQLGameDataAccess();

        userDataAccess.addUser(new UserData("a", "b", "c"));
        authDataAccess.addAuth(new AuthData("username", "exampleToken"));
        gameDataAccess.addGame(new GameData(1, null, null, "name", new ChessGame()));

        ClearService clearService = new ClearService();
        clearService.clearApplication();

        try {
            assertNull(userDataAccess.findUser("a"));
        } catch (dataaccess.DataAccessException e) {
            throw new RuntimeException(e);
        }
        assertNull(authDataAccess.findAuth("username"));
        assertNull(gameDataAccess.findGame(1));
    }
}