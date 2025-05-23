package dataaccess;

import model.AuthData;

import java.util.HashMap;

public class MemoryAuthDataAccess  implements AuthDataAccess {
    private static final HashMap<String, AuthData> AUTHORIZATIONS = new HashMap<>();

    @Override
    public void addAuth(AuthData authData) {
        AUTHORIZATIONS.put(authData.authToken(), authData);
    }

    @Override
    public AuthData findAuth(String authToken) {
        return AUTHORIZATIONS.get(authToken);
    }

    @Override
    public void deleteAuth(String authToken) {
        AUTHORIZATIONS.remove(authToken);
    }

    @Override
    public void clear() {
        AUTHORIZATIONS.clear();
    }
}
