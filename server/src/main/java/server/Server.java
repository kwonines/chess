package server;

import com.google.gson.Gson;
import dataaccess.MemoryAuthDataAccess;
import dataaccess.MemoryGameDataAccess;
import dataaccess.MemoryUserDataAccess;
import dataaccess.exceptions.BadRequestException;
import dataaccess.DataAccessException;
import dataaccess.exceptions.ColorTakenException;
import dataaccess.exceptions.UnauthorizedException;
import dataaccess.exceptions.UsernameTakenException;
import service.ClearService;
import service.GameService;
import service.RequestAndResult.*;
import service.UserService;
import spark.*;

public class Server {

    UserService userService;
    GameService gameService;
    ClearService clearService;
    MemoryUserDataAccess userDataAccess = new MemoryUserDataAccess();
    MemoryAuthDataAccess authDataAccess = new MemoryAuthDataAccess();
    MemoryGameDataAccess gameDataAccess = new MemoryGameDataAccess();

    public Server() {
        userService = new UserService(userDataAccess, authDataAccess);
        clearService = new ClearService(userDataAccess, authDataAccess, gameDataAccess);
        gameService = new GameService(authDataAccess, gameDataAccess);
    }

    public int run(int desiredPort) {
        Spark.port(desiredPort);

        Spark.staticFiles.location("web");

        // Register your endpoints and handle exceptions here.
        Spark.delete("/db", this::clear);
        Spark.post("/user", this::register);
        Spark.post("/session", this::login);
        Spark.delete("/session", this::logout);
        Spark.get("/game", this::listGames);
        Spark.post("/game", this::createGame);
        Spark.put("/game", this::joinGame);

        //This line initializes the server and can be removed once you have a functioning endpoint 
        Spark.init();

        Spark.awaitInitialization();
        return Spark.port();
    }

    private Object joinGame(Request request, Response response) throws DataAccessException {
        Gson gson = new Gson();
        String authToken = request.headers("Authorization");
        JoinRequest joinRequest = gson.fromJson(request.body(), JoinRequest.class);

        try {
            gameService.joinGame(new JoinRequest(joinRequest.playerColor(), joinRequest.gameID(), authToken));
            return gson.toJson(null);
        } catch (UnauthorizedException exception) {
            response.status(401);
            return gson.toJson(new ErrorMessage(exception.getMessage()));
        } catch (BadRequestException exception) {
            response.status(400);
            return gson.toJson(new ErrorMessage(exception.getMessage()));
        } catch (ColorTakenException exception) {
            response.status(403);
            return gson.toJson(new ErrorMessage(exception.getMessage()));
        }
    }

    private Object createGame(Request request, Response response) throws DataAccessException {
        Gson gson = new Gson();
        String authToken = request.headers("Authorization");
        CreateRequest createRequest = gson.fromJson(request.body(), CreateRequest.class);

        try {
            CreateResult result = gameService.createGame(new CreateRequest(authToken, createRequest.gameName()));
            return gson.toJson(result);
        } catch (UnauthorizedException exception) {
            response.status(401);
            return gson.toJson(new ErrorMessage(exception.getMessage()));
        } catch (BadRequestException exception) {
            response.status(400);
            return gson.toJson(new ErrorMessage(exception.getMessage()));
        }
    }

    private Object listGames(Request request, Response response) throws DataAccessException {
        Gson gson = new Gson();
        String authToken = request.headers("Authorization");
        ListRequest listRequest = new ListRequest(authToken);

        try {
            ListResult listResult = gameService.listGames(listRequest);
            return gson.toJson(listResult);
        } catch (UnauthorizedException exception) {
            response.status(401);
            return gson.toJson(new ErrorMessage(exception.getMessage()));
        }
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
