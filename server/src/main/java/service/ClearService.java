package service;

import dataaccess.*;
public class ClearService {

    private final UserDataAccess userDataAccess = new SQLUserDataAccess();
    private final AuthDataAccess authDataAccess = new MemoryAuthDataAccess();
    private final GameDataAccess gameDataAccess = new SQLGameDataAccess();

    public void clearApplication() {
        userDataAccess.clear();
        authDataAccess.clear();
        gameDataAccess.clear();
    }
}
