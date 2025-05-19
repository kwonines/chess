package service;

import dataaccess.MemoryAuthDataAccess;
import dataaccess.MemoryGameDataAccess;
import dataaccess.MemoryUserDataAccess;

public class ClearService {


    private final MemoryUserDataAccess userDataAccess;
    private final MemoryAuthDataAccess authDataAccess;
    private final MemoryGameDataAccess gameDataAccess;

    public ClearService(MemoryUserDataAccess userDataAccess, MemoryAuthDataAccess authDataAccess, MemoryGameDataAccess gameDataAccess) {
        this.userDataAccess = userDataAccess;
        this.authDataAccess = authDataAccess;
        this.gameDataAccess = gameDataAccess;
    }

    public void clearApplication() {
        userDataAccess.clear();
        authDataAccess.clear();
        gameDataAccess.clear();
    }
}
