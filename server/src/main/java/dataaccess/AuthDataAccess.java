package dataaccess;

import dataaccess.exceptions.ServerErrorException;
import model.AuthData;

public interface AuthDataAccess {
    void clear() throws ServerErrorException;
    void addAuth(AuthData authData) throws DataAccessException;
    AuthData findAuth(String authToken) throws ServerErrorException;
    void deleteAuth(String authToken) throws ServerErrorException;
}
