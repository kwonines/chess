package service;

import dataaccess.*;

public class ClearService {

    private final UserDataAccess userDataAccess = new MemoryUserDataAccess();
    private final AuthDataAccess authDataAccess = new MemoryAuthDataAccess();
    private final GameDataAccess gameDataAccess = new MemoryGameDataAccess();

    public void clearApplication() {
        userDataAccess.clear();
        authDataAccess.clear();
        gameDataAccess.clear();
    }
}
