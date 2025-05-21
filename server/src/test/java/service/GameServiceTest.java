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
import org.junit.jupiter.api.Order;
import org.junit.jupiter.api.Test;
import service.requestandresult.CreateRequest;
import service.requestandresult.JoinRequest;
import service.requestandresult.ListRequest;

import java.util.ArrayList;

class GameServiceTest {

    static MemoryGameDataAccess gameDataAccess;
    static MemoryAuthDataAccess authDataAccess;
    static GameService gameService;

    @BeforeEach
    void setUp() {
        gameDataAccess = new MemoryGameDataAccess();
        authDataAccess = new MemoryAuthDataAccess();
        gameService = new GameService();
        gameDataAccess.clear();
        authDataAccess.clear();
        authDataAccess.addAuth(new AuthData("username", "authToken"));
    }

    @Test
    @Order(0)
    void listGamesSuccess() throws DataAccessException {
        Assertions.assertEquals(new ArrayList<>(), gameService.listGames(new ListRequest("authToken")).games());
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