package server;

import com.google.gson.Gson;
import dataaccess.BadRequestException;
import dataaccess.DataAccessException;
import dataaccess.UsernameTakenException;
import service.ClearService;
import service.RequestAndResult.RegisterRequest;
import service.RequestAndResult.RegisterResult;
import service.UserService;
import spark.*;

public class Server {

    UserService userService;
    ClearService clearService;

    public Server() {
        userService = new UserService();
        clearService = new ClearService();
    }

    public int run(int desiredPort) {
        Spark.port(desiredPort);

        Spark.staticFiles.location("web");

        // Register your endpoints and handle exceptions here.
        Spark.delete("/db", this::clear);
        Spark.post("/user", this::register);

        //This line initializes the server and can be removed once you have a functioning endpoint 
        Spark.init();

        Spark.awaitInitialization();
        return Spark.port();
    }

    private Object clear(Request request, Response response) {
        clearService.clearApplication();
        return new Gson().toJson(null);
    }

    private Object register(Request request, Response response) throws DataAccessException {
        Gson gson = new Gson();
        RegisterRequest body = gson.fromJson(request.body(), RegisterRequest.class);
        String returnVal;
        RegisterResult result = null;
        try {
            result = userService.register(body);
            returnVal = gson.toJson(result);
        } catch (UsernameTakenException exception) {
            response.status(403);
            return gson.toJson(new Error(exception.getMessage()));
        } catch (BadRequestException exception) {
            response.status(400);
            return gson.toJson(new Error(exception.getMessage()));
        }
        return returnVal;
    }

    public void stop() {
        Spark.stop();
        Spark.awaitStop();
    }
}
