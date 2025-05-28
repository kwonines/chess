package service;

import chess.ChessGame;
import dataaccess.*;
import dataaccess.exceptions.BadRequestException;
import dataaccess.exceptions.ColorTakenException;
import dataaccess.exceptions.UnauthorizedException;
import model.AuthData;
import model.GameData;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Order;
import org.junit.jupiter.api.Test;
import model.requestandresult.CreateRequest;
import model.requestandresult.JoinRequest;
import model.requestandresult.ListRequest;

import java.util.ArrayList;

class GameServiceTest {

    static GameDataAccess gameDataAccess;
    static AuthDataAccess authDataAccess;
    static GameService gameService;

    @BeforeEach
    void setUp() throws DataAccessException {
        gameDataAccess = new SQLGameDataAccess();
        authDataAccess = new SQLAuthDataAccess();
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