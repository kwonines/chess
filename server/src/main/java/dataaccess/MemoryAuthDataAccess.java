package dataaccess;

import model.AuthData;

import java.util.HashMap;

public class MemoryAuthDataAccess  implements AuthDataAccess {
    private static final HashMap<String, AuthData> authorizations = new HashMap<>();

    @Override
    public void addAuth(AuthData authData) {
        authorizations.put(authData.authToken(), authData);
    }

    @Override
    public AuthData findAuth(String authToken) {
        return authorizations.get(authToken);
    }

    @Override
    public void deleteAuth(String authToken) {
        authorizations.remove(authToken);
    }

    @Override
    public void clear() {
        authorizations.clear();
    }
}
