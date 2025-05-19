package service;

import chess.ChessGame;
import dataaccess.DataAccessException;
import dataaccess.MemoryAuthDataAccess;
import dataaccess.MemoryGameDataAccess;
import dataaccess.exceptions.BadRequestException;
import dataaccess.exceptions.ColorTakenException;
import dataaccess.exceptions.UnauthorizedException;
import model.AuthData;
import model.GameData;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import service.RequestAndResult.CreateRequest;
import service.RequestAndResult.JoinRequest;
import service.RequestAndResult.ListRequest;

class GameServiceTest {

    static MemoryGameDataAccess gameDataAccess = new MemoryGameDataAccess();
    static MemoryAuthDataAccess authDataAccess = new MemoryAuthDataAccess();
    static GameService gameService;

    @BeforeEach
    void setUp() {
        gameService = new GameService(authDataAccess, gameDataAccess);
        authDataAccess.addAuth(new AuthData("username", "authToken"));
    }

    @Test
    void listGamesSuccess() {
        Assertions.assertDoesNotThrow(() -> gameService.listGames(new ListRequest("authToken")));
    }

    @Test
    void listGamesFail() {
        Assertions.assertThrows(UnauthorizedException.class, () -> gameService.listGames(new ListRequest(null)));
    }

    @Test
    void createGameSuccess() throws DataAccessException {
        int gameID = gameService.createGame(new CreateRequest("authToken", "gameName")).gameID();
        Assertions.assertEquals(gameDataAccess.findGame(gameID),
                new GameData(gameID, null, null, "gameName", new ChessGame()));
    }

    @Test
    void createGameFail() {
        Assertions.assertThrows(BadRequestException.class, () ->
                gameService.createGame(new CreateRequest("authToken", null)));
    }

    @Test
    void joinGameSuccess() throws DataAccessException {
        int gameID = gameService.createGame(new CreateRequest("authToken", "gameName")).gameID();
        gameService.joinGame(new JoinRequest(ChessGame.TeamColor.WHITE, gameID, "authToken"));
        Assertions.assertEquals("username", gameDataAccess.findGame(gameID).whiteUsername());
    }

    @Test
    void joinGameFail() throws DataAccessException {
        int gameID = gameService.createGame(new CreateRequest("authToken", "gameName")).gameID();

        gameService.joinGame(new JoinRequest(ChessGame.TeamColor.WHITE, gameID, "authToken"));
        Assertions.assertThrows(ColorTakenException.class, () ->
                gameService.joinGame(new JoinRequest(ChessGame.TeamColor.WHITE, gameID, "authToken")));
    }
}