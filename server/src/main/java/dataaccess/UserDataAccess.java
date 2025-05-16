package dataaccess;

import model.UserData;

public interface UserDataAccess {
    void clear();
    public UserData findUser(String username);
    public void addUser(UserData userData);
}
