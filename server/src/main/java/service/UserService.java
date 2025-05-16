package service;

import dataaccess.MemoryUserDataAccess;
import model.UserData;
import service.RequestAndResult.LoginRequest;
import service.RequestAndResult.LoginResult;
import service.RequestAndResult.RegisterRequest;
import service.RequestAndResult.RegisterResult;
import java.util.UUID;

public class UserService {
    public RegisterResult register(RegisterRequest registerRequest) {
        MemoryUserDataAccess userDataAccess = new MemoryUserDataAccess();
        if (userDataAccess.findUser(registerRequest.username()) == null) {
            userDataAccess.addUser(new UserData(registerRequest.username(), registerRequest.password(), registerRequest.email()));
            String authToken = UUID.randomUUID().toString();
            return new RegisterResult(registerRequest.username(), authToken);
        } else {return null;}
    }
    public LoginResult login(LoginRequest loginRequest) {
        return null;
    }
    public void logout(RegisterRequest.LogoutRequest logoutRequest) {}
}

