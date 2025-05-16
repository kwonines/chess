package dataaccess;

import model.UserData;

import java.util.HashMap;

public class MemoryUserDataAccess implements UserDataAccess {
    private final HashMap<String, UserData> users = new HashMap<>();

    @Override
    public UserData findUser(String username) {
        return users.get(username);
    }

    @Override
    public void addUser(UserData userData) {
        users.put(userData.username(), userData);
    }
}
