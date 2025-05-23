package dataaccess;

import model.UserData;

import java.util.HashMap;

public class MemoryUserDataAccess implements UserDataAccess {
    private static final HashMap<String, UserData> USERS = new HashMap<>();

    @Override
    public UserData findUser(String username) {
        return USERS.get(username);
    }

    @Override
    public void addUser(UserData userData) {
        USERS.put(userData.username(), userData);
    }

    @Override
    public void clear() {
        USERS.clear();
    }
}
