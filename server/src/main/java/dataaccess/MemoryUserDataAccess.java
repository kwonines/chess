package dataaccess;

import model.UserData;

import java.util.HashMap;

public class MemoryUserDataAccess implements UserDataAccess {
    private static final HashMap<String, UserData> users = new HashMap<>();

    @Override
    public UserData findUser(String username) throws DataAccessException {
        return users.get(username);
    }

    @Override
    public void addUser(UserData userData) {
        users.put(userData.username(), userData);
    }

    @Override
    public void clear() {
        users.clear();
    }
}
