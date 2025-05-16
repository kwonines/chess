package dataaccess;

import model.UserData;

public interface UserDataAccess {
    public UserData findUser(String username);
    public void addUser(UserData userData);
}
