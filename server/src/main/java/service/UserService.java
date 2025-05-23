package service;

import dataaccess.*;
import dataaccess.exceptions.BadRequestException;
import dataaccess.exceptions.UnauthorizedException;
import dataaccess.exceptions.UsernameTakenException;
import model.AuthData;
import model.UserData;
import service.requestandresult.*;

import java.util.UUID;

public class UserService {

    UserDataAccess userDataAccess = new SQLUserDataAccess();
    AuthDataAccess authDataAccess = new SQLAuthDataAccess();

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
            authDataAccess.addAuth(new AuthData(user.username(), authToken));
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

