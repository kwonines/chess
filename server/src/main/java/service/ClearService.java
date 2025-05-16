package service;

import dataaccess.MemoryAuthDataAccess;
import dataaccess.MemoryGameDataAccess;
import dataaccess.MemoryUserDataAccess;

public class ClearService {
    public void clearApplication() {
        new MemoryAuthDataAccess().clear();
        new MemoryGameDataAccess().clear();
        new MemoryUserDataAccess().clear();
    }
}
