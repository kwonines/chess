package server;

import com.google.gson.Gson;
import service.ClearService;
import service.RequestAndResult.RegisterRequest;
import service.UserService;
import spark.*;

public class Server {

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
        new ClearService().clearApplication();
        return new Gson().toJson(null);
    }

    private Object register(Request request, Response response) {
        var body = new Gson().fromJson(request.body(), RegisterRequest.class);
        return new UserService().register(body);
    }

    public void stop() {
        Spark.stop();
        Spark.awaitStop();
    }
}
