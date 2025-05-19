package service;

import dataaccess.DataAccessException;
import dataaccess.MemoryAuthDataAccess;
import dataaccess.MemoryGameDataAccess;
import dataaccess.exceptions.UnauthorizedException;
import service.RequestAndResult.ListRequest;
import service.RequestAndResult.ListResult;

public class GameService {
    private final MemoryAuthDataAccess authDataAccess;
    private final MemoryGameDataAccess gameDataAccess;

    public GameService(MemoryAuthDataAccess authDataAccess, MemoryGameDataAccess gameDataAccess) {
        this.authDataAccess = authDataAccess;
        this.gameDataAccess = gameDataAccess;
    }

    public ListResult listGames(ListRequest listRequest) throws DataAccessException {
        String authToken = listRequest.authToken();
        if (authDataAccess.findAuth(authToken) == null) {
            throw new UnauthorizedException("Error: unauthorized");
        }
        return new ListResult(gameDataAccess.listGames());
    }
}
