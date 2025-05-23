package dataaccess;

import dataaccess.exceptions.ServerErrorException;
import model.UserData;

public interface UserDataAccess {
    void clear() throws ServerErrorException;
    UserData findUser(String username) throws DataAccessException;
    void addUser(UserData userData) throws DataAccessException;
}
