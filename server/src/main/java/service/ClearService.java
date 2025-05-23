package service;

import dataaccess.*;
import dataaccess.exceptions.ServerErrorException;

public class ClearService {

    private final UserDataAccess userDataAccess = new SQLUserDataAccess();
    private final AuthDataAccess authDataAccess = new SQLAuthDataAccess();
    private final GameDataAccess gameDataAccess = new SQLGameDataAccess();

    public void clearApplication() throws ServerErrorException {
        try {
            userDataAccess.clear();
            authDataAccess.clear();
            gameDataAccess.clear();
        } catch (ServerErrorException exception) {
            throw new ServerErrorException(exception.getMessage());
        }
    }
}
