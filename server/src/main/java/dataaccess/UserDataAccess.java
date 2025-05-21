package dataaccess;

import model.UserData;

public interface UserDataAccess {
    void clear();
    UserData findUser(String username) throws DataAccessException;
    void addUser(UserData userData) throws DataAccessException;
}
