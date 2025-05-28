import chess.ChessPosition;
import com.google.gson.Gson;
import model.requestandresult.RegisterRequest;
import model.requestandresult.RegisterResult;

import java.io.*;
import java.net.HttpURLConnection;
import java.net.URI;
import java.net.URISyntaxException;

public class ServerFacade {

    public RegisterResult register(String username, String password, String email) {
        RegisterRequest request = new RegisterRequest(username, password, email);
        return makeRequest("POST", "/user", request, RegisterResult.class);

    }

    private <T> T makeRequest(String method, String path, Object request, Class<T> responseTypes) {
        return makeRequest(method, path, request, responseTypes, null);
    }

    private <T> T makeRequest(String method, String path, Object request, Class<T> responseType, String authToken) {
        try {
            HttpURLConnection connection = (HttpURLConnection) new URI("http://localhost:8080" + path).toURL().openConnection();
            connection.setRequestMethod(method);
            connection.setDoOutput(true);


            if (request != null) {
                connection.addRequestProperty("Content-Type", "application/json");
                String body = new Gson().toJson(request);
                try (OutputStream writeBody = connection.getOutputStream()) {
                    writeBody.write(body.getBytes());
                }
            }

            if (authToken != null) {
                connection.addRequestProperty("authorization", authToken);
            }

            connection.connect();

            if (connection.getResponseCode() == HttpURLConnection.HTTP_OK) {
                if (responseType != null) {
                    try (InputStream response = connection.getInputStream()) {
                        InputStreamReader reader = new InputStreamReader(response);
                        return new Gson().fromJson(reader, responseType);
                    }
                } else {
                    return null;
                }
            } else {
                System.out.println("you did it wrong");
                return null;
            }

        } catch (IOException | URISyntaxException exception) {
            throw new RuntimeException(exception);
        }
    }
}
