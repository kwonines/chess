package service;

import chess.ChessGame;
import dataaccess.*;
import dataaccess.exceptions.BadRequestException;
import dataaccess.exceptions.ColorTakenException;
import dataaccess.exceptions.ServerErrorException;
import dataaccess.exceptions.UnauthorizedException;
import model.GameData;
import model.requestandresult.*;

public class GameService {
    private final AuthDataAccess authDataAccess = new SQLAuthDataAccess();
    private final GameDataAccess gameDataAccess = new SQLGameDataAccess();

    public ListResult listGames(ListRequest listRequest) throws DataAccessException {
        try {
            if (authDataAccess.findAuth(listRequest.authToken()) == null) {
                throw new UnauthorizedException("Error: unauthorized");
            }
            return new ListResult(gameDataAccess.listGames());
        } catch (ServerErrorException exception) {
            throw new ServerErrorException(exception.getMessage());
        }
    }

    public CreateResult createGame(CreateRequest createRequest) throws DataAccessException {
        if (createRequest.gameName() == null) {
            throw new BadRequestException("Error: need to include a game name");
        }
        try {
            if (authDataAccess.findAuth(createRequest.authToken()) == null) {
                throw new UnauthorizedException("Error: unauthorized");
            }
            int gameID = gameDataAccess.listGames().size() + 1;
            gameDataAccess.addGame(new GameData(gameID, null, null, createRequest.gameName(), new ChessGame()));
            return new CreateResult(gameID);
        } catch (ServerErrorException exception) {
            throw new ServerErrorException(exception.getMessage());
        }
    }

    public void joinGame(JoinRequest joinRequest) throws DataAccessException {
        try {
            GameData oldGame = gameDataAccess.findGame(joinRequest.gameID());

            if (joinRequest.playerColor() == null || oldGame == null) {
                throw new BadRequestException("Error: bad request");
            }
            if (authDataAccess.findAuth(joinRequest.authToken()) == null) {
                throw new UnauthorizedException("Error: unauthorized");
            }

            String user = authDataAccess.findAuth(joinRequest.authToken()).username();

            if (joinRequest.playerColor() == ChessGame.TeamColor.WHITE) {
                if (gameDataAccess.findGame(joinRequest.gameID()).whiteUsername() != null) {
                    throw new ColorTakenException("Error: color is already taken");
                }
                gameDataAccess.updateGame(joinRequest.gameID(),
                        new GameData(oldGame.gameID(), user, oldGame.blackUsername(), oldGame.gameName(), oldGame.game()));
            }
            if (joinRequest.playerColor() == ChessGame.TeamColor.BLACK) {
                if (gameDataAccess.findGame(joinRequest.gameID()).blackUsername() != null) {
                    throw new ColorTakenException("Error: color is already taken");
                }
                gameDataAccess.updateGame(joinRequest.gameID(),
                        new GameData(oldGame.gameID(), oldGame.whiteUsername(), user, oldGame.gameName(), oldGame.game()));
            }
        } catch (ServerErrorException exception) {
            throw new ServerErrorException(exception.getMessage());
        }
    }

}
