package dataaccess;

import model.UserData;

import java.sql.SQLException;

public interface UserDataAccess {
    void clear() throws DataAccessException, SQLException;
    public UserData findUser(String username) throws DataAccessException, SQLException;
    public void addUser(UserData userData) throws DataAccessException;
}
