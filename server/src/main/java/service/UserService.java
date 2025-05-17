package service;

import dataaccess.DataAccessException;
import dataaccess.MemoryUserDataAccess;
import model.UserData;
import service.RequestAndResult.*;
import java.util.UUID;

public class UserService {
    MemoryUserDataAccess userDataAccess = new MemoryUserDataAccess();

    public RegisterResult register(RegisterRequest registerRequest) throws DataAccessException {
        if (userDataAccess.findUser(registerRequest.username()) == null) {
            userDataAccess.addUser(new UserData(registerRequest.username(), registerRequest.password(), registerRequest.email()));
            String authToken = UUID.randomUUID().toString();
            return new RegisterResult(registerRequest.username(), authToken);
        } else {
            throw new DataAccessException("Error: Username taken");
        }
    }
    public LoginResult login(LoginRequest loginRequest) {
        return null;
    }
    public void logout(LogoutRequest logoutRequest) {}
}

