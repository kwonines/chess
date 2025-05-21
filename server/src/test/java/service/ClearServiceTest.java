package service;

import chess.ChessGame;
import dataaccess.MemoryAuthDataAccess;
import dataaccess.MemoryGameDataAccess;
import dataaccess.MemoryUserDataAccess;
import model.AuthData;
import model.GameData;
import model.UserData;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;
class ClearServiceTest {

    @Test
    void clearApplication() {
        MemoryUserDataAccess userDataAccess = new MemoryUserDataAccess();
        MemoryAuthDataAccess authDataAccess = new MemoryAuthDataAccess();
        MemoryGameDataAccess gameDataAccess = new MemoryGameDataAccess();

        userDataAccess.addUser(new UserData("a", "b", "c"));
        authDataAccess.addAuth(new AuthData("username", "exampleToken"));
        gameDataAccess.addGame(new GameData(1, null, null, "name", new ChessGame()));

        ClearService clearService = new ClearService();
        clearService.clearApplication();

        assertNull(userDataAccess.findUser("a"));
        assertNull(authDataAccess.findAuth("username"));
        assertNull(gameDataAccess.findGame(1));
    }
}