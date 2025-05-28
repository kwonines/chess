package service;

import dataaccess.*;
import dataaccess.exceptions.BadRequestException;
import dataaccess.exceptions.ServerErrorException;
import dataaccess.exceptions.UnauthorizedException;
import dataaccess.exceptions.UsernameTakenException;
import model.AuthData;
import model.UserData;
import model.requestandresult.*;
import org.mindrot.jbcrypt.BCrypt;

import java.util.UUID;

public class UserService {

    UserDataAccess userDataAccess = new SQLUserDataAccess();
    AuthDataAccess authDataAccess = new SQLAuthDataAccess();

    public RegisterResult register(RegisterRequest registerRequest) throws DataAccessException {
        if (registerRequest.username() == null || registerRequest.password() == null || registerRequest.email() == null) {
            throw new BadRequestException("Error: Request was invalid, please try again");
        }
        try {
            if (userDataAccess.findUser(registerRequest.username()) == null) {
                userDataAccess.addUser(new UserData(registerRequest.username(), registerRequest.password(), registerRequest.email()));
                String authToken = UUID.randomUUID().toString();
                authDataAccess.addAuth(new AuthData(registerRequest.username(), authToken));
                return new RegisterResult(registerRequest.username(), authToken);
            } else {
                throw new UsernameTakenException("Error: Username taken");
            }
        } catch (ServerErrorException exception) {
            throw new ServerErrorException(exception.getMessage());
        }
    }

    public LoginResult login(LoginRequest loginRequest) throws DataAccessException {
        try {
                UserData user = userDataAccess.findUser(loginRequest.username());
            if (loginRequest.username() == null || loginRequest.password() == null) {
                throw new BadRequestException("Error: bad request");
            }
            if (user == null) {
                throw new UnauthorizedException("Error: Username not found");
            }
            if (BCrypt.checkpw(loginRequest.password(), user.password())) {
                String authToken = UUID.randomUUID().toString();
                authDataAccess.addAuth(new AuthData(user.username(), authToken));
                return new LoginResult(loginRequest.username(), authToken);
            } else {
                throw new UnauthorizedException("Error: Incorrect password");
            }
        } catch (ServerErrorException exception) {
            throw new ServerErrorException(exception.getMessage());
        }
    }

    public void logout(LogoutRequest logoutRequest) throws DataAccessException {
        try {
            AuthData authData = authDataAccess.findAuth(logoutRequest.authToken());
            if (authData == null) {
                throw new UnauthorizedException("Error: unauthorized");
            }
            authDataAccess.deleteAuth(authData.authToken());
        } catch (ServerErrorException exception) {
            throw new ServerErrorException(exception.getMessage());
        }
    }
}

