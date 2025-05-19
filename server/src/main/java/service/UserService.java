package service;

import dataaccess.MemoryAuthDataAccess;
import dataaccess.exceptions.BadRequestException;
import dataaccess.DataAccessException;
import dataaccess.MemoryUserDataAccess;
import dataaccess.exceptions.UnauthorizedException;
import dataaccess.exceptions.UsernameTakenException;
import model.AuthData;
import model.UserData;
import service.RequestAndResult.*;
import java.util.UUID;

public class UserService {

    private final MemoryUserDataAccess userDataAccess;
    private final MemoryAuthDataAccess authDataAccess;

    public UserService(MemoryUserDataAccess userDataAccess, MemoryAuthDataAccess authDataAccess) {
        this.userDataAccess = userDataAccess;
        this.authDataAccess = authDataAccess;
    }

    public RegisterResult register(RegisterRequest registerRequest) throws DataAccessException {
        if (registerRequest.username() == null || registerRequest.password() == null || registerRequest.email() == null) {
            throw new BadRequestException("Error: Request was invalid, please try again");
        }
        if (userDataAccess.findUser(registerRequest.username()) == null) {
            userDataAccess.addUser(new UserData(registerRequest.username(), registerRequest.password(), registerRequest.email()));
            String authToken = UUID.randomUUID().toString();
            authDataAccess.addAuth(new AuthData(registerRequest.username(), authToken));
            return new RegisterResult(registerRequest.username(), authToken);
        } else {
            throw new UsernameTakenException("Error: Username taken");
        }
    }

    public LoginResult login(LoginRequest loginRequest) throws DataAccessException {
        UserData user = userDataAccess.findUser(loginRequest.username());
        if (loginRequest.username() == null || loginRequest.password() == null) {
            throw new BadRequestException("Error: bad request");
        }
        if (user == null) {
            throw new UnauthorizedException("Error: Username not found");
        }
        if (loginRequest.password().equals(user.password())) {
            String authToken = UUID.randomUUID().toString();
            return new LoginResult(loginRequest.username(), authToken);
        } else {
            throw new UnauthorizedException("Error: Incorrect password");
        }
    }

    public void logout(LogoutRequest logoutRequest) throws DataAccessException {
        AuthData authData = authDataAccess.findAuth(logoutRequest.authToken());
        if (authData == null) {
            throw new UnauthorizedException("Error: unauthorized");
        }
        authDataAccess.deleteAuth(authData.authToken());
    }
}

