import com.google.gson.Gson;
import model.requestandresult.*;
import java.io.*;
import java.net.HttpURLConnection;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.Objects;

public class ServerFacade {

    public RegisterResult register(String username, String password, String email) throws ResponseException {
        RegisterRequest request = new RegisterRequest(username, password, email);
        return makeRequest("POST", "/user", request, RegisterResult.class, null);

    }

    public LoginResult login(String username, String password) throws ResponseException {
        LoginRequest request = new LoginRequest(username, password);
        return makeRequest("POST", "/session", request, LoginResult.class, null);
    }

    public void logout(String authToken) throws ResponseException {
        LogoutRequest request = new LogoutRequest(authToken);
        makeRequest("DELETE", "/session", request, null, authToken);
    }

    public ListResult listGames(String authToken) throws ResponseException {
        CreateRequest request = new CreateRequest(authToken, null);
        return makeRequest("GET", "/game", request, ListResult.class, authToken);
    }

    public void createGame(String authToken, String gameName) throws ResponseException {
        CreateRequest request = new CreateRequest(authToken, gameName);
        makeRequest("POST", "/game", request, null, authToken);
    }

    private <T> T makeRequest(String method, String path, Object request, Class<T> responseType, String authToken) throws ResponseException {
        try {
            HttpURLConnection connection = (HttpURLConnection) new URI("http://localhost:8080" + path).toURL().openConnection();
            connection.setRequestMethod(method);
            connection.setDoOutput(true);


            if (request != null) {
                connection.addRequestProperty("Content-Type", "application/json");
                connection.addRequestProperty("Authorization", authToken);
                if (!Objects.equals(method, "GET")) {
                    String body = new Gson().toJson(request);
                    try (OutputStream writeBody = connection.getOutputStream()) {
                        writeBody.write(body.getBytes());
                    }
                }
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
                try (InputStream errorResponse = connection.getErrorStream()) {
                    InputStreamReader errorReader = new InputStreamReader(errorResponse);
                    ErrorMessage message = new Gson().fromJson(errorReader, ErrorMessage.class);
                    throw new ResponseException(message.message());
                }
            }

        } catch (IOException | URISyntaxException exception) {
            throw new RuntimeException(exception);
        }
    }
}
