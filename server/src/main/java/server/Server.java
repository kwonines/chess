package server;

import com.google.gson.Gson;
import dataaccess.MemoryAuthDataAccess;
import dataaccess.MemoryGameDataAccess;
import dataaccess.MemoryUserDataAccess;
import dataaccess.exceptions.BadRequestException;
import dataaccess.DataAccessException;
import dataaccess.exceptions.UnauthorizedException;
import dataaccess.exceptions.UsernameTakenException;
import service.ClearService;
import service.RequestAndResult.*;
import service.UserService;
import spark.*;

import java.util.Iterator;
import java.util.Set;

public class Server {

    UserService userService;
    ClearService clearService;
    MemoryUserDataAccess userDataAccess = new MemoryUserDataAccess();
    MemoryAuthDataAccess authDataAccess = new MemoryAuthDataAccess();
    MemoryGameDataAccess gameDataAccess = new MemoryGameDataAccess();

    public Server() {
        userService = new UserService(userDataAccess, authDataAccess);
        clearService = new ClearService(userDataAccess, authDataAccess, gameDataAccess);
    }

    public int run(int desiredPort) {
        Spark.port(desiredPort);

        Spark.staticFiles.location("web");

        // Register your endpoints and handle exceptions here.
        Spark.delete("/db", this::clear);
        Spark.post("/user", this::register);
        Spark.post("/session", this::login);
        Spark.delete("/session", this::logout);

        //This line initializes the server and can be removed once you have a functioning endpoint 
        Spark.init();

        Spark.awaitInitialization();
        return Spark.port();
    }

    private Object logout(Request request, Response response) throws DataAccessException {
        Gson gson = new Gson();
        String header = request.headers("Authorization");

        LogoutRequest logoutRequest = new LogoutRequest(header);
        try {
            userService.logout(logoutRequest);
            return gson.toJson(null);
        } catch (UnauthorizedException exception) {
            response.status(401);
            return gson.toJson(new ErrorMessage(exception.getMessage()));
        }
    }

    private Object clear(Request request, Response response) {
        clearService.clearApplication();
        return new Gson().toJson(null);
    }

    private Object register(Request request, Response response) throws DataAccessException {
        Gson gson = new Gson();
        RegisterRequest body = gson.fromJson(request.body(), RegisterRequest.class);

        try {
            RegisterResult result = userService.register(body);
            return gson.toJson(result);
        } catch (UsernameTakenException exception) {
            response.status(403);
            return gson.toJson(new ErrorMessage(exception.getMessage()));
        } catch (BadRequestException exception) {
            response.status(400);
            return gson.toJson(new ErrorMessage(exception.getMessage()));
        }
    }

    private Object login(Request request, Response response) throws DataAccessException {
        Gson gson = new Gson();
        LoginRequest body = gson.fromJson(request.body(), LoginRequest.class);

        try {
            LoginResult result = userService.login(body);
            return gson.toJson(result);
        } catch (UnauthorizedException exception) {
            response.status(401);
            return gson.toJson(new ErrorMessage(exception.getMessage()));
        } catch (BadRequestException exception) {
            response.status(400);
            return gson.toJson(new ErrorMessage(exception.getMessage()));
        }
    }

    public void stop() {
        Spark.stop();
        Spark.awaitStop();
    }
}
