package service;

import dataaccess.MemoryAuthDataAccess;
import dataaccess.exceptions.BadRequestException;
import dataaccess.DataAccessException;
import dataaccess.MemoryUserDataAccess;
import dataaccess.exceptions.IncorrectPasswordException;
import dataaccess.exceptions.UserDoesNotExistException;
import dataaccess.exceptions.UsernameTakenException;
import model.AuthData;
import model.UserData;
import service.RequestAndResult.*;

import java.util.Objects;
import java.util.UUID;

public class UserService {
    MemoryUserDataAccess userDataAccess = new MemoryUserDataAccess();
    MemoryAuthDataAccess authDataAccess = new MemoryAuthDataAccess();

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
        if (user == null) {
            throw new UserDoesNotExistException("Error: Username does not exist");
        } else {
            if (Objects.equals(loginRequest.password(), user.password())) {
                String authToken = UUID.randomUUID().toString();
                return new LoginResult(loginRequest.username(), authToken);
            } else {
                throw new IncorrectPasswordException("Error: Incorrect password");
            }
        }

    }

    public void logout(LogoutRequest logoutRequest) {

    }
}

