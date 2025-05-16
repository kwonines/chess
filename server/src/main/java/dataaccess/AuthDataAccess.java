package dataaccess;

import model.AuthData;

public interface AuthDataAccess {
    void addAuth(AuthData authData);
    AuthData findAuth(String authToken);
    void deleteAuth(String authToken);
}
